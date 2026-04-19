package com.chriskocabas.redditclone.controller;

import com.chriskocabas.redditclone.Exceptions.ValidationExceptions;
import com.chriskocabas.redditclone.dto.AuthenticationResponse;
import com.chriskocabas.redditclone.dto.LoginRequest;
import com.chriskocabas.redditclone.dto.RefreshTokenRequest;
import com.chriskocabas.redditclone.dto.RegisterRequest;
import com.chriskocabas.redditclone.repository.IUserRepository;
import com.chriskocabas.redditclone.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.OK;
import com.chriskocabas.redditclone.service.AuthService;

import java.util.Optional;

/**
 * REST Controller responsible for managing user identity and session security.
 * Exposes API endpoints for registration, email verification, login, and token management.
 */
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor // Lombok: Injects required dependencies (AuthService, Repositories) via constructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final IUserRepository userRepository;

    /**
     * Handles new user registration.
     * Implements strict payload validation and defensive checks for data uniqueness.
     * * @return HTTP 200 on success, HTTP 400 on bad payload, HTTP 409 on duplicate user/email.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {

        // Validates DTO constraints before processing business logic
        Optional <String> validationErrors = ValidationExceptions.processValidationErrors(bindingResult);
        if (validationErrors.isPresent()){
            return new ResponseEntity<>(validationErrors.get(), HttpStatus.BAD_REQUEST);
        }

        // Database checks to prevent duplicate accounts and maintain data integrity
        Boolean usernamePresent = userRepository.findByUsername(registerRequest.getUsername()).isPresent();
        Boolean emailPresent = userRepository.findByEmail(registerRequest.getEmail()).isPresent();
        if(usernamePresent || emailPresent){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    usernamePresent ? "Username " + registerRequest.getUsername() +" is already taken."
                            : registerRequest.getEmail() + " is taken.");
        }

        authService.register(registerRequest);
        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }

    /**
     * Verifies a user's account via a secure token sent to their email.
     * Uses HTTP 302 (FOUND) to seamlessly redirect the user back to the frontend SPA login page.
     */
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);

        // Redirects to the hosted Azure static web app frontend
        String frontendLoginUrl = "https://zealous-wave-027e5c910.3.azurestaticapps.net/#/login";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", frontendLoginUrl);
        headers.add("X-Redirection-Flag", "true");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * Authenticates a user and issues a JWT (JSON Web Token) for secure API access.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    /**
     * Issues a fresh JWT using a valid refresh token.
     * Allows persistent user sessions without requiring repeated credential entry.
     */
    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        System.out.println("Refresh token triggered" + refreshTokenRequest);
        return authService.refreshToken(refreshTokenRequest);
    }

    /**
     * Secures user logout by permanently invalidating and deleting their refresh token from the database.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }
}
