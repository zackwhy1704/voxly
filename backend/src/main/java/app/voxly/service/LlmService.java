package app.voxly.service;

import app.voxly.exception.ResourceNotFoundException;
import app.voxly.model.entity.SessionEntity;
import app.voxly.repository.SessionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LlmService {

    private final SessionRepository sessionRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${voxly.anthropic.api-key}")
    private String anthropicApiKey;

    @Value("${voxly.anthropic.model}")
    private String model;

    @Value("${voxly.anthropic.base-url}")
    private String baseUrl;

    private WebClient webClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-api-key", anthropicApiKey)
                .defaultHeader("anthropic-version", "2023-06-01")
                .defaultHeader("content-type", "application/json")
                .build();
    }

    public List<String> generateFeedback(UUID sessionId) {
        SessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));

        String prompt = buildFeedbackPrompt(session);
        try {
            Map<String, Object> requestBody = Map.of(
                "model", model,
                "max_tokens", 512,
                "system", FEEDBACK_SYSTEM,
                "messages", List.of(Map.of("role", "user", "content", prompt))
            );

            Map<String, Object> response = webClient().post()
                    .uri("/messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(Duration.ofSeconds(30));

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> content = (List<Map<String, Object>>) response.get("content");
            String raw = (String) content.get(0).get("text");
            return objectMapper.readValue(raw.trim(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.error("LLM feedback generation failed", e);
            return fallbackInsights(session);
        }
    }

    public String conductInterview(UUID sessionId, String userMessage) {
        SessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));

        String redisKey = "session:" + sessionId + ":messages";
        String historyJson = redisTemplate.opsForValue().get(redisKey);
        List<Map<String, String>> history = new ArrayList<>();

        try {
            if (historyJson != null) {
                history = objectMapper.readValue(historyJson, new TypeReference<>() {});
            }
        } catch (Exception e) {
            log.warn("Failed to parse conversation history", e);
        }

        if (userMessage != null && !userMessage.isBlank()) {
            history.add(Map.of("role", "user", "content", userMessage));
        } else if (history.isEmpty()) {
            history.add(Map.of("role", "user", "content", "Please begin the interview."));
        }

        String tier = session.getScenario() != null
                ? session.getScenario().getTier().name() : "GOLD";
        String scenarioTitle = session.getScenario() != null
                ? session.getScenario().getTitle() : "Professional Interview";
        String systemPrompt = buildInterviewerSystem(scenarioTitle, tier);

        try {
            Map<String, Object> requestBody = Map.of(
                "model", model,
                "max_tokens", 256,
                "system", systemPrompt,
                "messages", history
            );

            Map<String, Object> response = webClient().post()
                    .uri("/messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(Duration.ofSeconds(30));

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> content = (List<Map<String, Object>>) response.get("content");
            String aiResponse = (String) content.get(0).get("text");

            history.add(Map.of("role", "assistant", "content", aiResponse));
            redisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(history), Duration.ofHours(2));

            return aiResponse;
        } catch (Exception e) {
            log.error("LLM interview generation failed", e);
            return "Could you walk me through a recent professional achievement and how it relates to this role?";
        }
    }

    private String buildFeedbackPrompt(SessionEntity session) {
        return String.format(
            "Scenario: %s\nScores: Pronunciation %d%%, Fluency %d%%, Grammar %d%%, Vocabulary %d%%\n" +
            "Provide 4 coaching insights as a JSON array of strings.",
            session.getScenario() != null ? session.getScenario().getTitle() : "Interview",
            session.getPronunciationScore(), session.getFluencyScore(),
            session.getGrammarScore(), session.getVocabularyScore()
        );
    }

    private String buildInterviewerSystem(String scenario, String tier) {
        String tone = switch (tier) {
            case "SILVER" -> "collegial and encouraging";
            case "PLATINUM" -> "demanding and high-pressure";
            default -> "formal and business-like";
        };
        return String.format(
            "You are a professional interviewer at a Singapore MNC. " +
            "Conduct a realistic job interview based on scenario: %s. " +
            "Your tone is %s. Ask one question at a time. " +
            "Ground questions in Singapore workplace culture. Do not break character.",
            scenario, tone
        );
    }

    private List<String> fallbackInsights(SessionEntity session) {
        return List.of(
            "✓ You completed the session — consistency is key to improvement.",
            "⚠️ Focus on reducing filler words like 'um' and 'uh' for a more professional delivery.",
            "💡 Practice the STAR method for answering behavioural questions concisely.",
            "🎯 Aim to maintain 110–150 wpm speaking rate to optimise comprehension."
        );
    }

    private static final String FEEDBACK_SYSTEM = """
        You are an expert English speech coach for Singapore professionals.
        Analyse the session scores and return exactly 4 coaching insights.
        Each insight starts with: ✓ (strength), ⚠️ (issue), 💡 (tip), or 🎯 (stretch goal).
        Be specific, actionable. Return as a JSON array of 4 strings only.
        """;
}
