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
import org.jetbrains.exposed.exceptions.ExposedSQLException
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
        route("/location") {
            get {
                call.respond(orderRepository.getAllLocations())
            }
        }
        authenticate("user") {
            route("/order") {
                post {
                    val jwt = call.principal<JWTPrincipal>()
                    val user = jwt!!.payload.getClaim("uid").asString()
                    val temp = call.receive<List<Int>>()
                    temp.forEach { orderRepository.addOrderByMenuId(it, user) }
                    //call.application.environment.log.info("user: $user ordered: $temp")
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

                    val newMenu = call.receive<MenuInfo>()
                    call.application.environment.log.info(newMenu.toString())
                    newMenu.dishes.forEach {
                        val dish = try {
                            dishRepository.addDish(DishDto(0, it.name))
                        } catch (e: ExposedSQLException) {
                            call.application.environment.log.info("Dish already exists")
                            dishRepository.findByName(it.name)
                        }
                        try {
                            call.application.environment.log.info("new Menu: ${newMenu.validFrom}, ${it.day} , ${dish.id.value}")
                            menuRepository.addNewMenu(newMenu.validFrom, newMenu.validTo, it.day, dish.id.value)
                        } catch (e: ExposedSQLException) {
                            call.application.environment.log.info("Menu already exists")
                            //call.respond(HttpStatusCode.Conflict)
                        }
                    }
                    call.respond(HttpStatusCode.Created)
                }
            }
        }
    }
}
