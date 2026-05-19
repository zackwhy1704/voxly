package app.voxly.websocket;

import app.voxly.model.dto.response.HudUpdateResponse;
import app.voxly.service.SpeechService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpeechWebSocketHandler extends AbstractWebSocketHandler {

    private final SpeechService speechService;
    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = extractSessionId(session);
        sessions.put(sessionId, session);
        log.info("Speech WebSocket connected: {}", sessionId);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        String sessionId = extractSessionId(session);
        HudUpdateResponse hud = speechService.streamChunk(message.getPayload().array(), sessionId);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(hud)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(extractSessionId(session));
    }

    private String extractSessionId(WebSocketSession session) {
        String path = session.getUri().getPath();
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }
}
