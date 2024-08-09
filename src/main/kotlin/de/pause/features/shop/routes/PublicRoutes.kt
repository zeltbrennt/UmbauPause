package de.pause.features.shop.routes

import de.pause.features.shop.data.repo.MenuRepository
import de.pause.features.shop.data.repo.OrderRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.joda.time.format.DateTimeFormat

fun Route.shopPublicRoutes(menuRepository: MenuRepository, orderRepository: OrderRepository) {
    route("/info") {
        route("/menu/{from}") {
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

                val menu = menuRepository.getScheduledMenuFrom(day)
                call.respond(menu ?: HttpStatusCode.NotFound)
            }
        }
        route("/locations") {
            get {
                //TODO: Move this to separate repo
                call.respond(orderRepository.getAllLocations())
            }
        }
    }
}