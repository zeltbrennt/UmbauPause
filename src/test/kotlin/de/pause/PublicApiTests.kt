package de.pause

import de.pause.model.Dish
import de.pause.model.RegisterRequest
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class PublicApiTests {

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
    fun `current menu endpoint is publicly available`() = testApplication {
        val response = client.get("/weekly")
        assertEquals(HttpStatusCode.OK, response.status)
        val dishes: List<Dish> = Json.decodeFromString(response.bodyAsText())
        assertEquals(5, dishes.size)
        val wochentage = listOf("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag")
        assertContentEquals(wochentage, dishes.map { it.scheduled })
    }

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
