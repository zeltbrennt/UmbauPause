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
import org.joda.time.format.DateTimeFormat
import java.time.Instant
import java.util.*


fun Application.configureRouting(
    appConfig: HoconApplicationConfig,
    dishRepository: DishRepository,
    userRepository: UserRepository,
    menuRepository: MenuRepository,
) {


    val secret = appConfig.property("ktor.jwt.secret").getString()
    val issuer = appConfig.property("ktor.jwt.issuer").getString()
    val audience = appConfig.property("ktor.jwt.audience").getString()
    val tokenExpiration = 600L

    routing {
        route("/menu") {
            get {
                val from = call.request.queryParameters["from"]
                val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
                val day = try {
                    formatter.parseDateTime(from)
                } catch (e: IllegalArgumentException) {
                    return@get call.respond(HttpStatusCode.BadRequest, "Invalid date format")
                } catch (e: NullPointerException) {
                    return@get call.respond(HttpStatusCode.BadRequest, "Missing parameter 'from'")
                }

                val menu = menuRepository.getScheduledMenuFrom(day)
                call.respond(menu ?: HttpStatusCode.NotFound)
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
                        .withClaim("uid", user.id)
                        .withClaim("roles", user.roles)
                        .sign(Algorithm.HMAC256(secret))
                    call.respond(HttpStatusCode.OK, mapOf("accessToken" to token))
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
            route("/order") {
                post {
                    val jwt = call.principal<JWTPrincipal>()
                    val user = jwt!!.payload.getClaim("userId").asString()
                    val temp = call.receive<String>()
                    /*
                    - get a list of dish names
                    - get uuid from jwt
                    - get timestamp from now
                    - insert into order table
                     */
                    call.application.environment.log.info("user: $user ordered: $temp")
                    call.respond(HttpStatusCode.Created)
                }
            }
            route("/logout") {
                post {
                    val jwt = call.principal<JWTPrincipal>()
                    val user = jwt!!.payload.getClaim("uid").asString()
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
                    val roles = jwt!!.payload.getClaim("roles").asString()
                    if (UserRole.ADMIN.name in roles) {
                        val newDish = call.receive<DishDto>()
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
            route("/newMenu") {
                post {
                    val jwt = call.principal<JWTPrincipal>()
                    val role = UserRole.valueOf(jwt!!.payload.getClaim("role").asString())
                    if (role == UserRole.MODERATOR) {
                        //val newMenu = call.receive<MenuDto>()
                        //menuRepository.addNewMenu(newMenu)
                        /*?
                        dishRepository.addDish(Dish(newMenu.Montag))
                        dishRepository.addDish(Dish(newMenu.Dienstag))
                        dishRepository.addDish(Dish(newMenu.Mittwoch))
                        dishRepository.addDish(Dish(newMenu.Donnerstag))
                        dishRepository.addDish(Dish(newMenu.Freitag))

                         */
                        call.respond(HttpStatusCode.Created)
                    } else {
                        call.respond(HttpStatusCode.Forbidden)
                    }
                }
            }
        }
    }
}
