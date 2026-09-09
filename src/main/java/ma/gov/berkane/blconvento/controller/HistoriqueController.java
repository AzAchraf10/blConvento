package ma.gov.berkane.blconvento.controller;

import ma.gov.berkane.blconvento.service.HistoriqueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/historique")
public class HistoriqueController {

    private final HistoriqueService historiqueService;

    public HistoriqueController(HistoriqueService historiqueService) {
        this.historiqueService = historiqueService;
    }

    @GetMapping
    public String index(Model model) {

        model.addAttribute("historiques", historiqueService.findAll());
        model.addAttribute("contentTemplate", "historique/index");
        model.addAttribute("currentPage", "historique");
        model.addAttribute("pageTitle", "Traçabilité Audit");

        return "layouts/app";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        historiqueService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Entrée supprimée de l'historique.");

        return "redirect:/historique";
    }

    @PostMapping("/clear")
    public String clear(RedirectAttributes redirectAttributes) {

        historiqueService.clear();
        redirectAttributes.addFlashAttribute("successMessage", "Historique vidé.");

        return "redirect:/historique";
    }
}
