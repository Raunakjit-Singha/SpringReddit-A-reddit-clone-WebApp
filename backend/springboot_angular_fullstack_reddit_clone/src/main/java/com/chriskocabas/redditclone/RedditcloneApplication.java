package com.chriskocabas.redditclone;

import com.chriskocabas.redditclone.config.OpenAPIConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main entry point for the Spring Boot Reddit Clone application.
 * This class bootstraps the server and enables key architectural features 
 * such as asynchronous processing and automated task scheduling.
 */
@SpringBootApplication
@EnableAsync      // Allows the app to run background tasks (like sending user verification emails) without freezing the main thread.
@EnableScheduling // Enables the scheduling of automated periodic tasks within the application context.
@Import(OpenAPIConfiguration.class) // Imports the Swagger/OpenAPI configuration for auto-generating API documentation.
public class RedditcloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditcloneApplication.class, args);
	}

}
