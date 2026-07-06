package com.GadgetGrove.user.service;


import com.GadgetGrove.user.mapper.UserMapper;
import com.GadgetGrove.user.model.User;
import com.GadgetGrove.user.dto.UserResponse;
import com.GadgetGrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public boolean updateUser(UUID id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    userRepository.save(existingUser);
            return true;
        }).orElse(false);
    }

}
