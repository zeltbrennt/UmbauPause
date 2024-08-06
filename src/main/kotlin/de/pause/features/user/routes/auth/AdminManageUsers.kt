package de.pause.features.user.routes.auth

import de.pause.features.user.data.repo.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*


fun Route.adminManageUsers(userRepository: UserRepository) {
    authenticate("admin") {
        route("/user") {
            route("/{id}") {
                get {
                    val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                    val user = userRepository.getUserByMail(id)
                    when {
                        user != null -> call.respond(user.toUserPrincipal())
                        else -> call.respond(HttpStatusCode.NotFound)
                    }
                }
                delete {
                    val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    val success = userRepository.deleteUserById(UUID.fromString(id))
                    when {
                        success -> call.respond(HttpStatusCode.OK)
                        else -> call.respond(HttpStatusCode.NotFound)
                    }
                }
                route("/roles") {
                    put {
                        val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                        val roles = call.receive<List<String>>()
                        val success = userRepository.updateUserRoles(UUID.fromString(id), roles)
                        when {
                            success -> call.respond(HttpStatusCode.OK)
                            else -> call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
            }
        }
    }
}
