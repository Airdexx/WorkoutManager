package com.workout.workoutManager.dto.User.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
   @Size(min = 2, max = 45, message = "Nickname must be between 2 and 45 characters")
   private String nickname;

   @Size(min = 8, message = "Password must be at least 8 characters")
   private String password;

   @Pattern(regexp = "^\\d{11}$", message = "Phone number must be 11 digits")
   private String phoneNumber;

   @Min(value = 1, message = "Height must be greater than 0")
   @Max(value = 300, message = "Height must be less than 300")
   private Short height;

   @Min(value = 1, message = "Weight must be greater than 0")
   @Max(value = 500, message = "Weight must be less than 500")
   private Short weight;

   private String profileImage;
}