package ma.gov.berkane.blconvento.repository;

import ma.gov.berkane.blconvento.entity.Convention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConventionRepository
        extends JpaRepository<Convention, Long> {

    List<Convention> findByArchivedFalse();

    List<Convention> findByArchivedTrue();

    List<Convention> findByIntituleContainingIgnoreCaseAndArchivedFalse(
            String intitule
    );

    boolean existsByIntituleIgnoreCase(String intitule);
}