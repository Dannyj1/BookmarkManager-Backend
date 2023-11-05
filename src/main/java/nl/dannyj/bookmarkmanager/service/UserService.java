package nl.dannyj.bookmarkmanager.service;

import nl.dannyj.bookmarkmanager.model.UserModel;
import nl.dannyj.bookmarkmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserModel> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
