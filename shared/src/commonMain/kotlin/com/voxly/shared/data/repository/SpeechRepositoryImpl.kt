package com.voxly.shared.data.repository

import com.voxly.shared.domain.model.HUDState
import com.voxly.shared.domain.model.ScoreBreakdown
import com.voxly.shared.domain.repository.SpeechRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SpeechRepositoryImpl : SpeechRepository {

    override suspend fun analyseAudio(audioData: ByteArray, sessionId: String): Result<ScoreBreakdown> =
        runCatching {
            // In production: POST to /api/v1/speech/analyse with multipart audio
            // Stub returns plausible scores
            ScoreBreakdown(
                pronunciation = (65..90).random(),
                fluency        = (55..85).random(),
                grammar        = (70..95).random(),
                vocabulary     = (60..88).random(),
            )
        }

    override fun streamHUD(sessionId: String): Flow<HUDState> = flow {
        // In production: WebSocket /speech/stream/{sessionId}
        var count = 0
        while (true) {
            kotlinx.coroutines.delay(2000)
            count++
            emit(HUDState(
                pace         = if (count % 3 == 0) "fast" else "good",
                fillerCount  = count / 5,
                clarity      = if (count % 4 == 0) "medium" else "high",
                elapsedSeconds = count * 2,
            ))
        }
    }
}
