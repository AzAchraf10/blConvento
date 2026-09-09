package ma.gov.berkane.blconvento.controller;

import ma.gov.berkane.blconvento.service.ReferentielService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/referentiels")
public class ReferentielController {

    private final ReferentielService service;

    public ReferentielController(ReferentielService service) {
        this.service = service;
    }

    @GetMapping
    public String index(Model model) {

        model.addAttribute("maitresOuvrage", service.getMaitresOuvrage());
        model.addAttribute("communes", service.getCommunes());
        model.addAttribute("secteurs", service.getSecteurs());
        model.addAttribute("partenaires", service.getPartenaires());
        model.addAttribute("contentTemplate", "referentiels/index");
        model.addAttribute("currentPage", "referentiels");
        model.addAttribute("pageTitle", "Référentiels");

        return "layouts/app";
    }

    @PostMapping("/maitres-ouvrage")
    public String createMaitre(@RequestParam String nom, RedirectAttributes redirectAttributes) {
        return handle(() -> service.createMaitreOuvrage(nom), redirectAttributes,
                "Maître d'ouvrage ajouté.");
    }

    @PostMapping("/communes")
    public String createCommune(@RequestParam String nom, RedirectAttributes redirectAttributes) {
        return handle(() -> service.createCommune(nom), redirectAttributes,
                "Commune ajoutée.");
    }

    @PostMapping("/secteurs")
    public String createSecteur(@RequestParam String nom, RedirectAttributes redirectAttributes) {
        return handle(() -> service.createSecteur(nom), redirectAttributes,
                "Secteur ajouté.");
    }

    @PostMapping("/partenaires")
    public String createPartenaire(
            @RequestParam String code,
            @RequestParam String nom,
            RedirectAttributes redirectAttributes) {

        return handle(() -> service.createPartenaire(code, nom), redirectAttributes,
                "Partenaire ajouté.");
    }

    private String handle(Runnable action, RedirectAttributes redirectAttributes, String successMessage) {

        try {
            action.run();
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/referentiels";
    }
}
