package ma.gov.berkane.blconvento.repository;

import ma.gov.berkane.blconvento.entity.PasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordResetRequestRepository
        extends JpaRepository<PasswordResetRequest, Long> {

    List<PasswordResetRequest> findByResolvedFalseOrderByCreatedAtDesc();
}