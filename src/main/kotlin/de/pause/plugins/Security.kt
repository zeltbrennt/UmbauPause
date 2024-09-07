package de.pause.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.pause.features.user.data.dto.UserPrincipal
import de.pause.util.UserRole
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.*
import kotlin.properties.Delegates

lateinit var secret: String
lateinit var issuer: String
lateinit var audience: String
var tokenExpiration by Delegates.notNull<Long>()
var refreshTokenExpiration by Delegates.notNull<Long>()
var cookieSecure by Delegates.notNull<Boolean>()

fun Application.configureSecurity(appConfig: HoconApplicationConfig) {

    secret = appConfig.property("ktor.jwt.secret").getString()
    issuer = appConfig.property("ktor.jwt.issuer").getString()
    audience = appConfig.property("ktor.jwt.audience").getString()
    tokenExpiration = appConfig.property("ktor.jwt.expiration").getString().toLong()
    refreshTokenExpiration = appConfig.property("ktor.jwt.refreshExpiration").getString().toLong()
    cookieSecure = appConfig.property("ktor.cookie.secure").getString().toBoolean()

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

private fun createAccessToken(user: UserPrincipal, fingerprintHash: String): String = JWT.create()
    .withJWTId(UUID.randomUUID().toString())
    .withAudience(audience)
    .withIssuer(issuer)
    .withIssuedAt(Instant.now())
    .withExpiresAt(Instant.now().plusSeconds(tokenExpiration))
    .withClaim("uid", user.id)
    .withClaim("roles", user.roles)
    .withClaim("fingerprint", fingerprintHash)
    .sign(Algorithm.HMAC256(secret))

private fun createRefreshToken(user: UserPrincipal): String = JWT.create()
    .withJWTId(UUID.randomUUID().toString())
    .withAudience(audience)
    .withIssuer(issuer)
    .withIssuedAt(Instant.now())
    .withExpiresAt(Instant.now().plusSeconds(refreshTokenExpiration))
    .withClaim("uid", user.id)
    .sign(Algorithm.HMAC256(secret))

fun createUserTokens(user: UserPrincipal): UserTokens {
    val fingerprint = createFingerprint()
    val accessToken = createAccessToken(user, fingerprint.hash)
    val cookie = Cookie(
        name = "access_fingerprint",
        value = fingerprint.value,
        maxAge = tokenExpiration.toInt(),
        path = "/",
        secure = cookieSecure,
        httpOnly = true,
    )
    // TODO: cookie.extensions["SameSite"] = "Strict"
    return UserTokens(accessToken, cookie)
}

data class UserTokens(val accessToken: String, val cookie: Cookie)
data class Fingerprint(val value: String, val hash: String)

private fun createFingerprint(): Fingerprint {
    val random = SecureRandom.getInstanceStrong()
    val bytes = ByteArray(64)
    random.nextBytes(bytes)
    val value = bytes.joinToString("") { "%02x".format(it) }
    val md = MessageDigest.getInstance("SHA-256")
    val hash = md.digest(value.toByteArray()).joinToString("") { "%02x".format(it) }
    return Fingerprint(value, hash)
}

fun validateFingerprint(fingerprint: Fingerprint): Boolean {
    val md = MessageDigest.getInstance("SHA-256")
    val hash = md.digest(fingerprint.value.toByteArray()).joinToString("") { "%02x".format(it) }
    return hash == fingerprint.hash
}