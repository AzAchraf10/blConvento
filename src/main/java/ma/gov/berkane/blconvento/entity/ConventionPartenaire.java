package ma.gov.berkane.blconvento.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "convention_partenaires")
public class ConventionPartenaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "convention_id", nullable = false)
    private Convention convention;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partenaire_code", nullable = false)
    private Partenaire partenaire;

    @Column(name = "a_signe", nullable = false)
    private boolean aSigne;

    @Column(name = "montant_investi", nullable = false)
    private Double montantInvesti = 0.0;

    public Long getId() {
        return id;
    }

    public Convention getConvention() {
        return convention;
    }

    public void setConvention(Convention convention) {
        this.convention = convention;
    }

    public Partenaire getPartenaire() {
        return partenaire;
    }

    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
    }

    public boolean isASigne() {
        return aSigne;
    }

    public void setASigne(boolean aSigne) {
        this.aSigne = aSigne;
    }

    public Double getMontantInvesti() {
        return montantInvesti;
    }

    public void setMontantInvesti(Double montantInvesti) {
        this.montantInvesti = montantInvesti;
    }
}