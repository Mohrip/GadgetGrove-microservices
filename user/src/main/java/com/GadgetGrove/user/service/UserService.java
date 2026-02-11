package com.GadgetGrove.user.service;


import com.GadgetGrove.user.dto.AddressDTO;
import com.GadgetGrove.user.model.User;
import com.GadgetGrove.user.dto.UserResponse;
import com.GadgetGrove.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private List<User> userList = new ArrayList<>();
    private Long nextId = 1L;

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .toList();

    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public void addUser( User user) {
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

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRole(user.getRole());

        if(user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipCode(user.getAddress().getZipCode());
            userResponse.setAddress(addressDTO);
        }
        return userResponse;
    }

}
