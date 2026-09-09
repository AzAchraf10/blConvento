package ma.gov.berkane.blconvento.repository;

import ma.gov.berkane.blconvento.entity.Secteur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecteurRepository
        extends JpaRepository<Secteur, Long> {

    boolean existsByNomIgnoreCase(String nom);
}