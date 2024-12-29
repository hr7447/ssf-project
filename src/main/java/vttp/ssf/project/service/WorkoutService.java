package vttp.ssf.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp.ssf.project.model.Workout;
import vttp.ssf.project.repository.WorkoutRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WorkoutService {
    
    @Autowired
    private WorkoutRepository workoutRepository;
    
    @Autowired
    private ExternalApiService externalApiService;
    
    public void saveWorkout(Workout workout) {
        workout.setId(UUID.randomUUID().toString());
        workout.setDateTime(LocalDateTime.now());
        
        if (workout.getCalories() == null || workout.getCalories() == 0) {
            Integer calculatedCalories = externalApiService.calculateCalories(
                workout.getType(), 
                workout.getDuration()
            );
            workout.setCalories(calculatedCalories);
        }
        
        workoutRepository.saveWorkout(workout);
    }
    
    public List<Workout> getUserWorkouts(String userId) {
        return workoutRepository.findByUserId(userId);
    }
} 