package de.breigindustries.cs.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import de.breigindustries.cs.shared.models.User;
import de.breigindustries.cs.shared.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User createUser(String username) {
        return userRepo.save(new User(null, username, null, null, null));
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUser(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }
}
