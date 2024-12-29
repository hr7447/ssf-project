package vttp.ssf.project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vttp.ssf.project.model.Workout;
import java.util.*;
import java.time.LocalDateTime;

@Repository
public class WorkoutRepository {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public void saveWorkout(Workout workout) {
        String workoutKey = "workout:" + workout.getId();
        redisTemplate.opsForHash().putAll(workoutKey, Map.of(
            "userId", workout.getUserId(),
            "type", workout.getType(),
            "duration", workout.getDuration().toString(),
            "dateTime", workout.getDateTime().toString(),
            "calories", workout.getCalories().toString(),
            "notes", workout.getNotes()
        ));
        
        redisTemplate.opsForSet().add("user:" + workout.getUserId() + ":workouts", workout.getId());
    }
    
    public List<Workout> findByUserId(String userId) {
        Set<Object> workoutIds = redisTemplate.opsForSet().members("user:" + userId + ":workouts");
        List<Workout> workouts = new ArrayList<>();
        
        for (Object workoutId : workoutIds) {
            Map<Object, Object> workoutData = redisTemplate.opsForHash()
                .entries("workout:" + workoutId.toString());
            workouts.add(mapToWorkout(workoutId.toString(), workoutData));
        }
        
        return workouts;
    }
    
    private Workout mapToWorkout(String id, Map<Object, Object> data) {
        Workout workout = new Workout();
        workout.setId(id);
        workout.setUserId((String) data.get("userId"));
        workout.setType((String) data.get("type"));
        workout.setDuration(Integer.parseInt((String) data.get("duration")));
        workout.setDateTime(LocalDateTime.parse((String) data.get("dateTime")));
        workout.setCalories(Integer.parseInt((String) data.get("calories")));
        workout.setNotes((String) data.get("notes"));
        return workout;
    }
} 