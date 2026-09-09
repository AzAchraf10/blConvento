package ma.gov.berkane.blconvento.repository;

import ma.gov.berkane.blconvento.entity.MaitreOuvrage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaitreOuvrageRepository
        extends JpaRepository<MaitreOuvrage, Long> {

    boolean existsByNomIgnoreCase(String nom);
}