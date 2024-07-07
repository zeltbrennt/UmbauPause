package de.pause

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BackendApiTests {
    @Test
    fun `current menu endpoint is publicly available`() = testApplication {
        application {

        }
        val response = client.get("/weekly")
        assertEquals(HttpStatusCode.OK, response.status)
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
