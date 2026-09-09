package ma.gov.berkane.blconvento.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConventionRequest {

    public static ConventionRequest fromEntity(
            ma.gov.berkane.blconvento.entity.Convention convention) {

        ConventionRequest req = new ConventionRequest();
        req.intitule = convention.getIntitule();
        req.objet = convention.getObjet();
        req.dateDebut = convention.getDateDebut();
        req.dateFin = convention.getDateFin();
        req.idMaitreOuvrage = convention.getMaitreOuvrage() != null
                ? convention.getMaitreOuvrage().getId()
                : null;
        req.statutSignature = convention.getStatutSignature();
        req.statutExecution = convention.getStatutExecution();
        req.tauxEngagement = convention.getTauxEngagement();
        req.montant = convention.getMontant();
        req.communes = convention.getCommunes().stream()
                .map(ma.gov.berkane.blconvento.entity.Commune::getId)
                .collect(java.util.stream.Collectors.toList());
        req.secteurs = convention.getSecteurs().stream()
                .map(ma.gov.berkane.blconvento.entity.Secteur::getId)
                .collect(java.util.stream.Collectors.toList());
        req.partenaires = convention.getPartenaires().stream()
                .map(p -> {
                    ConventionPartenaireRequest pr = new ConventionPartenaireRequest();
                    pr.setCodePartenaire(p.getPartenaire().getCode());
                    pr.setASigne(p.isASigne());
                    pr.setMontantInvesti(p.getMontantInvesti());
                    return pr;
                })
                .collect(java.util.stream.Collectors.toList());
        return req;
    }

    @NotBlank
    private String intitule;

    @NotBlank
    private String objet;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateDebut;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateFin;

    @NotNull
    private Long idMaitreOuvrage;

    @NotBlank
    private String statutSignature;

    @NotBlank
    private String statutExecution;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("100")
    private Double tauxEngagement;

    @NotNull
    @DecimalMin("0")
    private Double montant;

    private List<Long> communes = new ArrayList<>();

    private List<Long> secteurs = new ArrayList<>();

    private List<ConventionPartenaireRequest> partenaires =
            new ArrayList<>();

    private MultipartFile fichierPdf;

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Long getIdMaitreOuvrage() {
        return idMaitreOuvrage;
    }

    public void setIdMaitreOuvrage(Long idMaitreOuvrage) {
        this.idMaitreOuvrage = idMaitreOuvrage;
    }

    public String getStatutSignature() {
        return statutSignature;
    }

    public void setStatutSignature(String statutSignature) {
        this.statutSignature = statutSignature;
    }

    public String getStatutExecution() {
        return statutExecution;
    }

    public void setStatutExecution(String statutExecution) {
        this.statutExecution = statutExecution;
    }

    public Double getTauxEngagement() {
        return tauxEngagement;
    }

    public void setTauxEngagement(Double tauxEngagement) {
        this.tauxEngagement = tauxEngagement;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public List<Long> getCommunes() {
        return communes;
    }

    public List<Long> getSecteurs() {
        return secteurs;
    }

    public List<ConventionPartenaireRequest> getPartenaires() {
        return partenaires;
    }

    public MultipartFile getFichierPdf() {
        return fichierPdf;
    }

    public void setFichierPdf(MultipartFile fichierPdf) {
        this.fichierPdf = fichierPdf;
    }
}