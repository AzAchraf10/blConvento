package ma.gov.berkane.blconvento.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "conventions")
public class Convention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_convention")
    private Long id;

    @Column(nullable = false, length = 500)
    private String intitule;

    @Column(nullable = false, length = 2000)
    private String objet;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "chemin_fichier_pdf")
    private String cheminFichierPdf;

    @Column(name = "statut_signature", nullable = false, length = 50)
    private String statutSignature;

    @Column(name = "statut_execution", nullable = false, length = 50)
    private String statutExecution;

    @Column(name = "taux_engagement", nullable = false)
    private Double tauxEngagement = 0.0;

    @Column(nullable = false)
    private Double montant = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_maitre_ouvrage", nullable = false)
    private MaitreOuvrage maitreOuvrage;

    @ManyToMany
    @JoinTable(
            name = "convention_communes",
            joinColumns = @JoinColumn(name = "convention_id"),
            inverseJoinColumns = @JoinColumn(name = "commune_id")
    )
    private Set<Commune> communes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "convention_secteurs",
            joinColumns = @JoinColumn(name = "convention_id"),
            inverseJoinColumns = @JoinColumn(name = "secteur_id")
    )
    private Set<Secteur> secteurs = new HashSet<>();

    @OneToMany(
            mappedBy = "convention",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ConventionPartenaire> partenaires = new ArrayList<>();

    @Column(name = "is_archived", nullable = false)
    private boolean archived = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

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

    public String getCheminFichierPdf() {
        return cheminFichierPdf;
    }

    public void setCheminFichierPdf(String cheminFichierPdf) {
        this.cheminFichierPdf = cheminFichierPdf;
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

    public MaitreOuvrage getMaitreOuvrage() {
        return maitreOuvrage;
    }

    public void setMaitreOuvrage(MaitreOuvrage maitreOuvrage) {
        this.maitreOuvrage = maitreOuvrage;
    }

    public Set<Commune> getCommunes() {
        return communes;
    }

    public Set<Secteur> getSecteurs() {
        return secteurs;
    }

    public List<ConventionPartenaire> getPartenaires() {
        return partenaires;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}