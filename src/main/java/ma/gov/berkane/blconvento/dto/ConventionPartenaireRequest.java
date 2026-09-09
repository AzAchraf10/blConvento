package ma.gov.berkane.blconvento.dto;

public class ConventionPartenaireRequest {

    private String codePartenaire;

    private boolean aSigne;

    private Double montantInvesti;

    /**
     * Transitoire : coché côté formulaire pour indiquer que ce partenaire
     * (parmi tout le référentiel affiché) est réellement engagé sur cette
     * convention. Ne correspond à aucune colonne, filtré dans le service.
     */
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCodePartenaire() {
        return codePartenaire;
    }

    public void setCodePartenaire(String codePartenaire) {
        this.codePartenaire = codePartenaire;
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
