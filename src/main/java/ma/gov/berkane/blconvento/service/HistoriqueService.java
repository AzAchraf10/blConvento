package ma.gov.berkane.blconvento.service;

import ma.gov.berkane.blconvento.entity.Convention;
import ma.gov.berkane.blconvento.entity.HistoriqueAction;
import ma.gov.berkane.blconvento.entity.User;
import ma.gov.berkane.blconvento.repository.HistoriqueActionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoriqueService {

    private final HistoriqueActionRepository repository;

    public HistoriqueService(
            HistoriqueActionRepository repository) {

        this.repository = repository;
    }

    public void log(
            User user,
            Convention convention,
            String action) {

        HistoriqueAction historique =
                new HistoriqueAction();

        historique.setUser(user);
        historique.setConvention(convention);
        historique.setAction(action);

        repository.save(historique);
    }

    public List<HistoriqueAction> findAll() {
        return repository.findAllByOrderByDateActionDesc();
    }

    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "Historique introuvable."
            );
        }

        repository.deleteById(id);
    }

    public void clear() {
        repository.deleteAll();
    }
}