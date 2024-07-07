package de.pause.plugins

import de.pause.model.DishRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting(dishRepository: DishRepository) {

    routing {
        route("/weekly") {
            get {

                val articles = dishRepository.getAvailableDishes().sortedBy { it.order }
                call.respond(
                    articles
                )
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
