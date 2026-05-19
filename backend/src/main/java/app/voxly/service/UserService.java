package app.voxly.service;

import app.voxly.exception.ResourceNotFoundException;
import app.voxly.model.dto.request.UpdateProfileRequest;
import app.voxly.model.dto.response.UserResponse;
import app.voxly.model.entity.UserEntity;
import app.voxly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;

    public UserResponse getById(UUID id) {
        return toResponse(findById(id));
    }

    @Transactional
    public UserResponse update(UUID id, UpdateProfileRequest request) {
        UserEntity user = findById(id);
        if (request.getName() != null) user.setName(request.getName());
        if (request.getCountry() != null) user.setCountry(request.getCountry());
        if (request.getGoal() != null) user.setGoal(request.getGoal());
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void updateStreak(UUID userId) {
        UserEntity user = findById(userId);
        String key = "streak:" + userId;
        String lastActivity = redisTemplate.opsForValue().get(key);
        LocalDate today = LocalDate.now();

        if (lastActivity == null) {
            user.setStreakDays(1);
        } else {
            LocalDate last = LocalDate.parse(lastActivity);
            long diff = today.toEpochDay() - last.toEpochDay();
            if (diff == 1) user.setStreakDays(user.getStreakDays() + 1);
            else if (diff > 1) user.setStreakDays(1);
        }
        redisTemplate.opsForValue().set(key, today.toString(), Duration.ofDays(2));
        userRepository.save(user);
    }

    @Transactional
    public void addXp(UUID userId, int xp) {
        UserEntity user = findById(userId);
        user.setTotalXp(user.getTotalXp() + xp);
        user.setWeeklyXp(user.getWeeklyXp() + xp);
        userRepository.save(user);
    }

    public UserEntity findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    public UserResponse toResponse(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .country(user.getCountry())
                .goal(user.getGoal())
                .currentTier(user.getCurrentTier())
                .careerReadinessScore(user.getCareerReadinessScore())
                .streakDays(user.getStreakDays())
                .totalXp(user.getTotalXp())
                .weeklyXp(user.getWeeklyXp())
                .rank(user.getRank())
                .emailVerified(user.isEmailVerified())
                .build();
    }
}
