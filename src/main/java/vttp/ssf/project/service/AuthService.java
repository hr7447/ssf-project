package vttp.ssf.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp.ssf.project.model.User;
import vttp.ssf.project.repository.UserRepository;
import java.util.UUID;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    public void registerUser(User user) {
        user.setId(UUID.randomUUID().toString());
        userRepository.saveUser(user);
    }
    
    public User authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
} 