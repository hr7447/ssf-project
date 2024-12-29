package vttp.ssf.project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExternalApiService {
    
    @Value("${nutritionix.app.id}")
    private String appId;
    
    @Value("${nutritionix.app.key}")
    private String appKey;
    
    private final String NUTRITIONIX_EXERCISE_URL = "https://trackapi.nutritionix.com/v2/natural/exercise";
    
    public Integer calculateCalories(String exercise, int duration) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-app-id", appId);
        headers.set("x-app-key", appKey);
        
        String body = String.format("{\"query\":\"%s for %d minutes\"}", exercise, duration);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                NUTRITIONIX_EXERCISE_URL, 
                request, 
                String.class
            );
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode exercises = root.get("exercises");
            
            if (exercises.isArray() && exercises.size() > 0) {
                return exercises.get(0).get("nf_calories").asInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return duration * 7;
    }
} 