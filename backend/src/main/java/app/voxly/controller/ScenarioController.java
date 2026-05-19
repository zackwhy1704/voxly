package app.voxly.controller;

import app.voxly.model.dto.response.ScenarioResponse;
import app.voxly.model.enums.DifficultyTier;
import app.voxly.model.enums.ScenarioCategory;
import app.voxly.repository.UserRepository;
import app.voxly.security.JwtTokenProvider;
import app.voxly.service.ScenarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/scenarios")
@RequiredArgsConstructor
public class ScenarioController {

    private final ScenarioService scenarioService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ScenarioResponse>> getAll(
            @RequestParam(required = false) DifficultyTier tier,
            @RequestParam(required = false) ScenarioCategory category) {
        return ResponseEntity.ok(scenarioService.getAll(tier, category));
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<ScenarioResponse>> getRecommended(
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = userRepository.findByEmail(userDetails.getUsername())
                .map(u -> u.getId()).orElse(null);
        return ResponseEntity.ok(scenarioService.getRecommended(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScenarioResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(scenarioService.getById(id));
    }
}
