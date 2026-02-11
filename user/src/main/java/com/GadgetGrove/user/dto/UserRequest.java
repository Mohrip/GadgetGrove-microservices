package com.GadgetGrove.user.dto;

import com.GadgetGrove.user.dto.AddressDTO;
import lombok.Data;

import java.util.UUID;

@Data
public class UserRequest {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AddressDTO address;
}
