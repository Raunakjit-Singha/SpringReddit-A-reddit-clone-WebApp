package com.chriskocabas.redditclone.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

/**
 * Entity representing a user-submitted Post within a Subreddit.
 * This class maps to the main content table in the database and manages
 * relationships between users, subreddits, and the content itself.
 */
@Getter
@Setter
@Entity
@Builder            // Implements the Builder design pattern, which is highly valuable for writing clean, readable unit tests when mocking Post data.
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    
    /**
     * Enforces backend data validation before hitting the database.
     * Prevents the creation of empty or whitespace-only post titles.
     */
    @NotBlank(message = "Post name cannot be empty")
    private String postName;
    
    @Nullable
    private String url;
    
    /**
     * @Lob (Large Object) tells JPA to store this as a TEXT or LONGTEXT field in the database,
     * accommodating potentially massive text payloads without character limits.
     */
    @Nullable
    @Lob
    private String description;
    
    // Initializes new posts with a baseline score of 0
    private Integer voteCount = 0;
    
    /**
     * Foreign Key relationship to the User who authored the post.
     * FetchType.LAZY prevents N+1 query performance issues by only loading 
     * user details when explicitly requested by the application layer.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
    
    private Instant createdDate;
    
    /**
     * Foreign Key relationship linking the post to its parent Subreddit category.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id") // Note: The database column name maps to the parent Subreddit ID
    private Subreddit subreddit;
    
    private Boolean notificationStatus = true;
}
