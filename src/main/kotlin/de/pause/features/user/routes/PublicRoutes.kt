package de.pause.features.user.routes

import de.pause.features.mail.routes.NoReplyEmailClient
import de.pause.features.user.data.dto.LoginRequest
import de.pause.features.user.data.dto.RegisterRequest
import de.pause.features.user.data.repo.UserRepository
import de.pause.plugins.createJWT
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userAuthentication(userRepository: UserRepository) {
    route("/user") {
        route("/login") {
            post {
                val loginRequest = call.receive<LoginRequest>()
                val user = userRepository.login(loginRequest)
                if (user != null) {
                    val token = createJWT(user)
                    call.respond(HttpStatusCode.OK, mapOf("accessToken" to token))
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }
        }
        route("/register") {
            post {
                val registerRequest = call.receive<RegisterRequest>()
                val success = userRepository.register(registerRequest)
                when {
                    success != null -> {
                        NoReplyEmailClient.sendVerificationMail(registerRequest.email, success.id.toString())
                        call.respond(HttpStatusCode.Created)
                    }

                    else -> call.respond(HttpStatusCode.UnprocessableEntity, "User already exists")
                }
            }
        }
    }
}