package vttp.ssf.project.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/auth/login";
    }
    
    @GetMapping("/status")
    public String status() {
        return "redirect:/auth/login";
    }
} 