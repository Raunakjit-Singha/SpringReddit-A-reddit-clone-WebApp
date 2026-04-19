package com.chriskocabas.redditclone.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Entity representing a Refresh Token in the database.
 * Refresh tokens are used to securely issue new JWTs (JSON Web Tokens) 
 * without requiring the user to constantly re-enter their login credentials.
 */
@Data               // Lombok: Auto-generates getters, setters, toString, and equals methods to reduce boilerplate
@AllArgsConstructor // Lombok: Auto-generates a constructor with all arguments
@NoArgsConstructor  // Lombok: Auto-generates an empty constructor (Required by JPA)
@Entity             // JPA: Marks this class as an entity mapped to a database table
@Table(name = "refresh_token") // Explicitly sets the table name in the PostgreSQL/MySQL database
public class RefreshToken {
    
    @Id // Denotes the primary key of the table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID for each new token
    private Long id;
    
    @Column(length = 600) // Explicitly increases column size to accommodate long encrypted token strings
    private String token;
    
    private Instant createdDate;
    
    private Instant expirationDate; // Tracked to determine if a user must completely log in again
    
    /**
     * Establishes a Many-to-One relationship with the User entity.
     * Using FetchType.LAZY optimizes database performance by only loading the 
     * associated User object into memory when it is explicitly accessed, rather than on every query.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
}
