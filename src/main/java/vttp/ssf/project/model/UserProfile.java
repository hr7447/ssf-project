package vttp.ssf.project.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;

public class UserProfile {
    private String userId;
    
    @Min(value = 30, message = "Weight must be at least 30kg")
    @Max(value = 300, message = "Weight must be less than 300kg")
    private Double weight;
    
    @Min(value = 100, message = "Height must be at least 100cm")
    @Max(value = 250, message = "Height must be less than 250cm")
    private Double height;
    
    private String fitnessGoal;
    private Integer weeklyGoal;
    
    private LocalDateTime weekStartDate;
    
    public Double getBMI() {
        if (height == null || weight == null || height == 0) return 0.0;
        return weight / ((height/100) * (height/100));
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    
    public String getFitnessGoal() { return fitnessGoal; }
    public void setFitnessGoal(String fitnessGoal) { this.fitnessGoal = fitnessGoal; }
    
    public Integer getWeeklyGoal() { return weeklyGoal; }
    public void setWeeklyGoal(Integer weeklyGoal) { this.weeklyGoal = weeklyGoal; }

    public LocalDateTime getWeekStartDate() { return weekStartDate; }
    public void setWeekStartDate(LocalDateTime weekStartDate) { this.weekStartDate = weekStartDate; }

    public UserProfile() {
        this.weekStartDate = LocalDateTime.now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .withHour(0).withMinute(0);
    }
} 