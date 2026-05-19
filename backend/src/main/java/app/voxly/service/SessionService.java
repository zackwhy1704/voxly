package app.voxly.service;

import app.voxly.exception.ResourceNotFoundException;
import app.voxly.model.dto.request.CompleteSessionRequest;
import app.voxly.model.dto.response.SessionResponse;
import app.voxly.model.entity.ScenarioEntity;
import app.voxly.model.entity.SessionEntity;
import app.voxly.model.entity.UserEntity;
import app.voxly.repository.ScenarioRepository;
import app.voxly.repository.SessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final ScenarioRepository scenarioRepository;
    private final UserService userService;
    private final ScoringService scoringService;
    private final ObjectMapper objectMapper;

    @Transactional
    public SessionResponse create(UUID scenarioId, UUID userId) {
        ScenarioEntity scenario = scenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Scenario not found: " + scenarioId));
        UserEntity user = userService.findById(userId);
        SessionEntity session = SessionEntity.builder()
                .user(user)
                .scenario(scenario)
                .startedAt(LocalDateTime.now())
                .build();
        return toResponse(sessionRepository.save(session));
    }

    public SessionResponse getById(UUID id) {
        return toResponse(findById(id));
    }

    @Transactional
    public SessionResponse complete(UUID sessionId, CompleteSessionRequest request) {
        SessionEntity session = findById(sessionId);
        session.setCompletedAt(LocalDateTime.now());
        session.setDurationSeconds(request.getDurationSeconds());
        session.setPronunciationScore(request.getPronunciationScore());
        session.setFluencyScore(request.getFluencyScore());
        session.setGrammarScore(request.getGrammarScore());
        session.setVocabularyScore(request.getVocabularyScore());
        int overall = scoringService.calculateOverallScore(
                request.getPronunciationScore(), request.getFluencyScore(),
                request.getGrammarScore(), request.getVocabularyScore());
        session.setOverallScore(overall);

        int baseXp = session.getScenario() != null ? session.getScenario().getXpReward() : 30;
        int xpEarned = (int) (baseXp * (overall / 100.0));
        session.setXpEarned(xpEarned);

        try {
            if (request.getAiInsights() != null) {
                session.setAiInsightsJson(objectMapper.writeValueAsString(request.getAiInsights()));
            }
        } catch (Exception e) {
            log.warn("Failed to serialize AI insights", e);
        }

        session = sessionRepository.save(session);
        userService.addXp(session.getUser().getId(), xpEarned);
        userService.updateStreak(session.getUser().getId());
        return toResponse(session);
    }

    public List<SessionResponse> getHistory(UUID userId) {
        return sessionRepository
                .findByUserIdAndCompletedAtIsNotNullOrderByCompletedAtDesc(userId, PageRequest.of(0, 20))
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public SessionEntity findById(UUID id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + id));
    }

    private SessionResponse toResponse(SessionEntity e) {
        List<String> insights = List.of();
        try {
            if (e.getAiInsightsJson() != null && !e.getAiInsightsJson().equals("[]")) {
                insights = objectMapper.readValue(e.getAiInsightsJson(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
            }
        } catch (Exception ex) {
            log.warn("Failed to deserialize AI insights", ex);
        }
        return SessionResponse.builder()
                .id(e.getId())
                .userId(e.getUser() != null ? e.getUser().getId() : null)
                .scenarioId(e.getScenario() != null ? e.getScenario().getId() : null)
                .scenarioTitle(e.getScenario() != null ? e.getScenario().getTitle() : null)
                .startedAt(e.getStartedAt())
                .completedAt(e.getCompletedAt())
                .durationSeconds(e.getDurationSeconds())
                .overallScore(e.getOverallScore())
                .pronunciationScore(e.getPronunciationScore())
                .fluencyScore(e.getFluencyScore())
                .grammarScore(e.getGrammarScore())
                .vocabularyScore(e.getVocabularyScore())
                .xpEarned(e.getXpEarned())
                .aiInsights(insights)
                .build();
    }
}
