package ma.gov.berkane.blconvento.controller;

import ma.gov.berkane.blconvento.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/password-reset-request")
    public String requestPasswordReset(
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {

        try {
            authService.requestPasswordReset(email);
            redirectAttributes.addFlashAttribute("successMessage", "Votre demande a été envoyée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/login";
    }
}
