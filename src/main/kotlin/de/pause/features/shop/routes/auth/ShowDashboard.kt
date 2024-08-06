package de.pause.features.shop.routes.auth

import de.pause.features.shop.data.repo.OrderRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.joda.time.format.DateTimeFormat


fun Route.dashboardRoutes(orderRepository: OrderRepository) {
    authenticate("admin") {
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
