package com.learning.userservice.userservice.service;

import com.learning.userservice.userservice.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userAccount = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Trả về một đối tượng UserDetails, bao gồm username, password (đã mã hóa), và roles
        return User.withUsername(userAccount.getEmail())  // Sử dụng email làm username
                .password(userAccount.getPassword())  // Password đã mã hóa
                .roles(userAccount.getRole())  // Lấy role từ đối tượng UserAccount
                .build();
    }
}
