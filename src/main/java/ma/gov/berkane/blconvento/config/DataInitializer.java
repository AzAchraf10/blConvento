package ma.gov.berkane.blconvento.config;

import ma.gov.berkane.blconvento.entity.*;
import ma.gov.berkane.blconvento.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            MaitreOuvrageRepository moRepository,
            CommuneRepository communeRepository,
            SecteurRepository secteurRepository,
            PartenaireRepository partenaireRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.count() == 0) {

                createUser(
                        userRepository,
                        passwordEncoder,
                        "Imad BOULOUIZ",
                        "imad.boulouiz@dsit.gov.ma",
                        Role.SUPER_ADMIN
                );

                createUser(
                        userRepository,
                        passwordEncoder,
                        "Agent Admin DSIT",
                        "admin@dsit.gov.ma",
                        Role.ADMIN
                );

                createUser(
                        userRepository,
                        passwordEncoder,
                        "Responsable Consultation",
                        "responsable@dsit.gov.ma",
                        Role.CONSULTATION
                );
            }

            if (moRepository.count() == 0) {

                createMO(
                        moRepository,
                        "Commune de Berkane"
                );

                createMO(
                        moRepository,
                        "RADEEO"
                );

                createMO(
                        moRepository,
                        "DPA Berkane (Agriculture)"
                );

                createMO(
                        moRepository,
                        "Ministère de la Culture"
                );

                createMO(
                        moRepository,
                        "Conseil Provincial de Berkane"
                );
            }

            if (communeRepository.count() == 0) {

                createCommune(
                        communeRepository,
                        "Berkane"
                );

                createCommune(
                        communeRepository,
                        "Ahfir"
                );

                createCommune(
                        communeRepository,
                        "Saïdia"
                );

                createCommune(
                        communeRepository,
                        "Aklim"
                );

                createCommune(
                        communeRepository,
                        "Madagh"
                );

                createCommune(
                        communeRepository,
                        "Sidi Slimane Ech-Charaa"
                );
            }

            if (secteurRepository.count() == 0) {

                createSecteur(
                        secteurRepository,
                        "Aménagement Urbain"
                );

                createSecteur(
                        secteurRepository,
                        "Eau Potable & Assainissement"
                );

                createSecteur(
                        secteurRepository,
                        "Sports & Jeunesse"
                );

                createSecteur(
                        secteurRepository,
                        "Culture & Patrimoine"
                );

                createSecteur(
                        secteurRepository,
                        "Infrastructure Routière"
                );
            }

            if (partenaireRepository.count() == 0) {

                createPartenaire(
                        partenaireRepository,
                        "PART-001",
                        "Ministère de l'Intérieur"
                );

                createPartenaire(
                        partenaireRepository,
                        "PART-002",
                        "Conseil Régional de l'Orientale"
                );

                createPartenaire(
                        partenaireRepository,
                        "PART-003",
                        "DGCT"
                );

                createPartenaire(
                        partenaireRepository,
                        "PART-004",
                        "INDH"
                );
            }
        };
    }

    private void createUser(
            UserRepository repository,
            PasswordEncoder encoder,
            String nom,
            String email,
            Role role) {

        User user = new User();

        user.setNom(nom);
        user.setEmail(email);
        user.setMotDePasse(
                encoder.encode("password123")
        );
        user.setRole(role);
        user.setActif(true);

        repository.save(user);
    }

    private void createMO(
            MaitreOuvrageRepository repository,
            String nom) {

        MaitreOuvrage mo =
                new MaitreOuvrage();

        mo.setNom(nom);

        repository.save(mo);
    }

    private void createCommune(
            CommuneRepository repository,
            String nom) {

        Commune commune =
                new Commune();

        commune.setNom(nom);

        repository.save(commune);
    }

    private void createSecteur(
            SecteurRepository repository,
            String nom) {

        Secteur secteur =
                new Secteur();

        secteur.setNom(nom);

        repository.save(secteur);
    }

    private void createPartenaire(
            PartenaireRepository repository,
            String code,
            String nom) {

        Partenaire partenaire =
                new Partenaire();

        partenaire.setCode(code);
        partenaire.setNom(nom);

        repository.save(partenaire);
    }
}