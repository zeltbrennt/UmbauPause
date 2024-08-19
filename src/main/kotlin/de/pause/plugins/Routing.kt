package de.pause.plugins

import de.pause.features.app.data.AppRepository
import de.pause.features.app.routes.sendAppFeedback
import de.pause.features.shop.data.dto.OrderDto
import de.pause.features.shop.data.repo.DishRepository
import de.pause.features.shop.data.repo.MenuRepository
import de.pause.features.shop.data.repo.OrderRepository
import de.pause.features.shop.routes.auth.dashboardRoutes
import de.pause.features.shop.routes.auth.manageShopContent
import de.pause.features.shop.routes.shopPublicRoutes
import de.pause.features.user.data.repo.UserRepository
import de.pause.features.user.routes.auth.adminManageUsers
import de.pause.features.user.routes.auth.userManageUsers
import de.pause.features.user.routes.userAuthentication
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


fun Application.configureRouting(
    dishRepository: DishRepository,
    userRepository: UserRepository,
    menuRepository: MenuRepository,
    orderRepository: OrderRepository,
    appRepository: AppRepository,
) {

    val orderUpdates = MutableStateFlow(value = 0)
    val version = this::class.java.classLoader.getResource("version.properties")?.readText() ?: "unknown"


    suspend fun onOrderUpdated() {
        val currenOrders = orderRepository.getCountCurrentOrders()
        orderUpdates.emit(currenOrders.toInt())
    }

    routing {

        webSocket("/ws") {
            onOrderUpdated()
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
            route("/app-version") {
                get {

                    call.respondText(version.substringAfter("="))
                }
            }

            shopPublicRoutes(menuRepository, orderRepository)
            manageShopContent(dishRepository, menuRepository)
            dashboardRoutes(orderRepository)

            adminManageUsers(userRepository)
            userManageUsers(userRepository)
            userAuthentication(userRepository)
            sendAppFeedback(appRepository)

            authenticate("user") {
                route("/order") {
                    post {
                        val jwt = call.principal<JWTPrincipal>()
                        val user = jwt!!.payload.getClaim("uid").asString()
                        val orderRequest = call.receive<OrderDto>()
                        orderRequest.orders.forEach {
                            orderRepository.addOrderByMenuId(
                                it,
                                user,
                                orderRequest.validFrom,
                                orderRequest.validTo
                            )
                        }
                        //call.application.environment.log.info("user: $user ordered: $temp")
                        //todo: check if order was successful
                        onOrderUpdated()
                        call.respond(HttpStatusCode.Created)
                    }
                }
            }

        }
    }
}


