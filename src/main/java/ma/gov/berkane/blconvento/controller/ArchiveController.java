package ma.gov.berkane.blconvento.controller;

import ma.gov.berkane.blconvento.service.ConventionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveController {

    private final ConventionService conventionService;

    public ArchiveController(ConventionService conventionService) {
        this.conventionService = conventionService;
    }

    @GetMapping("/archives")
    public String index(Model model) {

        model.addAttribute("conventions", conventionService.findArchived());
        model.addAttribute("contentTemplate", "archives/index");
        model.addAttribute("currentPage", "archives");
        model.addAttribute("pageTitle", "Archives");

        return "layouts/app";
    }
}
