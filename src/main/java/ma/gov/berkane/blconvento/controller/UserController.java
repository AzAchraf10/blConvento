package ma.gov.berkane.blconvento.controller;

import ma.gov.berkane.blconvento.entity.Role;
import ma.gov.berkane.blconvento.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model) {

        model.addAttribute("users", userService.findAll());
        model.addAttribute("resetRequests", userService.getPendingResetRequests());
        model.addAttribute("roles", Role.values());
        model.addAttribute("contentTemplate", "users/index");
        model.addAttribute("currentPage", "users");
        model.addAttribute("pageTitle", "Agents & Accès");

        return "layouts/app";
    }

    @PostMapping
    public String create(
            @RequestParam String nom,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam Role role,
            RedirectAttributes redirectAttributes) {

        try {
            userService.create(nom, email, password, role);
            redirectAttributes.addFlashAttribute("successMessage", "Agent créé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/users";
    }

    @PostMapping("/{id}/update")
    public String update(
            @PathVariable Long id,
            @RequestParam String nom,
            @RequestParam String email,
            @RequestParam Role role,
            @RequestParam boolean actif,
            RedirectAttributes redirectAttributes) {

        try {
            userService.update(id, nom, email, role, actif);
            redirectAttributes.addFlashAttribute("successMessage", "Agent mis à jour.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        userService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Agent supprimé.");

        return "redirect:/users";
    }

    @PostMapping("/reset-requests/{id}/resolve")
    public String resolveReset(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        userService.resolveReset(id);
        redirectAttributes.addFlashAttribute("successMessage", "Demande de réinitialisation traitée.");

        return "redirect:/users";
    }

    @PostMapping("/reset-requests/{id}/delete")
    public String deleteReset(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        userService.deleteReset(id);
        redirectAttributes.addFlashAttribute("successMessage", "Demande supprimée.");

        return "redirect:/users";
    }
}
