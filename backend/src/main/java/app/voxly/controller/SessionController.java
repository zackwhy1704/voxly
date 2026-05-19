package app.voxly.controller;

import app.voxly.model.dto.request.CompleteSessionRequest;
import app.voxly.model.dto.response.SessionResponse;
import app.voxly.repository.UserRepository;
import app.voxly.service.LlmService;
import app.voxly.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final LlmService llmService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<SessionResponse> create(
            @RequestParam UUID scenarioId,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = userRepository.findByEmail(userDetails.getUsername())
                .map(u -> u.getId())
                .orElseThrow();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.create(scenarioId, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(sessionService.getById(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<SessionResponse> complete(@PathVariable UUID id,
                                                    @Valid @RequestBody CompleteSessionRequest request) {
        return ResponseEntity.ok(sessionService.complete(id, request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SessionResponse>> getHistory(@PathVariable UUID userId) {
        return ResponseEntity.ok(sessionService.getHistory(userId));
    }

    @PostMapping("/{id}/feedback")
    public ResponseEntity<List<String>> generateFeedback(@PathVariable UUID id) {
        return ResponseEntity.ok(llmService.generateFeedback(id));
    }
}
