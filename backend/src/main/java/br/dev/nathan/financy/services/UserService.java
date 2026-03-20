package br.dev.nathan.financy.services;

import br.dev.nathan.financy.dtos.response.UserResponse;
import br.dev.nathan.financy.entities.User;
import br.dev.nathan.financy.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void changeName(UUID userId, String newName) {

        User user = userRepository.findById(userId).orElseThrow();

        user.setName(newName);

        userRepository.save(user);
    }

    public UserResponse getUserInfo(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return new UserResponse(user.getName(), user.getEmail());
    }
}
