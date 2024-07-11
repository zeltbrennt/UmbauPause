package de.pause.plugins

import de.pause.model.DishRepository
import de.pause.model.LoginRequest
import de.pause.model.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    appConfig: HoconApplicationConfig,
    dishRepository: DishRepository,
    userRepository: UserRepository
) {


    val secret = appConfig.property("ktor.jwt.secret").getString()
    val issuer = appConfig.property("ktor.jwt.issuer").getString()
    val audience = appConfig.property("ktor.jwt.audience").getString()
    val tokenExpiration = 600L

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
