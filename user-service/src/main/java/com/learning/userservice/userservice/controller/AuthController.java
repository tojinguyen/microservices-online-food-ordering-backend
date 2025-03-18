package com.learning.userservice.userservice.controller;

import com.learning.userservice.userservice.dto.request.*;
import com.learning.userservice.userservice.dto.response.*;
import com.learning.userservice.userservice.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@Slf4j
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    //Region:  Register
    @PostMapping("/send-register-code")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<SendRegisterVerifyCodeResponse>> sendVerificationCode(@RequestBody RegisterRequest registerRequest) {
        try {
            authService.sendRegisterVerificationCode(registerRequest.getEmail());
            return ResponseEntity.ok(ApiResponse.<SendRegisterVerifyCodeResponse>builder()
                    .success(true)
                    .message("Verification code sent successfully.")
                    .data(new SendRegisterVerifyCodeResponse("Verification code sent to " + registerRequest.getEmail()))
                    .build());
        } catch (Exception e) {
            log.info("Failed to send verification code: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.<SendRegisterVerifyCodeResponse>builder()
                    .success(false)
                    .message("Failed to send verification code.")
                    .data(new SendRegisterVerifyCodeResponse(e.getMessage()))
                    .build());
        }
    }

    @PostMapping("/verify-register-code")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> verify(@RequestBody VerifyRegisterCodeRequest verifyRegisterCodeRequest) {
        return ResponseEntity.ok(authService.verifyAndCreateUser(verifyRegisterCodeRequest));
    }
    //EndRegion

    //Region:  Login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    //EndRegion

    //Region:  Reset Password
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<ForgotPasswordResponse>> forgotPassword(@RequestBody ForgotPasswordRequest email) {
        try {
            authService.sendResetPasswordVerificationCode(email.getEmail());
            return ResponseEntity.ok(ApiResponse.<ForgotPasswordResponse>builder()
                    .success(true)
                    .message("Verification code sent successfully.")
                    .data(new ForgotPasswordResponse("Verification code sent to " + email.getEmail()))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<ForgotPasswordResponse>builder()
                    .success(false)
                    .message("Error when send verify code")
                    .data(new ForgotPasswordResponse("Error when send verify code " + e.getMessage()))
                    .build());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<ResetPasswordResponse>> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            var response = authService.resetPassword(resetPasswordRequest);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(ApiResponse.<ResetPasswordResponse>builder()
                    .success(false)
                    .message(e.getReason())  // Lấy thông báo từ ResponseStatusException
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<ResetPasswordResponse>builder()
                    .success(false)
                    .message("An unexpected error occurred")
                    .build());
        }
    }
    //EndRegion

    //Region:  Logout
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(@RequestHeader("Authorization") String token) {
        try {
            authService.logout(token);
            return ResponseEntity.ok(ApiResponse.<LogoutResponse>builder()
                    .success(true)
                    .message("Logout successfully.")
                    .data(new LogoutResponse("Logout successfully."))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<LogoutResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(new LogoutResponse(e.getMessage()))
                    .build());
        }
    }
    //EndRegion

    //Region: Refresh Token
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest.getRefreshToken()));
    }
    //EndRegion

    @PostMapping("test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test");
    }
}
