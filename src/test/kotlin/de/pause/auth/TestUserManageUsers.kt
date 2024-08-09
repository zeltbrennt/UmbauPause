package de.pause.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.pause.features.user.data.dto.LoginRequest
import de.pause.features.user.data.repo.UserRepository
import de.pause.util.Constraints
import de.pause.util.UserRole
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import java.time.Instant
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class TestUserManageUsers {

    private val testUserName = "test"
    private val testUserPassword = "P@ssw0rd"
    private val testUserMailSuffix = Constraints.VALID_USER_EMAIL_SUFFIX
    private val userRepo = UserRepository()

    private lateinit var token: String

    @BeforeTest
    fun setup() {
        val secret = System.getenv("JWT_SHARED_SECRET")
        token = JWT.create()
            .withJWTId(UUID.randomUUID().toString())
            .withAudience(System.getenv("JWT_AUDIENCE"))
            .withIssuer(System.getenv("JWT_ISSUER"))
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(10))
            .withClaim("uid", "irrelevant")
            .withClaim("role", listOf(UserRole.USER.toString()))
            .sign(Algorithm.HMAC256(secret))
    }

    @Test
    fun `logout with valid token should succeed`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val loginRequest = LoginRequest("alice@example.com", System.getenv("USER_DEFAULT_PASSWORD"))
        val response = client.post("/rest/v1/user/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val accessToken = response.body<Map<String, String>>()["accessToken"]
        val logoutResponse = client.post("/rest/v1/user/logout") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${accessToken!!}")
            }
        }
        assertEquals(HttpStatusCode.OK, logoutResponse.status)
    }

    @Test
    fun `logout with invalid token should fail`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val loginRequest = LoginRequest("alice@example.com", System.getenv("USER_DEFAULT_PASSWORD"))
        val response = client.post("/rest/v1/user/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val logoutResponse = client.post("/rest/v1/user/logout") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token}")
            }
        }
        assertEquals(HttpStatusCode.Unauthorized, logoutResponse.status)
    }

    @Test
    fun `change password with valid token should succeed`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val loginRequest = LoginRequest("bob@example.com", System.getenv("USER_DEFAULT_PASSWORD"))
        val response = client.post("/rest/v1/user/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val accessToken = response.body<Map<String, String>>()["accessToken"]
        val newPassword = testUserPassword
        val changePasswordResponse = client.put("/rest/v1/user/manage/password") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${accessToken!!}")
            }
            setBody(newPassword)
        }
        assertEquals(HttpStatusCode.OK, changePasswordResponse.status)
        val logoutResponse = client.post {
            url("/rest/v1/user/logout")
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${accessToken!!}")
            }
        }
        assertEquals(HttpStatusCode.OK, logoutResponse.status)
        val loginRequestWithNewPassword = LoginRequest("bob@example.com", testUserPassword)
        val loginResponse = client.post("/rest/v1/user/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequestWithNewPassword)
        }
        assertEquals(HttpStatusCode.OK, loginResponse.status)
        //reset
        val resetPasswordResponse = client.put("/rest/v1/user/manage/password") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${accessToken!!}")
            }
            setBody(System.getenv("DEFAULT_USER_PASSWORD"))
        }
        assertEquals(HttpStatusCode.OK, resetPasswordResponse.status)
    }

}
