package com.GadgetGrove.user.dto;

import com.GadgetGrove.GadgetGrove.address.AddressDTO;
import com.GadgetGrove.user.enums.UserRole;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private AddressDTO address;
}
