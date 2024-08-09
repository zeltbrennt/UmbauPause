package de.pause.features.user.routes

import de.pause.features.user.data.dto.LoginRequest
import de.pause.features.user.data.dto.RegisterRequest
import de.pause.features.user.data.repo.UserRepository
import de.pause.plugins.createJWT
import de.pause.util.Constraints
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
                val loginRequest = call.receive<RegisterRequest>()
                //TODO: Add validation
                if (loginRequest.email.isBlank() || loginRequest.password.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Email or password is missing")
                    return@post
                }
                if (loginRequest.email.endsWith(Constraints.VALID_USER_EMAIL_SUFFIX).not()) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid email suffix")
                    return@post
                }
                if (Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")
                        .matches(loginRequest.password).not()
                ) {
                    call.respond(HttpStatusCode.BadRequest, "Password not complex enough")
                    return@post
                }
                val success = userRepository.register(loginRequest)
                when {
                    success -> call.respond(HttpStatusCode.Created)
                    else -> call.respond(HttpStatusCode.UnprocessableEntity, "User already exists")
                }
            }
        }
    }
}