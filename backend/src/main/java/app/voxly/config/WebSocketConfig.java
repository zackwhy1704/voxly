package app.voxly.config;

import app.voxly.websocket.SessionWebSocketHandler;
import app.voxly.websocket.SpeechWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final SpeechWebSocketHandler speechWebSocketHandler;
    private final SessionWebSocketHandler sessionWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(speechWebSocketHandler, "/speech/stream/{sessionId}")
                .setAllowedOrigins("*");
        registry.addHandler(sessionWebSocketHandler, "/sessions/{id}/converse")
                .setAllowedOrigins("*");
    }
}
