package app.voxly.service;

import app.voxly.exception.AuthException;
import app.voxly.exception.ResourceNotFoundException;
import app.voxly.model.dto.request.LoginRequest;
import app.voxly.model.dto.request.OtpRequest;
import app.voxly.model.dto.request.SignUpRequest;
import app.voxly.model.dto.response.AuthResponse;
import app.voxly.model.dto.response.UserResponse;
import app.voxly.model.entity.UserEntity;
import app.voxly.repository.UserRepository;
import app.voxly.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;
    private final UserService userService;
    private final JavaMailSender mailSender;
    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public AuthResponse signUpWithEmail(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email already registered");
        }
        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .country(request.getCountry())
                .goal(request.getGoal())
                .build();
        user = userRepository.save(user);
        String token = jwtTokenProvider.generateTokenForUser(user.getId(), user.getEmail());
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(userService.toResponse(user))
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthException("Invalid credentials");
        }
        String token = jwtTokenProvider.generateTokenForUser(user.getId(), user.getEmail());
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(userService.toResponse(user))
                .build();
    }

    public void sendOtp(String email) {
        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        redisTemplate.opsForValue().set("otp:" + email, otp, Duration.ofSeconds(300));
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Your Voxly verification code");
            msg.setText("Your OTP is: " + otp + "\nValid for 5 minutes.");
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Failed to send OTP email to {}: {}", email, e.getMessage());
        }
    }

    @Transactional
    public AuthResponse verifyOtp(OtpRequest request) {
        String stored = redisTemplate.opsForValue().get("otp:" + request.getEmail());
        if (stored == null || !stored.equals(request.getCode())) {
            throw new AuthException("Invalid or expired OTP");
        }
        redisTemplate.delete("otp:" + request.getEmail());
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setEmailVerified(true);
        user = userRepository.save(user);
        String token = jwtTokenProvider.generateTokenForUser(user.getId(), user.getEmail());
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(userService.toResponse(user))
                .build();
    }

    public void resetPassword(String email) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("reset:" + email, token, Duration.ofSeconds(3600));
        log.info("Password reset token for {}: {}", email, token);
    }

    public UserResponse getCurrentUser(UUID userId) {
        return userService.getById(userId);
    }
}
