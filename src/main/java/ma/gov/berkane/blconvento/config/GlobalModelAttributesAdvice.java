package ma.gov.berkane.blconvento.config;

import ma.gov.berkane.blconvento.entity.User;
import ma.gov.berkane.blconvento.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Le layout (views/layouts/app.html) a été porté depuis Laravel en s'appuyant
 * sur les variables de session `session('user')` (isAuthenticated,
 * currentUser, isAdmin/isSuperAdmin selon le rôle). En Spring Security, ces
 * informations viennent de l'objet Authentication et doivent être injectées
 * dans le modèle de CHAQUE requête MVC. Cette classe manquait entièrement
 * dans le portage : sans elle, le header/la nav du layout ne peuvent jamais
 * savoir qui est connecté ni afficher le bon menu.
 */
@ControllerAdvice
public class GlobalModelAttributesAdvice {

    private final UserRepository userRepository;

    public GlobalModelAttributesAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model, Authentication authentication) {

        boolean isAuthenticated =
                authentication != null
                        && authentication.isAuthenticated()
                        && !(authentication instanceof AnonymousAuthenticationToken);

        model.addAttribute("isAuthenticated", isAuthenticated);

        boolean isAdmin = false;
        boolean isSuperAdmin = false;
        User currentUser = null;

        if (isAuthenticated) {

            currentUser = userRepository.findByEmail(authentication.getName())
                    .orElse(null);

            isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                            || a.getAuthority().equals("ROLE_SUPER_ADMIN"));

            isSuperAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isSuperAdmin", isSuperAdmin);
    }
}
