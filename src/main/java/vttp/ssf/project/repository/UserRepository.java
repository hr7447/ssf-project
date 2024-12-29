package vttp.ssf.project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vttp.ssf.project.model.User;
import java.util.Map;
import java.time.LocalDate;
import vttp.ssf.project.model.UserProfile;
import java.time.LocalDateTime;
import java.util.HashMap;

@Repository
public class UserRepository {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public void saveUser(User user) {
        redisTemplate.opsForHash().put("usernames", user.getUsername(), user.getId());
        
        redisTemplate.opsForHash().putAll("user:" + user.getId(), Map.of(
            "username", user.getUsername(),
            "email", user.getEmail(),
            "password", user.getPassword(),
            "dateOfBirth", user.getDateOfBirth().toString()
        ));
    }
    
    public User findByUsername(String username) {
        String userId = (String) redisTemplate.opsForHash().get("usernames", username);
        if (userId == null) return null;
        
        Map<Object, Object> userData = redisTemplate.opsForHash().entries("user:" + userId);
        if (userData.isEmpty()) return null;
        
        User user = new User();
        user.setId(userId);
        user.setUsername((String) userData.get("username"));
        user.setEmail((String) userData.get("email"));
        user.setPassword((String) userData.get("password"));
        user.setDateOfBirth(LocalDate.parse((String) userData.get("dateOfBirth")));
        return user;
    }
    
    public User findById(String id) {
        Map<Object, Object> userData = redisTemplate.opsForHash().entries("user:" + id);
        if (userData.isEmpty()) return null;
        
        User user = new User();
        user.setId(id);
        user.setUsername((String) userData.get("username"));
        user.setEmail((String) userData.get("email"));
        user.setPassword((String) userData.get("password"));
        user.setDateOfBirth(LocalDate.parse((String) userData.get("dateOfBirth")));
        return user;
    }
    
    public void saveProfile(UserProfile profile) {
        try {
            Map<String, String> profileData = new HashMap<>();
            profileData.put("weight", profile.getWeight() != null ? profile.getWeight().toString() : "0");
            profileData.put("height", profile.getHeight() != null ? profile.getHeight().toString() : "0");
            profileData.put("fitnessGoal", profile.getFitnessGoal() != null ? profile.getFitnessGoal() : "");
            profileData.put("weeklyGoal", profile.getWeeklyGoal() != null ? profile.getWeeklyGoal().toString() : "0");
            profileData.put("weekStartDate", profile.getWeekStartDate().toString());
            
            redisTemplate.opsForHash().putAll("userProfile:" + profile.getUserId(), profileData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public UserProfile findProfileById(String userId) {
        try {
            Map<Object, Object> profileData = redisTemplate.opsForHash().entries("userProfile:" + userId);
            
            UserProfile profile = new UserProfile();
            profile.setUserId(userId);
            
            if (!profileData.isEmpty()) {
                if (profileData.get("weight") != null) 
                    profile.setWeight(Double.parseDouble((String) profileData.get("weight")));
                if (profileData.get("height") != null)
                    profile.setHeight(Double.parseDouble((String) profileData.get("height")));
                if (profileData.get("fitnessGoal") != null)
                    profile.setFitnessGoal((String) profileData.get("fitnessGoal"));
                if (profileData.get("weeklyGoal") != null)
                    profile.setWeeklyGoal(Integer.parseInt((String) profileData.get("weeklyGoal")));
                if (profileData.get("weekStartDate") != null)
                    profile.setWeekStartDate(LocalDateTime.parse((String) profileData.get("weekStartDate")));
            }
            
            return profile;
        } catch (Exception e) {
            e.printStackTrace();
            return new UserProfile();
        }
    }
} 