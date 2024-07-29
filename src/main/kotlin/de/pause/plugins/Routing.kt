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
    orderRepository: OrderRepository,
) {


    val secret = appConfig.property("ktor.jwt.secret").getString()
    val issuer = appConfig.property("ktor.jwt.issuer").getString()
    val audience = appConfig.property("ktor.jwt.audience").getString()
    val tokenExpiration = 6000000L // todo: just for debugging

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
        authenticate("user") {
            route("/order") {
                post {
                    val jwt = call.principal<JWTPrincipal>()
                    val user = jwt!!.payload.getClaim("uid").asString()
                    val temp = call.receive<List<Int>>()
                    temp.forEach { orderRepository.addOrderByMenuId(it, user) }
                    call.application.environment.log.info("user: $user ordered: $temp")
                    //todo: check if order was successful
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
        }
        authenticate("admin") {
            route("/newDish") {
                post {
                    val newDish = call.receive<DishDto>()
                    dishRepository.addDish(newDish)
                    call.respond(HttpStatusCode.Created)

                }
            }
            route("/dishes") {
                get {
                    call.respond(dishRepository.allDishes())
                }
            }
            route("/orderOverview") {
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
                    val overview = orderRepository.getAllOrdersFrom(day)
                    call.respond(overview ?: HttpStatusCode.NotFound)
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
