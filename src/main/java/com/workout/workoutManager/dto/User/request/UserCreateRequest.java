package com.workout.workoutManager.dto.User.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
   @NotBlank(message = "Email is required")
   @Email(message = "Invalid email format")
   private String email;

   @NotBlank(message = "Password is required")
   @Size(min = 8, message = "Password must be at least 8 characters")
   private String password;

   @NotBlank(message = "Nickname is required")
   @Size(min = 2, max = 45, message = "Nickname must be between 2 and 45 characters")
   private String nickname;

   @NotBlank(message = "Phone number is required")
   @Pattern(regexp = "^\\d{11}$", message = "Phone number must be 11 digits")
   private String phoneNumber;

   @NotNull(message = "Birth date is required")
   @Past(message = "Birth date must be in the past")
   private LocalDate birth;

   @NotNull(message = "Gender is required")
   private Boolean gender;  // false: FEMALE, true: MALE

   @NotNull(message = "Height is required")
   @Min(value = 1, message = "Height must be greater than 0")
   @Max(value = 300, message = "Height must be less than 300")
   private Short height;

   @NotNull(message = "Weight is required")
   @Min(value = 1, message = "Weight must be greater than 0")
   @Max(value = 500, message = "Weight must be less than 500")
   private Short weight;

   private String profileImage;
}