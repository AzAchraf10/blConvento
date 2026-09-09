package ma.gov.berkane.blconvento.service;

import ma.gov.berkane.blconvento.entity.User;
import ma.gov.berkane.blconvento.exception.ResourceNotFoundException;
import ma.gov.berkane.blconvento.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User updateProfile(
            Long id,
            String nom,
            String email,
            String newPassword) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Utilisateur introuvable."
                                ));

        user.setNom(nom);
        user.setEmail(email);

        if (newPassword != null &&
                !newPassword.isBlank()) {

            user.setMotDePasse(
                    passwordEncoder.encode(
                            newPassword
                    )
            );
        }

        return userRepository.save(user);
    }
}