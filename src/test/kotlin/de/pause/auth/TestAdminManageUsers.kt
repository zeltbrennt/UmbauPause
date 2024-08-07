package de.pause.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.pause.features.user.data.dto.RegisterRequest
import de.pause.features.user.data.repo.UserRepository
import de.pause.util.Constraints
import de.pause.util.UserRole
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import java.time.Instant
import java.util.*
import kotlin.test.*

class TestAdminManageUsers {

    private val testUserName = "test"
    private val testUserPassword = "Pas\$w0rd"
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
            .withClaim("role", listOf(UserRole.ADMIN.toString()))
            .sign(Algorithm.HMAC256(secret))
    }


    @Test
    fun `deleting a user by id should succeed`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData = RegisterRequest("$testUserName@$testUserMailSuffix", testUserPassword)
        val response = client.post("/rest/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val user = userRepo.getUserByMail("$testUserName@$testUserMailSuffix")
        assertNotNull(user)
        val userId = user.id.value
        val deleteResponse = client.delete("/rest/v1/user/$userId") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        assertEquals(HttpStatusCode.OK, deleteResponse.status)
        assertNull(userRepo.getUserById(userId))
    }
}