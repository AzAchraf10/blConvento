package ma.gov.berkane.blconvento.controller;

import jakarta.validation.Valid;
import ma.gov.berkane.blconvento.dto.ConventionRequest;
import ma.gov.berkane.blconvento.entity.Convention;
import ma.gov.berkane.blconvento.entity.User;
import ma.gov.berkane.blconvento.repository.UserRepository;
import ma.gov.berkane.blconvento.service.ConventionService;
import ma.gov.berkane.blconvento.service.ReferentielService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/conventions")
public class ConventionController {

    private final ConventionService conventionService;
    private final ReferentielService referentielService;
    private final UserRepository userRepository;

    public ConventionController(
            ConventionService conventionService,
            ReferentielService referentielService,
            UserRepository userRepository) {

        this.conventionService = conventionService;
        this.referentielService = referentielService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String index(Model model) {

        java.util.List<Convention> conventions = conventionService.findActive();

        model.addAttribute("conventions", conventions);
        model.addAttribute("stats", buildStats(conventions));
        addReferentiels(model);
        model.addAttribute("contentTemplate", "conventions/index");
        model.addAttribute("currentPage", "dashboard");
        model.addAttribute("pageTitle", "Tableau de Bord");

        return "layouts/app";
    }

    private java.util.Map<String, Object> buildStats(java.util.List<Convention> conventions) {

        java.util.Map<String, Object> stats = new java.util.HashMap<>();

        double totalMontant = conventions.stream().mapToDouble(c -> c.getMontant() == null ? 0 : c.getMontant()).sum();
        double avgMontant = conventions.isEmpty() ? 0 : totalMontant / conventions.size();
        double avgEngagement = conventions.isEmpty() ? 0
                : conventions.stream().mapToDouble(c -> c.getTauxEngagement() == null ? 0 : c.getTauxEngagement()).average().orElse(0);

        long countTerminee = conventions.stream().filter(c -> "Terminée".equals(c.getStatutExecution())).count();
        long countEnCours = conventions.stream().filter(c -> "En cours".equals(c.getStatutExecution())).count();
        long countEnAttente = conventions.size() - countTerminee - countEnCours;

        stats.put("total_montant", totalMontant);
        stats.put("avg_montant", avgMontant);
        stats.put("avg_engagement", Math.round(avgEngagement));
        stats.put("count_terminee", countTerminee);
        stats.put("count_en_cours", countEnCours);
        stats.put("count_en_attente", countEnAttente);

        return stats;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {

        model.addAttribute("convention", conventionService.findById(id));
        model.addAttribute("contentTemplate", "conventions/show");
        model.addAttribute("currentPage", "dashboard");
        model.addAttribute("pageTitle", "Fiche Convention");

        return "layouts/app";
    }

    @GetMapping("/create")
    public String createForm(Model model) {

        if (!model.containsAttribute("conventionRequest")) {
            model.addAttribute("conventionRequest", new ConventionRequest());
        }

        addReferentiels(model);
        model.addAttribute("existingPartenairesByCode", java.util.Collections.emptyMap());
        model.addAttribute("contentTemplate", "conventions/create");
        model.addAttribute("currentPage", "conventions-create");
        model.addAttribute("pageTitle", "Nouvelle Convention");

        return "layouts/app";
    }

    @PostMapping
    public String store(
            @Valid @ModelAttribute ConventionRequest request,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            User user = getCurrentUser(authentication);
            conventionService.create(request, user);
            redirectAttributes.addFlashAttribute("successMessage", "Convention créée avec succès.");
            return "redirect:/conventions";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("conventionRequest", request);
            return "redirect:/conventions/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {

        Convention convention = conventionService.findById(id);

        model.addAttribute("convention", convention);

        if (!model.containsAttribute("conventionRequest")) {
            model.addAttribute("conventionRequest", ConventionRequest.fromEntity(convention));
        }

        addReferentiels(model);

        ConventionRequest req = (ConventionRequest) model.asMap().get("conventionRequest");
        java.util.Map<String, ma.gov.berkane.blconvento.dto.ConventionPartenaireRequest> existingByCode = new java.util.HashMap<>();
        if (req != null && req.getPartenaires() != null) {
            for (ma.gov.berkane.blconvento.dto.ConventionPartenaireRequest p : req.getPartenaires()) {
                existingByCode.put(p.getCodePartenaire(), p);
            }
        }
        model.addAttribute("existingPartenairesByCode", existingByCode);

        model.addAttribute("contentTemplate", "conventions/edit");
        model.addAttribute("currentPage", "dashboard");
        model.addAttribute("pageTitle", "Modifier la Convention");

        return "layouts/app";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute ConventionRequest request,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            User user = getCurrentUser(authentication);
            conventionService.update(id, request, user);
            redirectAttributes.addFlashAttribute("successMessage", "Convention mise à jour avec succès.");
            return "redirect:/conventions/" + id;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("conventionRequest", request);
            return "redirect:/conventions/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/archive")
    public String archive(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        conventionService.archive(id, getCurrentUser(authentication));
        redirectAttributes.addFlashAttribute("successMessage", "Convention archivée.");

        return "redirect:/conventions";
    }

    @PostMapping("/{id}/restore")
    public String restore(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        conventionService.restore(id, getCurrentUser(authentication));
        redirectAttributes.addFlashAttribute("successMessage", "Convention restaurée au tableau de bord.");

        return "redirect:/archives";
    }

    @PostMapping("/check-duplicate")
    @ResponseBody
    public boolean checkDuplicate(
            @RequestParam String intitule,
            @RequestParam(required = false) Long id) {

        return conventionService.isDuplicate(intitule, id);
    }

    private void addReferentiels(Model model) {
        model.addAttribute("maitresOuvrage", referentielService.getMaitresOuvrage());
        model.addAttribute("communes", referentielService.getCommunes());
        model.addAttribute("secteurs", referentielService.getSecteurs());
        model.addAttribute("partenaires", referentielService.getPartenaires());
    }

    private User getCurrentUser(Authentication authentication) {

        return userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();
    }
}
