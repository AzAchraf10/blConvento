package ma.gov.berkane.blconvento.service;

import ma.gov.berkane.blconvento.entity.PasswordResetRequest;
import ma.gov.berkane.blconvento.entity.User;
import ma.gov.berkane.blconvento.repository.PasswordResetRequestRepository;
import ma.gov.berkane.blconvento.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetRequestRepository resetRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordResetRequestRepository resetRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.resetRepository = resetRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(
            String email,
            String password) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Email ou mot de passe incorrect."
                                ));

        if (!user.isActif()) {
            throw new RuntimeException(
                    "Ce compte est désactivé."
            );
        }

        if (!passwordEncoder.matches(
                password,
                user.getMotDePasse())) {

            throw new RuntimeException(
                    "Email ou mot de passe incorrect."
            );
        }

        return user;
    }

    public void requestPasswordReset(
            String email) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Utilisateur introuvable."
                                ));

        PasswordResetRequest request =
                new PasswordResetRequest();

        request.setUser(user);

        resetRepository.save(request);
    }
}