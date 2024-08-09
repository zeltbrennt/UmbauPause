package de.pause.plugins

import io.ktor.server.application.*
import io.ktor.server.websocket.*
import java.time.Duration

fun Application.configureWebsockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofMinutes(1)
        timeout = Duration.ofMinutes(1)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}