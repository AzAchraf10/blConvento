package ma.gov.berkane.blconvento.config;

import ma.gov.berkane.blconvento.entity.User;
import ma.gov.berkane.blconvento.repository.UserRepository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.Authentication;

@ControllerAdvice
public class GlobalModelAttributes {
    private final UserRepository userRepository;
    public GlobalModelAttributes(UserRepository userRepository) { this.userRepository = userRepository; }

    @ModelAttribute
    public void addGlobalAttributes(Authentication authentication, org.springframework.ui.Model model) {
        boolean authenticated = authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
        model.addAttribute("isAuthenticated", authenticated);
        if (authenticated) {
            User user = userRepository.findByEmail(authentication.getName()).orElse(null);
            model.addAttribute("currentUser", user);
            model.addAttribute("isAdmin", user != null && (user.getRole().name().equals("ADMIN") || user.getRole().name().equals("SUPER_ADMIN")));
            model.addAttribute("isSuperAdmin", user != null && user.getRole().name().equals("SUPER_ADMIN"));
        } else {
            model.addAttribute("isAdmin", false);
            model.addAttribute("isSuperAdmin", false);
        }
    }
}
