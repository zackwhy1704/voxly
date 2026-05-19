package app.voxly.controller;

import app.voxly.model.dto.response.ScoreResponse;
import app.voxly.service.SpeechService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/speech")
@RequiredArgsConstructor
public class SpeechController {

    private final SpeechService speechService;

    @PostMapping("/analyse")
    public ResponseEntity<ScoreResponse> analyse(
            @RequestParam("audio") MultipartFile audio,
            @RequestParam(value = "sessionId", required = false) String sessionId) {
        return ResponseEntity.ok(speechService.analyse(audio, sessionId));
    }
}
