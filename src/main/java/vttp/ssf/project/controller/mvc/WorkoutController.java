package vttp.ssf.project.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vttp.ssf.project.model.Workout;
import vttp.ssf.project.service.WorkoutService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {
    
    @Autowired
    private WorkoutService workoutService;
    
    @GetMapping
    public String listWorkouts(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("workouts", workoutService.getUserWorkouts(userId));
        return "workout/list";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("workout", new Workout());
        return "workout/create";
    }
    
    @PostMapping("/create")
    public String createWorkout(@ModelAttribute Workout workout, 
                              HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }
        
        workout.setUserId(userId);
        workoutService.saveWorkout(workout);
        return "redirect:/workouts";
    }
} 