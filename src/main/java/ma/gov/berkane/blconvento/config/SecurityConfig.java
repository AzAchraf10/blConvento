package ma.gov.berkane.blconvento.config;

import ma.gov.berkane.blconvento.entity.User;
import ma.gov.berkane.blconvento.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository repository) {

        return username -> {

            User user =
                    repository.findByEmail(username)
                            .orElseThrow(() ->
                                    new UsernameNotFoundException(
                                            "Utilisateur introuvable."
                                    ));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getMotDePasse())
                    .roles(user.getRole().name())
                    .disabled(!user.isActif())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/login",
                                "/password-reset-request",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        .requestMatchers(
                                "/users/**",
                                "/historique/**",
                                "/referentiels/**"
                        ).hasRole("SUPER_ADMIN")

                        .requestMatchers(
                                "/conventions/create",
                                "/conventions/check-duplicate",
                                "/conventions/*/edit",
                                "/conventions/*/archive",
                                "/conventions/*/restore"
                        ).hasAnyRole(
                                "ADMIN",
                                "SUPER_ADMIN"
                        )

                        .requestMatchers(
                                "/conventions/**",
                                "/archives",
                                "/profile"
                        ).authenticated()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl(
                                "/",
                                true
                        )
                        .failureUrl(
                                "/login?error=true"
                        )
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}