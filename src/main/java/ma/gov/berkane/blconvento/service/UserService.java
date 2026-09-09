package ma.gov.berkane.blconvento.service;

import ma.gov.berkane.blconvento.entity.PasswordResetRequest;
import ma.gov.berkane.blconvento.entity.Role;
import ma.gov.berkane.blconvento.entity.User;
import ma.gov.berkane.blconvento.exception.ResourceNotFoundException;
import ma.gov.berkane.blconvento.repository.PasswordResetRequestRepository;
import ma.gov.berkane.blconvento.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordResetRequestRepository resetRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordResetRequestRepository resetRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.resetRepository = resetRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable."
                        ));
    }

    public User create(
            String nom,
            String email,
            String password,
            Role role) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Cet email existe déjà."
            );
        }

        User user = new User();

        user.setNom(nom);
        user.setEmail(email);
        user.setMotDePasse(
                passwordEncoder.encode(password)
        );
        user.setRole(role);
        user.setActif(true);

        return userRepository.save(user);
    }

    public User update(
            Long id,
            String nom,
            String email,
            Role role,
            boolean actif) {

        User user = findById(id);

        user.setNom(nom);
        user.setEmail(email);
        user.setRole(role);
        user.setActif(actif);

        return userRepository.save(user);
    }

    public void delete(Long id) {

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Utilisateur introuvable."
            );
        }

        userRepository.deleteById(id);
    }

    public List<PasswordResetRequest>
    getPendingResetRequests() {

        return resetRepository
                .findByResolvedFalseOrderByCreatedAtDesc();
    }

    public void resolveReset(Long id) {

        PasswordResetRequest request =
                resetRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Demande introuvable."
                                ));

        request.resolve();

        resetRepository.save(request);
    }

    public void deleteReset(Long id) {

        resetRepository.deleteById(id);
    }
}