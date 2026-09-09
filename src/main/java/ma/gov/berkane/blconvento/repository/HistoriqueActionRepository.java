package ma.gov.berkane.blconvento.repository;

import ma.gov.berkane.blconvento.entity.HistoriqueAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueActionRepository
        extends JpaRepository<HistoriqueAction, Long> {

    List<HistoriqueAction> findAllByOrderByDateActionDesc();
}