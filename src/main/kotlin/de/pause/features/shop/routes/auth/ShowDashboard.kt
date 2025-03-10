package de.pause.features.shop.routes.auth

import de.pause.features.shop.data.dto.LocationDto
import de.pause.features.shop.data.repo.OrderRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.joda.time.format.DateTimeFormat


fun Route.dashboardRoutes(orderRepository: OrderRepository) {
    authenticate("admin") {
        route("/statistics") {
            route("/order-overview/{from}") {
                get {
                    val from = call.parameters["from"]
                    val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
                    val day = try {
                        formatter.parseDateTime(from)
                    } catch (e: IllegalArgumentException) {
                        return@get call.respond(HttpStatusCode.BadRequest, "Invalid date format")
                    } catch (e: NullPointerException) {
                        return@get call.respond(HttpStatusCode.BadRequest, "Missing parameter 'from'")
                    }
                    val overview = orderRepository.getAllOrdersByDate(day)
                    call.respond(overview ?: HttpStatusCode.NotFound)
                }
            }
            route("/tags") {
                get {
                    call.respond(orderRepository.getTagsForAllOrderes())
                }
            }
        }
        route("/location") {
            route("/add") {
                post {
                    val newLocation = call.receiveText()
                    orderRepository.addNewLocation(newLocation)
                }
            }
            route("/modify") {
                put {
                    val location = call.receive<LocationDto>()
                    when (orderRepository.updateLocation(location)) {
                        true -> call.respond(HttpStatusCode.OK)
                        else -> call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
        }
    }
}
