package ma.gov.berkane.blconvento.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Racine de l'application. Sous Laravel, "/" et "/dashboard" redirigeaient
 * vers la liste des conventions (le "Tableau de Bord"). Cette route
 * manquait dans le portage Spring Boot (Spring Security redirige vers "/"
 * après connexion, et le menu de navigation pointe vers "/dashboard").
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/conventions";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/conventions";
    }
}
