package com.voxly.shared.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object VoxlyConfig {
    const val BASE_URL = "https://api.voxly.app"
    const val WS_URL   = "wss://api.voxly.app"
    const val API_V1   = "$BASE_URL/api/v1"
}

fun createHttpClient(tokenProvider: () -> String? = { null }): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; isLenient = true })
        }
        install(Logging) {
            level = LogLevel.BODY
            logger = Logger.DEFAULT
        }
        install(WebSockets)
        install(HttpTimeout) {
            requestTimeoutMillis  = 30_000
            connectTimeoutMillis  = 15_000
            socketTimeoutMillis   = 30_000
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
            tokenProvider()?.let { token ->
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }
}
