package de.pause.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.pause.model.*
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
    userRepository: UserRepository,
    menurepo: MenuRepository,
) {


    val secret = appConfig.property("ktor.jwt.secret").getString()
    val issuer = appConfig.property("ktor.jwt.issuer").getString()
    val audience = appConfig.property("ktor.jwt.audience").getString()
    val tokenExpiration = 600L

    routing {
        route("/current_menu") {
            get {

                val menu = menurepo.getCurrentMenu()
                call.respond(menu)
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
        route("/register") {
            post {
                val loginRequest = call.receive<RegisterRequest>()
                val success = userRepository.register(loginRequest)
                when {
                    success -> call.respond(HttpStatusCode.Created)
                    else -> call.respond(HttpStatusCode.BadRequest)
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
            route("/newDish") {
                post {
                    val jwt = call.principal<JWTPrincipal>()
                    val role = UserRole.valueOf(jwt!!.payload.getClaim("role").asString())
                    if (role == UserRole.MODERATOR) {
                        val newDish = call.receive<Dish>()
                        dishRepository.addDish(newDish)
                        call.respond(HttpStatusCode.Created)
                    } else {
                        call.respond(HttpStatusCode.Forbidden)
                    }
                }
            }
            route("/dishes") {
                get {
                    val jwt = call.principal<JWTPrincipal>()
                    val role = UserRole.valueOf(jwt!!.payload.getClaim("role").asString())
                    if (role == UserRole.MODERATOR) {
                        call.respond(HttpStatusCode.OK, dishRepository.allDishes())
                    } else {
                        call.respond(HttpStatusCode.Forbidden)
                    }
                }
            }
        }
    }
}
