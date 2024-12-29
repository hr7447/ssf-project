package vttp.ssf.project.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vttp.ssf.project.model.UserProfile;
import vttp.ssf.project.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }
        
        UserProfile profile = userService.getUserProfile(userId);
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
        }
        
        model.addAttribute("profile", profile);
        model.addAttribute("weeklyStats", userService.getWeeklyStats(userId));
        model.addAttribute("weeklyProgress", userService.calculateWeeklyProgress(userId));
        
        return "user/profile";
    }
    
    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute UserProfile profile,
                              BindingResult result,
                              HttpSession session) {
        if (result.hasErrors()) {
            return "user/profile";
        }
        
        String userId = (String) session.getAttribute("userId");
        profile.setUserId(userId);
        userService.saveUserProfile(profile);
        
        return "redirect:/user/profile";
    }
} 