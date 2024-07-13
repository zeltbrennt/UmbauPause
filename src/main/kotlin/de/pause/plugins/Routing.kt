package de.pause.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.pause.model.DishRepository
import de.pause.model.LoginRequest
import de.pause.model.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant
import java.util.*


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
                    val token = JWT.create()
                        .withJWTId(UUID.randomUUID().toString())
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withIssuedAt(Instant.now())
                        .withExpiresAt(Instant.now().plusSeconds(tokenExpiration))
                        .withClaim("email", user.email)
                        .withClaim("role", user.role.name)
                        .sign(Algorithm.HMAC256(secret))
                    call.respond(HttpStatusCode.OK, hashMapOf("accessToken" to token))
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }
        }
        authenticate("jwt-auth") {
            route("/logout") {
                post {
                    val jwt = call.principal<JWTPrincipal>()
                    val user = jwt!!.payload.getClaim("email").asString()
                    val success = userRepository.logout(user)
                    when {
                        success -> call.respond(HttpStatusCode.OK)
                        else -> call.respond(HttpStatusCode.BadRequest)
                    }
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
