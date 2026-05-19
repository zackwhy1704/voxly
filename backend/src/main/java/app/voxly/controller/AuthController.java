package app.voxly.controller;

import app.voxly.model.dto.request.*;
import app.voxly.model.dto.response.*;
import app.voxly.repository.UserRepository;
import app.voxly.security.JwtTokenProvider;
import app.voxly.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup/email")
    public ResponseEntity<AuthResponse> signUpEmail(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUpWithEmail(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/otp/send")
    public ResponseEntity<Void> sendOtp(@RequestParam String email) {
        authService.sendOtp(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<AuthResponse> verifyOtp(@Valid @RequestBody OtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(@RequestParam String email) {
        authService.resetPassword(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(authService.getCurrentUser(user.getId())))
                .orElse(ResponseEntity.notFound().build());
    }
}
