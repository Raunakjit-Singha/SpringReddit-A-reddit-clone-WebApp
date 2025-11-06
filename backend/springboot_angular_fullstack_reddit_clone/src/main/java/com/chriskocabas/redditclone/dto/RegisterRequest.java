package com.chriskocabas.redditclone.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Email
    @NotEmpty(message = "Email is required")
    private String email;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;

    // Corrected @Size annotation
    @Size(min = 2, max = 50, message = "Firstname must be between 2 and 50 characters")
    @NotBlank(message = "Firstname is required")
    private String firstname;

    // Corrected @Size annotation
    @Size(min = 2, max = 50, message = "Lastname must be between 2 and 50 characters")
    @NotBlank(message = "Lastname is required")
    private String lastname;

}