package ma.gov.berkane.blconvento.repository;

import ma.gov.berkane.blconvento.entity.Commune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommuneRepository
        extends JpaRepository<Commune, Long> {

    boolean existsByNomIgnoreCase(String nom);
}