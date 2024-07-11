package de.pause.plugins

import de.pause.model.DishRepository
import de.pause.model.LoginRequest
import de.pause.model.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting(dishRepository: DishRepository, userRepository: UserRepository) {

    routing {
        route("/weekly") {
            get {

                val articles = dishRepository.getAvailableDishes().sortedBy { it.order }
                call.respond(
                    articles
                )
            }
        }
        route("/login") {
            post {
                val loginRequest = call.receive<LoginRequest>()
                val user = userRepository.login(loginRequest)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }
        }
        authenticate("basic-auth") {
            route("/newMenu") {
                get {
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
