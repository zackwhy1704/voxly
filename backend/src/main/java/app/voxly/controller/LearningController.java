package app.voxly.controller;

import app.voxly.model.dto.response.ChallengeResponse;
import app.voxly.model.dto.response.LessonResponse;
import app.voxly.repository.UserRepository;
import app.voxly.service.LearningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/learning")
@RequiredArgsConstructor
public class LearningController {

    private final LearningService learningService;
    private final UserRepository userRepository;

    private UUID getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .map(u -> u.getId()).orElseThrow();
    }

    @GetMapping("/path")
    public ResponseEntity<List<LessonResponse>> getPath(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(learningService.getPath(getUserId(userDetails)));
    }

    @PostMapping("/lessons/{id}/complete")
    public ResponseEntity<LessonResponse> completeLesson(@PathVariable UUID id,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(learningService.completeLesson(id, getUserId(userDetails)));
    }

    @GetMapping("/challenges/daily")
    public ResponseEntity<List<ChallengeResponse>> getDailyChallenges(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(learningService.getDailyChallenges(getUserId(userDetails)));
    }

    @PostMapping("/challenges/{id}/complete")
    public ResponseEntity<ChallengeResponse> completeChallenge(@PathVariable UUID id,
                                                                @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(learningService.completeChallenge(id, getUserId(userDetails)));
    }
}
