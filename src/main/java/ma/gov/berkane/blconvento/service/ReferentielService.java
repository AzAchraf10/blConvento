package ma.gov.berkane.blconvento.service;

import ma.gov.berkane.blconvento.entity.*;
import ma.gov.berkane.blconvento.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferentielService {

    private final MaitreOuvrageRepository moRepository;
    private final CommuneRepository communeRepository;
    private final SecteurRepository secteurRepository;
    private final PartenaireRepository partenaireRepository;

    public ReferentielService(
            MaitreOuvrageRepository moRepository,
            CommuneRepository communeRepository,
            SecteurRepository secteurRepository,
            PartenaireRepository partenaireRepository) {

        this.moRepository = moRepository;
        this.communeRepository = communeRepository;
        this.secteurRepository = secteurRepository;
        this.partenaireRepository = partenaireRepository;
    }

    public List<MaitreOuvrage> getMaitresOuvrage() {
        return moRepository.findAll();
    }

    public List<Commune> getCommunes() {
        return communeRepository.findAll();
    }

    public List<Secteur> getSecteurs() {
        return secteurRepository.findAll();
    }

    public List<Partenaire> getPartenaires() {
        return partenaireRepository.findAll();
    }

    public MaitreOuvrage createMaitreOuvrage(
            String nom) {

        checkSimilar(
                nom,
                moRepository.findAll()
                        .stream()
                        .map(MaitreOuvrage::getNom)
                        .toList()
        );

        MaitreOuvrage mo =
                new MaitreOuvrage();

        mo.setNom(nom.trim());

        return moRepository.save(mo);
    }

    public Commune createCommune(
            String nom) {

        checkSimilar(
                nom,
                communeRepository.findAll()
                        .stream()
                        .map(Commune::getNom)
                        .toList()
        );

        Commune commune = new Commune();
        commune.setNom(nom.trim());

        return communeRepository.save(commune);
    }

    public Secteur createSecteur(
            String nom) {

        checkSimilar(
                nom,
                secteurRepository.findAll()
                        .stream()
                        .map(Secteur::getNom)
                        .toList()
        );

        Secteur secteur = new Secteur();
        secteur.setNom(nom.trim());

        return secteurRepository.save(secteur);
    }

    public Partenaire createPartenaire(
            String code,
            String nom) {

        code = code.trim().toUpperCase();

        if (partenaireRepository.existsById(code)) {
            throw new IllegalArgumentException(
                    "Ce code partenaire existe déjà."
            );
        }

        checkSimilar(
                nom,
                partenaireRepository.findAll()
                        .stream()
                        .map(Partenaire::getNom)
                        .toList()
        );

        Partenaire partenaire =
                new Partenaire();

        partenaire.setCode(code);
        partenaire.setNom(nom.trim());

        return partenaireRepository.save(partenaire);
    }

    private void checkSimilar(
            String value,
            List<String> existing) {

        String normalized =
                normalize(value);

        for (String item : existing) {

            String existingNormalized =
                    normalize(item);

            if (normalized.equals(
                    existingNormalized)) {

                throw new IllegalArgumentException(
                        "Une valeur similaire existe déjà : "
                                + item
                );
            }

            int distance =
                    levenshtein(
                            normalized,
                            existingNormalized
                    );

            int max =
                    Math.max(
                            normalized.length(),
                            existingNormalized.length()
                    );

            if ((max <= 5 && distance <= 1)
                    || (max > 5 && distance <= 2)) {

                throw new IllegalArgumentException(
                        "Une valeur trop similaire existe déjà : "
                                + item
                );
            }
        }
    }

    private String normalize(String value) {

        if (value == null) {
            return "";
        }

        return java.text.Normalizer
                .normalize(
                        value.trim().toLowerCase(),
                        java.text.Normalizer.Form.NFD
                )
                .replaceAll(
                        "\\p{M}",
                        ""
                )
                .replaceAll(
                        "[^a-z0-9]",
                        ""
                );
    }

    private int levenshtein(
            String a,
            String b) {

        int[][] dp =
                new int[a.length() + 1]
                        [b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= b.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= a.length(); i++) {

            for (int j = 1; j <= b.length(); j++) {

                int cost =
                        a.charAt(i - 1)
                                == b.charAt(j - 1)
                                ? 0
                                : 1;

                dp[i][j] =
                        Math.min(
                                Math.min(
                                        dp[i - 1][j] + 1,
                                        dp[i][j - 1] + 1
                                ),
                                dp[i - 1][j - 1] + cost
                        );
            }
        }

        return dp[a.length()][b.length()];
    }
}