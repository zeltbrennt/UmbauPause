package de.pause.features.app.refresh.routes

import de.pause.features.user.data.dto.UserPrincipal
import de.pause.features.user.data.repo.UserRepository
import de.pause.plugins.createAccessToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.refreshToken(userRepository: UserRepository) {
    authenticate("refresh") {
        post("/refresh") {
            val jwt = call.principal<JWTPrincipal>()
            val userID = jwt!!.payload.getClaim("uid").asString()
            val roles = userRepository.getUserRolesByUserId(UUID.fromString(userID))
            val token = createAccessToken(UserPrincipal(userID, roles))
            call.respond(HttpStatusCode.OK, mapOf("accessToken" to token))
        }
    }
}