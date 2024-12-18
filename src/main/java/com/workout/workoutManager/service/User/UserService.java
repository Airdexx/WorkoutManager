package com.workout.workoutManager.service.User;

import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.User.exception.UserDuplicateException;
import com.workout.workoutManager.domain.User.exception.UserNotFoundException;
import com.workout.workoutManager.domain.User.repository.UserRepository;
import com.workout.workoutManager.dto.User.request.UserCreateRequest;
import com.workout.workoutManager.dto.User.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Create new user
     */
    @Transactional
    public User createUser(UserCreateRequest request) {
        validateNewUser(request.getEmail(), request.getNickname(), request.getPhoneNumber());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .phoneNumber(request.getPhoneNumber())
                .birth(request.getBirth())
                .gender(request.getGender())
                .height(request.getHeight())
                .weight(request.getWeight())
                .build();

        return userRepository.save(user);
    }

    //Get User by email(gonna use in login)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get user by id
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Update user
     */
    @Transactional
    public User updateUser(Long id, UserUpdateRequest request) {
        User user = getUserById(id);

        if (request.getNickname() != null && !request.getNickname().equals(user.getNickname())) {
            validateNickname(request.getNickname());
            user.updateNickname(request.getNickname());
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            validatePhoneNumber(request.getPhoneNumber());
            user.updatePhoneNumber(request.getPhoneNumber());
        }

        if (request.getPassword() != null) {
            user.updatePassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getHeight() != null) {
            user.updateHeight(request.getHeight());
        }

        if (request.getWeight() != null) {
            user.updateWeight(request.getWeight());
        }

        if (request.getProfileImage() != null) {
            user.updateProfileImage(request.getProfileImage());
        }

        return user; // JPA의 더티체킹으로 인해 별도의 save 불필요
    }

    /**
     * Soft delete user
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.softDelete(id);
    }

    /**
     * Get all active users
     */
    public List<User> getAllActiveUsers() {
        return userRepository.findAllActive();
    }

    /**
     * Update last login time
     */
    @Transactional
    public void updateLastLoginTime(Long id) {
        User user = getUserById(id);
        user.updateLastLoginTime();
    }

    /**
     * Validate new user
     */
    private void validateNewUser(String email, String nickname, String phoneNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new UserDuplicateException("Email", email);
        }
        if (userRepository.existsByNickname(nickname)) {
            throw new UserDuplicateException("Nickname", nickname);
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserDuplicateException("PhoneNumber", phoneNumber);
        }
    }

    private void validateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new UserDuplicateException("Nickname", nickname);
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserDuplicateException("PhoneNumber", phoneNumber);
        }
    }
}