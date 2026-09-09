package ma.gov.berkane.blconvento.controller;

import ma.gov.berkane.blconvento.entity.User;
import ma.gov.berkane.blconvento.repository.UserRepository;
import ma.gov.berkane.blconvento.service.ProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final ProfileService profileService;
    private final UserRepository userRepository;

    public ProfileController(
            ProfileService profileService,
            UserRepository userRepository) {

        this.profileService = profileService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String show(Authentication authentication, Model model) {

        User user = getCurrentUser(authentication);

        model.addAttribute("user", user);
        model.addAttribute("contentTemplate", "profile/index");
        model.addAttribute("currentPage", "profile");
        model.addAttribute("pageTitle", "Mon Profil");

        return "layouts/app";
    }

    @PostMapping("/profile")
    public String update(
            Authentication authentication,
            @RequestParam String nom,
            @RequestParam String email,
            @RequestParam(required = false) String newPassword,
            RedirectAttributes redirectAttributes) {

        User user = getCurrentUser(authentication);

        try {
            profileService.updateProfile(user.getId(), nom, email, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Profil mis à jour avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/profile";
    }

    private User getCurrentUser(Authentication authentication) {

        return userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();
    }
}
