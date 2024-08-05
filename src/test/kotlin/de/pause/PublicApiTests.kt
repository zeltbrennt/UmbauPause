package de.pause


import com.typesafe.config.ConfigFactory
import de.pause.model.MenuInfo
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PublicApiTests {

    @Test
    fun `current menu endpoint is publicly available`() = testApplication {
        environment {
            config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        }
        val response = client.get("/rest/v1/info/menu?from=2024-08-01")
        assertEquals(HttpStatusCode.OK, response.status)
        val menuInfo: MenuInfo = Json.decodeFromString(response.bodyAsText())
        assertEquals("2024-07-29", menuInfo.validFrom)
        assertEquals("2024-08-02", menuInfo.validTo)
        assertEquals(5, menuInfo.dishes.size)
        assertTrue { menuInfo.dishes.all { it.day in 1..5 } }
        assertTrue { menuInfo.dishes.all { it.name.isNotBlank() } }
    }

    @Test
    fun `current menu endpoint returns 400 on invalid date format`() = testApplication {
        val response = client.get("/rest/v1/info/menu?from=2024-08-01T00:00:00")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `current menu endpoint returns 400 on invalid date`() = testApplication {
        val response = client.get("/rest/v1/info/menu?from=2024-13-00")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `current menu endpoint returns 400 on missing parameter 'from'`() = testApplication {
        val response = client.get("/rest/v1/info/menu")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `current menu endpoint returns 404 when no menu is available for the given date`() = testApplication {
        val response = client.get("/rest/v1/info/menu?from=9999-01-01")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

}
