package de.pause.features.app.routes

import de.pause.features.app.data.AppRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.sendAppFeedback(appRepository: AppRepository) {
    post("/feedback/{author}") {
        val author = call.parameters["author"].toString()
        val feedback = call.receiveText()
        appRepository.submitFeedback(author, feedback)
        call.respond(HttpStatusCode.OK)
    }
}