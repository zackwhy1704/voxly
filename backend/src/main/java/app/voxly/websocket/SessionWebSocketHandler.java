package app.voxly.websocket;

import app.voxly.service.LlmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionWebSocketHandler extends AbstractWebSocketHandler {

    private final LlmService llmService;
    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = extractSessionId(session);
        sessions.put(sessionId, session);
        log.info("Session WebSocket connected: {}", sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = extractSessionId(session);
        String userTranscript = message.getPayload();
        String aiResponse = llmService.conductInterview(UUID.fromString(sessionId), userTranscript);
        Map<String, String> response = Map.of("text", aiResponse);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(extractSessionId(session));
    }

    private String extractSessionId(WebSocketSession session) {
        String path = session.getUri().getPath();
        String[] parts = path.split("/");
        return parts[parts.length - 2];
    }
}
