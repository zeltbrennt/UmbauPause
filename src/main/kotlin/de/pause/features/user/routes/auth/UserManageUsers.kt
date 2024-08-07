package de.pause.features.user.routes.auth

import de.pause.features.user.data.dto.UserDto
import de.pause.features.user.data.repo.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.userManageUsers(userRepository: UserRepository) {
    route("/user") {
        authenticate("user") {
            route("/logout") {
                post {
                    val jwt = call.principal<JWTPrincipal>()
                    val id = jwt!!.payload.getClaim("uid").asString()
                    val success = userRepository.logout(id)
                    when {
                        success -> call.respond(HttpStatusCode.OK)
                        else -> call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }

            route("/manage") {
                route("/password") {
                    put {
                        val jwt = call.principal<JWTPrincipal>()
                        val id = jwt!!.payload.getClaim("uid").asString()
                        val newPassword = call.receive<String>()
                        val success = userRepository.updateUserPassword(UUID.fromString(id), newPassword)
                        when {
                            success -> call.respond(HttpStatusCode.OK)
                            else -> call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                route("/profile") {
                    put {
                        val userData = call.receive<UserDto>()
                        val success = userRepository.updateUserData(userData)
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