package com.learning.userservice.userservice.service;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.learning.userservice.userservice.dto.request.ProfileRequest;
import com.learning.userservice.userservice.model.UserAddress;
import com.learning.userservice.userservice.model.UserProfile;
import com.learning.userservice.userservice.repository.AccountRepository;
import com.learning.userservice.userservice.repository.ProfileRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;

    public UserProfile createProfile(String userId, ProfileRequest createProfileRequest) {
        var userAccountOpt = accountRepository.findById(userId);
        if (userAccountOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Account not found");
        }

        if (profileRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile already exists");
        }

        var profile = new UserProfile();
        profile.setUserId(userId);
        profile.setName(createProfileRequest.getName());
        profile.setAvatarUrl(createProfileRequest.getAvatarUrl());
        profile.setAddresses(createProfileRequest.getAddresses().stream()
                .map(addressRequest -> new UserAddress(profile, addressRequest.getStreet(), addressRequest.getCity(),
                        addressRequest.getState(), addressRequest.getPostalCode()))
                .collect(Collectors.toList()));
        profile.setPhoneNumber(createProfileRequest.getPhoneNumber());

        if (createProfileRequest.getGender() != null) {
            profile.setGender(createProfileRequest.getGender());
        }

        profile.setDateOfBirth(createProfileRequest.getDateOfBirth());
        profile.setCreatedAt(Instant.now());
        profile.setUpdatedAt(Instant.now());

        return profileRepository.save(profile);
    }

    public UserProfile getProfileByUserId(String userId) {
        return profileRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
    }

    public UserProfile updateProfile(String userId, ProfileRequest updatedProfile) {
        var existingProfile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
        existingProfile.setName(updatedProfile.getName());
        existingProfile.setAvatarUrl(updatedProfile.getAvatarUrl());
        existingProfile.setAddresses(updatedProfile.getAddresses().stream()
                .map(addressRequest -> new UserAddress(addressRequest.getStreet(), addressRequest.getCity(),
                        addressRequest.getState(), addressRequest.getPostalCode(), existingProfile))
                .collect(Collectors.toList()));
        existingProfile.setPhoneNumber(updatedProfile.getPhoneNumber());
        existingProfile.setGender(updatedProfile.getGender());
        existingProfile.setDateOfBirth(updatedProfile.getDateOfBirth());
        profileRepository.save(existingProfile);
        return existingProfile;
    }

    public void deleteProfile(String userId) {
        if (!profileRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }
        profileRepository.deleteById(userId);
    }
}
