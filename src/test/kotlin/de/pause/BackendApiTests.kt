package de.pause

import de.pause.model.Dish
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class BackendApiTests {
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
    fun `add new menu is not publicly available`() = testApplication {

        val response = client.get("/newMenu")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `add new menu is available with vite token`() = testApplication {

        val response = client.get("/newMenu") {
            header(HttpHeaders.Authorization, "Bearer ${System.getenv("VITE_API_TOKEN")}")
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
