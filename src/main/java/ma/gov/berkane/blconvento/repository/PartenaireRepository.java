package ma.gov.berkane.blconvento.repository;

import ma.gov.berkane.blconvento.entity.Partenaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartenaireRepository
        extends JpaRepository<Partenaire, String> {

    boolean existsByNomIgnoreCase(String nom);
}