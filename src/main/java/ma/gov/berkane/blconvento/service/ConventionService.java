package ma.gov.berkane.blconvento.service;

import ma.gov.berkane.blconvento.dto.ConventionPartenaireRequest;
import ma.gov.berkane.blconvento.dto.ConventionRequest;
import ma.gov.berkane.blconvento.entity.*;
import ma.gov.berkane.blconvento.exception.ResourceNotFoundException;
import ma.gov.berkane.blconvento.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class ConventionService {

    private final ConventionRepository conventionRepository;
    private final MaitreOuvrageRepository maitreRepository;
    private final CommuneRepository communeRepository;
    private final SecteurRepository secteurRepository;
    private final PartenaireRepository partenaireRepository;
    private final HistoriqueService historiqueService;
    private final FileStorageService fileStorageService;

    public ConventionService(
            ConventionRepository conventionRepository,
            MaitreOuvrageRepository maitreRepository,
            CommuneRepository communeRepository,
            SecteurRepository secteurRepository,
            PartenaireRepository partenaireRepository,
            HistoriqueService historiqueService,
            FileStorageService fileStorageService) {

        this.conventionRepository = conventionRepository;
        this.maitreRepository = maitreRepository;
        this.communeRepository = communeRepository;
        this.secteurRepository = secteurRepository;
        this.partenaireRepository = partenaireRepository;
        this.historiqueService = historiqueService;
        this.fileStorageService = fileStorageService;
    }

    public List<Convention> findActive() {
        return conventionRepository.findByArchivedFalse();
    }

    public List<Convention> findArchived() {
        return conventionRepository.findByArchivedTrue();
    }

    public Convention findById(Long id) {

        return conventionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Convention introuvable : " + id
                        ));
    }

    public Convention create(
            ConventionRequest request,
            User user) {

        if (isDuplicate(request.getIntitule(), null)) {
            throw new IllegalArgumentException(
                    "Une convention similaire existe déjà."
            );
        }

        Convention convention = new Convention();

        fillConvention(convention, request);

        if (request.getFichierPdf() != null &&
                !request.getFichierPdf().isEmpty()) {

            String path =
                    fileStorageService.store(
                            request.getFichierPdf()
                    );

            convention.setCheminFichierPdf(path);
        }

        conventionRepository.save(convention);

        historiqueService.log(
                user,
                convention,
                "Création de la convention : "
                        + convention.getIntitule()
        );

        return convention;
    }

    public Convention update(
            Long id,
            ConventionRequest request,
            User user) {

        Convention convention = findById(id);

        if (isDuplicate(
                request.getIntitule(),
                id)) {

            throw new IllegalArgumentException(
                    "Une convention similaire existe déjà."
            );
        }

        String oldFile =
                convention.getCheminFichierPdf();

        fillConvention(convention, request);

        if (request.getFichierPdf() != null &&
                !request.getFichierPdf().isEmpty()) {

            String newFile =
                    fileStorageService.store(
                            request.getFichierPdf()
                    );

            convention.setCheminFichierPdf(newFile);

            if (oldFile != null) {
                fileStorageService.delete(oldFile);
            }
        }

        conventionRepository.save(convention);

        historiqueService.log(
                user,
                convention,
                "Modification de la convention : "
                        + convention.getIntitule()
        );

        return convention;
    }

    public void archive(
            Long id,
            User user) {

        Convention convention = findById(id);

        convention.setArchived(true);

        historiqueService.log(
                user,
                convention,
                "Archivage de la convention : "
                        + convention.getIntitule()
        );
    }

    public void restore(
            Long id,
            User user) {

        Convention convention = findById(id);

        convention.setArchived(false);

        historiqueService.log(
                user,
                convention,
                "Restauration de la convention : "
                        + convention.getIntitule()
        );
    }

    public void delete(
            Long id,
            User user) {

        Convention convention = findById(id);

        String file =
                convention.getCheminFichierPdf();

        historiqueService.log(
                user,
                convention,
                "Suppression définitive de la convention : "
                        + convention.getIntitule()
        );

        conventionRepository.delete(convention);

        if (file != null) {
            fileStorageService.delete(file);
        }
    }

    public boolean isDuplicate(
            String intitule,
            Long ignoredId) {

        if (intitule == null ||
                intitule.isBlank()) {

            return false;
        }

        String normalized =
                intitule.trim().toLowerCase();

        return conventionRepository
                .findByIntituleContainingIgnoreCaseAndArchivedFalse(
                        normalized
                )
                .stream()
                .anyMatch(c ->
                        ignoredId == null ||
                                !c.getId().equals(ignoredId)
                );
    }

    private void fillConvention(
            Convention convention,
            ConventionRequest request) {

        convention.setIntitule(
                request.getIntitule().trim()
        );

        convention.setObjet(
                request.getObjet().trim()
        );

        convention.setDateDebut(
                request.getDateDebut()
        );

        convention.setDateFin(
                request.getDateFin()
        );

        convention.setStatutSignature(
                request.getStatutSignature()
        );

        convention.setStatutExecution(
                request.getStatutExecution()
        );

        convention.setTauxEngagement(
                request.getTauxEngagement()
        );

        convention.setMontant(
                request.getMontant()
        );

        MaitreOuvrage mo =
                maitreRepository.findById(
                                request.getIdMaitreOuvrage()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maître d'ouvrage introuvable."
                                ));

        convention.setMaitreOuvrage(mo);

        convention.getCommunes().clear();

        if (request.getCommunes() != null) {

            convention.getCommunes().addAll(
                    new HashSet<>(
                            communeRepository
                                    .findAllById(
                                            request.getCommunes()
                                    )
                    )
            );
        }

        convention.getSecteurs().clear();

        if (request.getSecteurs() != null) {

            convention.getSecteurs().addAll(
                    new HashSet<>(
                            secteurRepository
                                    .findAllById(
                                            request.getSecteurs()
                                    )
                    )
            );
        }

        convention.getPartenaires().clear();

        if (request.getPartenaires() != null) {

            for (
                    ConventionPartenaireRequest p
                    : request.getPartenaires()
            ) {

                if (!p.isSelected()
                        || p.getCodePartenaire() == null
                        || p.getCodePartenaire().isBlank()) {
                    continue;
                }

                Partenaire partenaire =
                        partenaireRepository
                                .findById(
                                        p.getCodePartenaire()
                                )
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "Partenaire introuvable."
                                        ));

                ConventionPartenaire cp =
                        new ConventionPartenaire();

                cp.setConvention(convention);
                cp.setPartenaire(partenaire);
                cp.setASigne(p.isASigne());

                cp.setMontantInvesti(
                        p.getMontantInvesti() == null
                                ? 0.0
                                : p.getMontantInvesti()
                );

                convention.getPartenaires()
                        .add(cp);
            }
        }
    }
}