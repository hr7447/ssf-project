package vttp.ssf.project.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vttp.ssf.project.model.User;
import vttp.ssf.project.service.AuthService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @GetMapping
    public String authRoot() {
        return "redirect:/auth/login";
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        authService.registerUser(user);
        return "redirect:/auth/login";
    }
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password, 
                       HttpSession session,
                       Model model) {
        User user = authService.authenticateUser(username, password);
        
        if (user != null) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            return "redirect:/workouts";
        }
        
        model.addAttribute("error", "Invalid username or password");
        model.addAttribute("user", new User());
        return "auth/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
} 