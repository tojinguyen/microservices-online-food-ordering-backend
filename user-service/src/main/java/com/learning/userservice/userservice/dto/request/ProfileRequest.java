package com.learning.userservice.userservice.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String userId;
    private String name;
    private String avatarUrl;
    private List<AddressRequest> addresses;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
}
