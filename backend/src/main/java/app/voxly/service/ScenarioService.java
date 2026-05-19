package app.voxly.service;

import app.voxly.exception.ResourceNotFoundException;
import app.voxly.model.dto.response.ScenarioResponse;
import app.voxly.model.entity.ScenarioEntity;
import app.voxly.model.enums.DifficultyTier;
import app.voxly.model.enums.ScenarioCategory;
import app.voxly.repository.ScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;

    public List<ScenarioResponse> getAll(DifficultyTier tier, ScenarioCategory category) {
        List<ScenarioEntity> scenarios;
        if (tier != null && category != null) {
            scenarios = scenarioRepository.findByTierAndCategory(tier, category);
        } else if (tier != null) {
            scenarios = scenarioRepository.findByTier(tier);
        } else if (category != null) {
            scenarios = scenarioRepository.findByCategory(category);
        } else {
            scenarios = scenarioRepository.findAll();
        }
        return scenarios.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ScenarioResponse> getRecommended(UUID userId) {
        return scenarioRepository.findByIsLockedFalse().stream()
                .limit(5)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ScenarioResponse getById(UUID id) {
        return toResponse(scenarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scenario not found: " + id)));
    }

    private ScenarioResponse toResponse(ScenarioEntity e) {
        return ScenarioResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .subtitle(e.getSubtitle())
                .tier(e.getTier())
                .category(e.getCategory())
                .description(e.getDescription())
                .durationMinutes(e.getDurationMinutes())
                .accentColorHex(e.getAccentColorHex())
                .xpReward(e.getXpReward())
                .isLocked(e.isLocked())
                .skillsTested(e.getSkillsTested())
                .build();
    }
}
