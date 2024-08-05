package de.pause

import de.pause.model.RegisterRequest
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals

class UserRepoTests {
    @Test
    fun `registering a new user`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData = RegisterRequest("test@email.com", "password")
        val response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `registering a new user with existing email should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData = RegisterRequest("alice@example.com", "password")
        val response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    @Ignore
    fun `registering a new user with empty email should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData = RegisterRequest("", "password")
        val response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}