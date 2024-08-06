package de.pause

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.pause.features.user.data.dto.LoginRequest
import de.pause.model.UserRole
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SecurityTests {

    /* TODO: cleanup is not working
    @AfterTest
    fun cleanup() {
        runBlocking {
            suspendTransaction {
                UserTable.deleteWhere { email like "test%" }
            }
        }
    }
     */


    @Test
    fun `login with example data`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val loginRequestData = LoginRequest("alice@example.com", System.getenv("USER_DEFAULT_PASSWORD"))
        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequestData)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val respondBody = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        val token = respondBody["accessToken"]!!.jsonPrimitive.content
        val decodedJWT = JWT.decode(token)
        assertEquals("alice@example.com", decodedJWT.getClaim("email").asString())
        assertEquals(UserRole.USER.toString(), decodedJWT.getClaim("role").asString())

        val secret = System.getenv("JWT_SHARED_SECRET")
        val algorithm = Algorithm.HMAC256(secret)
        val verifier = JWT.require(algorithm).build()
        assertTrue { verifier.verify(token) != null }

    }

    @Test
    fun `login with wrong password should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val loginRequestData = LoginRequest("alice@example.com", "wrong")
        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequestData)
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `login with unknown user should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val loginRequestData = LoginRequest("charly@example.com", "wrong")
        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequestData)
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `logout with example data`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        //login first
        val loginRequestData = LoginRequest("alice@example.com", System.getenv("USER_DEFAULT_PASSWORD"))
        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequestData)
        }
        //receive token from response and try to logout
        val respondBody = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        val token = respondBody["accessToken"]!!.jsonPrimitive.content
        val logoutResponse = client.post("/logout") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, logoutResponse.status)
    }

    @Test
    fun `adding new dish without jwt is impossible`() = testApplication {

    }

    @Test
    fun `add new dish with wrong role in jwt is impossible`() = testApplication {

    }

}
