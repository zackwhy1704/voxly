package app.voxly.service;

import app.voxly.exception.SpeechProcessingException;
import app.voxly.model.dto.response.HudUpdateResponse;
import app.voxly.model.dto.response.ScoreResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeechService {

    private final ScoringService scoringService;
    private final LlmService llmService;

    @Value("${voxly.whisper.api-url}")
    private String whisperApiUrl;

    public ScoreResponse analyse(MultipartFile audio, String sessionId) {
        try {
            WebClient client = WebClient.create(whisperApiUrl);

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("audio_file", audio.getResource());

            @SuppressWarnings("unchecked")
            Map<String, Object> whisperResponse = client.post()
                    .uri("/asr?task=transcribe&language=en&output=json")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(Duration.ofSeconds(60));

            String transcript = whisperResponse != null
                    ? (String) whisperResponse.get("text") : "";

            int grammar = scoringService.calculateGrammarScore(transcript);
            List<String> fillers = scoringService.detectFillerWords(transcript);
            int vocabulary = Math.max(0, 100 - fillers.size() * 10);
            int pronunciation = scoringService.calculatePronunciationScore(transcript, transcript);
            int fluency = 75;
            int overall = scoringService.calculateOverallScore(pronunciation, fluency, grammar, vocabulary);

            return ScoreResponse.builder()
                    .pronunciation(pronunciation)
                    .fluency(fluency)
                    .grammar(grammar)
                    .vocabulary(vocabulary)
                    .overall(overall)
                    .fillerWords(fillers)
                    .flaggedWords(List.of())
                    .xpEarned(overall / 10)
                    .isPersonalBest(false)
                    .build();
        } catch (Exception e) {
            log.error("Speech analysis failed", e);
            throw new SpeechProcessingException("Speech analysis failed", e);
        }
    }

    public HudUpdateResponse streamChunk(byte[] audioChunk, String sessionId) {
        return HudUpdateResponse.builder()
                .pace("Good")
                .fillerCount(0)
                .clarityScore(0.8f)
                .elapsedSeconds(0)
                .partialTranscript("")
                .build();
    }
}
