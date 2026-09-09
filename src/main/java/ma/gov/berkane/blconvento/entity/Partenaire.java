package ma.gov.berkane.blconvento.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "partenaires")
public class Partenaire {

    @Id
    @Column(name = "code_partenaire", length = 50)
    private String code;

    @Column(name = "nom_partenaire", nullable = false, length = 255)
    private String nom;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}