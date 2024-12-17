package com.workout.workoutManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Workout Manager API",
		version = "1.0",
		description = "운동 관리 시스템 API 문서"
))
@EnableJpaAuditing // Automatically Recording Entity's generation time
public class WorkoutManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(WorkoutManagerApplication.class, args);
	}
}