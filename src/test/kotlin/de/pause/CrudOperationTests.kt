/*package de.pause

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.pause.model.Dish
import de.pause.model.UserRole
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import java.time.Instant
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CrudOperationTests {

    private lateinit var token: String
    private lateinit var client: HttpClient

    @BeforeTest
    fun setup() {
        val secret = System.getenv("JWT_SHARED_SECRET")
        token = JWT.create()
            .withJWTId(UUID.randomUUID().toString())
            .withAudience(System.getenv("JWT_AUDIENCE"))
            .withIssuer(System.getenv("JWT_ISSUER"))
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(600L))
            .withClaim("email", "mod@example.com")
            .withClaim("role", UserRole.MODERATOR.toString())
            .sign(Algorithm.HMAC256(secret))
        client = createTestClient()
    }

    private fun createTestClient() = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }


    @Test
    fun `role moderator can access newDish endpoint`() = testApplication {
        val testDish = Dish(
            description = "TestDish",
            available = false,
            scheduled = "Montag",
            price = 9.99,
        )
        val response = client.post("/newDish") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(testDish)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }
}
*/