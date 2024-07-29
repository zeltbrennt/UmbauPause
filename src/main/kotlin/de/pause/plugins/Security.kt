package de.pause.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.pause.model.UserRole
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureSecurity(appConfig: HoconApplicationConfig) {

    val secret = appConfig.property("ktor.jwt.secret").getString()
    val issuer = appConfig.property("ktor.jwt.issuer").getString()
    val audience = appConfig.property("ktor.jwt.audience").getString()

    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(Authentication) {

        jwt("user") {
            realm = "webshop"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                when {
                    credential.payload.getClaim("uid").asString().isBlank() -> null
                    !credential.payload.getClaim("roles").asList(String::class.java)
                        .contains(UserRole.USER.name) -> null

                    else -> JWTPrincipal(credential.payload)
                }
            }
        }
        jwt("admin") {
            realm = "webshop"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                when {
                    credential.payload.getClaim("uid").asString().isBlank() -> null
                    !credential.payload.getClaim("roles").asList(String::class.java)
                        .contains(UserRole.ADMIN.name) -> null

                    else -> JWTPrincipal(credential.payload)
                }
            }
        }
    }

    routing {
        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}

