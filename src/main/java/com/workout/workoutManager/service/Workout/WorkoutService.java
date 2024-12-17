package com.workout.workoutManager.service.Workout;

import com.workout.workoutManager.domain.User.entity.User;
import com.workout.workoutManager.domain.User.repository.UserRepository;
import com.workout.workoutManager.domain.Workout.WorkoutDetail;
import com.workout.workoutManager.domain.Workout.entity.WorkoutHistory;
import com.workout.workoutManager.domain.Workout.entity.WorkoutSet;
import com.workout.workoutManager.domain.Workout.repository.WorkoutHistoryRepository;
import com.workout.workoutManager.domain.Workout.repository.WorkoutSetRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class WorkoutService {
    private static final int DAILY_ATTENDANCE_POINTS = 300;

    private final WorkoutHistoryRepository workoutHistoryRepository;
    private final UserRepository userRepository;
    private final WorkoutSetRepository workoutSetRepository;

    public WorkoutService(
            WorkoutHistoryRepository workoutHistoryRepository,
            UserRepository userRepository,
            WorkoutSetRepository workoutSetRepository
    ) {
        this.workoutHistoryRepository = workoutHistoryRepository;
        this.userRepository = userRepository;
        this.workoutSetRepository = workoutSetRepository;
    }

    @Transactional
    public void recordWorkout(Long userId, WorkoutHistory workoutHistory, List<WorkoutSet> sets) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        recordWorkout(user, workoutHistory, sets);
    }

    @Transactional
    public void recordWorkout(User user, WorkoutHistory workoutHistory, List<WorkoutSet> sets) {
        workoutHistory.setUser(user);
        workoutHistory.setWorkoutDate(LocalDateTime.now());

        // 운동 기록 저장
        WorkoutHistory savedHistory = workoutHistoryRepository.save(workoutHistory);

        // 세트 정보 저장
        for (int i = 0; i < sets.size(); i++) {
            WorkoutSet set = sets.get(i);
            set.setWorkoutHistory(savedHistory);
            set.setSetNumber(i + 1);
            workoutSetRepository.save(set);
        }

        // 오늘 첫 운동인지 확인하고 포인트 부여
        checkAndUpdateDailyStatus(user);
    }

    private void checkAndUpdateDailyStatus(User user) {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        // 오늘 첫 운동인 경우에만 포인트와 스트릭 업데이트
        if (!workoutHistoryRepository.existsByUserAndWorkoutDateBetween(user, startOfDay, endOfDay)) {
            // 기본 출석 포인트 부여
            user.addPoint(DAILY_ATTENDANCE_POINTS);

            // 스트릭 업데이트 (스트릭에 따른 추가 포인트는 incrementStreak 내부에서 처리됨)
            updateStreak(user);

            userRepository.save(user);
        }
    }

    private void updateStreak(User user) {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        boolean workedOutYesterday = workoutHistoryRepository.existsByUserAndWorkoutDateBetween(
                user,
                yesterday.with(LocalTime.MIN),
                yesterday.with(LocalTime.MAX)
        );

        if (workedOutYesterday) {
            user.incrementStreak();  // 내부적으로 스트릭에 따른 추가 포인트도 부여됨
        } else {
            user.resetStreak();  // 스트릭 0으로 리셋
        }
    }

    // 매일 자정에 실행될 스케줄러 메소드
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void resetMissedStreaks() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime yesterdayStart = yesterday.with(LocalTime.MIN);
        LocalDateTime yesterdayEnd = yesterday.with(LocalTime.MAX);

        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            boolean workedOutYesterday = workoutHistoryRepository.existsByUserAndWorkoutDateBetween(
                    user, yesterdayStart, yesterdayEnd
            );

            if (!workedOutYesterday && user.getStreak() > 0) {
                user.resetStreak();
                userRepository.save(user);
            }
        }
    }

    // 사용자의 운동 기록 조회 (ID 기반)
    public List<WorkoutHistory> getUserWorkoutHistory(Long userId) {
        return workoutHistoryRepository.findByUserIdOrderByWorkoutDateDesc(userId);
    }

    // 사용자의 운동 기록 조회 (엔티티 기반)
    public List<WorkoutHistory> getUserWorkoutHistory(User user) {
        return workoutHistoryRepository.findByUserOrderByWorkoutDateDesc(user);
    }

    // 특정 운동의 세트 정보 조회
    public List<WorkoutSet> getWorkoutSets(Long workoutHistoryId) {
        return workoutSetRepository.findByWorkoutHistoryIdOrderBySetNumberAsc(workoutHistoryId);
    }

    // 특정 부위의 운동 기록 조회 (ID 기반)
    public List<WorkoutHistory> getWorkoutsByBodyPart(Long userId, String bodyPart) {
        return workoutHistoryRepository.findByUserIdAndWorkoutType_BodyPart(userId, bodyPart);
    }

    // 특정 부위의 운동 기록 조회 (엔티티 기반)
    public List<WorkoutHistory> getWorkoutsByBodyPart(User user, String bodyPart) {
        return workoutHistoryRepository.findByUserAndWorkoutType_BodyPart(user, bodyPart);
    }
}