package de.pause.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.pause.features.user.data.dto.UserPrincipal
import de.pause.util.UserRole
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import java.time.Instant
import java.util.*
import kotlin.properties.Delegates

lateinit var secret: String
lateinit var issuer: String
lateinit var audience: String
var tokenExpiration by Delegates.notNull<Long>()
var refreshTokenExpiration by Delegates.notNull<Long>()

fun Application.configureSecurity(appConfig: HoconApplicationConfig) {

    secret = appConfig.property("ktor.jwt.secret").getString()
    issuer = appConfig.property("ktor.jwt.issuer").getString()
    audience = appConfig.property("ktor.jwt.audience").getString()
    tokenExpiration = appConfig.property("ktor.jwt.expiration").getString().toLong()
    refreshTokenExpiration = appConfig.property("ktor.jwt.refreshExpiration").getString().toLong()

    install(Authentication) {

        jwt("user") {
            realm = "user"
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
            realm = "admin"
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
        jwt("refresh") {
            realm = "refresh"
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
                    credential.payload.getClaim("roles").isMissing.not() -> null// access tokens can not be uses as refresh tokens
                    else -> JWTPrincipal(credential.payload)
                }
            }
        }
    }

}

fun createAccessToken(user: UserPrincipal): String = JWT.create()
    .withJWTId(UUID.randomUUID().toString())
    .withAudience(audience)
    .withIssuer(issuer)
    .withIssuedAt(Instant.now())
    .withExpiresAt(Instant.now().plusSeconds(tokenExpiration))
    .withClaim("uid", user.id)
    .withClaim("roles", user.roles)
    .sign(Algorithm.HMAC256(secret))

fun createRefreshToken(user: UserPrincipal): String = JWT.create()
    .withJWTId(UUID.randomUUID().toString())
    .withAudience(audience)
    .withIssuer(issuer)
    .withIssuedAt(Instant.now())
    .withExpiresAt(Instant.now().plusSeconds(refreshTokenExpiration))
    .withClaim("uid", user.id)
    .sign(Algorithm.HMAC256(secret))