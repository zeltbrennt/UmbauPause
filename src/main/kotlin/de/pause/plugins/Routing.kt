package de.pause.plugins

import de.pause.model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.joda.time.format.DateTimeFormat


fun Application.configureRouting(
    dishRepository: DishRepository,
    userRepository: UserRepository,
    menuRepository: MenuRepository,
    orderRepository: OrderRepository,
) {

    val orderUpdates = MutableStateFlow(value = 0)

    suspend fun onOrderUpdated() {
        val currenOrders = orderRepository.getCountCurrentOrders()
        orderUpdates.emit(currenOrders.toInt())
    }

    routing {

        webSocket("/ws") {
            val job = launch {
                orderUpdates.collect {
                    outgoing.send(Frame.Text(it.toString()))
                }
            }
            try {
                job.join()
            } finally {
                job.cancel()
            }
        }

        route("/rest/v1") {
            route("/info") {
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
                route("/locations") {
                    get {
                        call.respond(orderRepository.getAllLocations())
                    }
                }
                authenticate("admin") {
                    route("/dishes") {
                        get {
                            call.respond(dishRepository.allDishes())
                        }
                    }
                }
            }
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
                        if (loginRequest.email.isBlank() || loginRequest.password.isBlank()) {
                            call.respond(HttpStatusCode.BadRequest, "Email or password is missing")
                            return@post
                        }
                        if (loginRequest.email.endsWith(Constraints.VALID_USER_EMAIL_SUFFIX).not()) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid email suffix")
                            return@post
                        }
                        val success = userRepository.register(loginRequest)
                        when {
                            success -> call.respond(HttpStatusCode.Created)
                            else -> call.respond(HttpStatusCode.UnprocessableEntity, "User already exists")
                        }
                    }
                }
                authenticate("user") {
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
            }
            authenticate("user") {
                route("/order") {
                    post {
                        val jwt = call.principal<JWTPrincipal>()
                        val user = jwt!!.payload.getClaim("uid").asString()
                        val temp = call.receive<List<OrderDto>>()
                        temp.forEach { orderRepository.addOrderByMenuId(it, user) }
                        //call.application.environment.log.info("user: $user ordered: $temp")
                        //todo: check if order was successful
                        onOrderUpdated()
                        call.respond(HttpStatusCode.Created)
                    }
                }
            }
            authenticate("admin") {
                route("/content") {
                    route("/add-dish") {
                        post {
                            val newDish = call.receive<DishDto>()
                            dishRepository.addDish(newDish)
                            call.respond(HttpStatusCode.Created)

                        }
                    }
                    route("/new-menu") {
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
                route("/statistics") {
                    route("/order-overview") {
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
                }

            }
        }
    }
}


