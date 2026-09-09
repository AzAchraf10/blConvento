package ma.gov.berkane.blconvento.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "secteurs")
public class Secteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_secteur")
    private Long id;

    @Column(name = "nom_secteur", nullable = false, length = 255)
    private String nom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}