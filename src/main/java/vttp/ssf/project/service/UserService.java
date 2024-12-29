package vttp.ssf.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp.ssf.project.model.UserProfile;
import vttp.ssf.project.model.Workout;
import vttp.ssf.project.repository.UserRepository;
import vttp.ssf.project.repository.WorkoutRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WorkoutRepository workoutRepository;
    
    public UserProfile getUserProfile(String userId) {
        return userRepository.findProfileById(userId);
    }
    
    public void saveUserProfile(UserProfile profile) {
        userRepository.saveProfile(profile);
    }
    
    public WeeklyStats getWeeklyStats(String userId) {
        UserProfile profile = getUserProfile(userId);
        List<Workout> workouts = workoutRepository.findByUserId(userId);
        WeeklyStats stats = new WeeklyStats();

        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
            userRepository.saveProfile(profile);
            return stats;
        }
        
        LocalDateTime currentWeekStart = LocalDateTime.now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .withHour(0).withMinute(0);
        
        if (profile.getWeekStartDate().isBefore(currentWeekStart)) {
            profile.setWeekStartDate(currentWeekStart);
            userRepository.saveProfile(profile);
        }
        
        for (Workout w : workouts) {
            if (w.getDateTime().isAfter(profile.getWeekStartDate())) {
                stats.totalWorkouts++;
                stats.totalMinutes += w.getDuration();
                stats.totalCalories += w.getCalories();
            }
        }
        return stats;
    }
    
    public int calculateWeeklyProgress(String userId) {
        UserProfile profile = getUserProfile(userId);
        if (profile == null || profile.getWeeklyGoal() == null || profile.getWeeklyGoal() == 0) {
            return 0;
        }
        
        WeeklyStats stats = getWeeklyStats(userId);
        return (int) ((stats.totalMinutes * 100.0) / profile.getWeeklyGoal());
    }
    
    public static class WeeklyStats {
        public int totalWorkouts = 0;
        public int totalMinutes = 0;
        public int totalCalories = 0;
    }
} 