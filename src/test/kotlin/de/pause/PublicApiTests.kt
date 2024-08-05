package de.pause


import de.pause.model.MenuInfo
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PublicApiTests {

    @Test
    fun `current menu endpoint is publicly available`() = testApplication {
        val response = client.get("/rest/v1/info/menu?from=9999-01-03")
        assertEquals(HttpStatusCode.OK, response.status)
        val menuInfo: MenuInfo = Json.decodeFromString(response.bodyAsText())
        assertEquals("9999-01-01", menuInfo.validFrom)
        assertEquals("9999-01-05", menuInfo.validTo)
        assertEquals(5, menuInfo.dishes.size)
        assertTrue { menuInfo.dishes.all { it.day in 1..5 } }
        assertTrue { menuInfo.dishes.all { it.name.isNotBlank() } }
    }

    @Test
    fun `current menu endpoint returns valid menu if one day is blank`() = testApplication {
        val response = client.get("/rest/v1/info/menu?from=9999-01-10")
        assertEquals(HttpStatusCode.OK, response.status)
        val menuInfo: MenuInfo = Json.decodeFromString(response.bodyAsText())
        assertEquals("9999-01-08", menuInfo.validFrom)
        assertEquals("9999-01-12", menuInfo.validTo)
        assertEquals(5, menuInfo.dishes.size)
        assertTrue { menuInfo.dishes.all { it.day in 1..5 } }
        assertTrue { menuInfo.dishes.any { it.name.isBlank() } }
    }

    @Test
    fun `current menu endpoint returns valid menu if every day is blank`() = testApplication {
        val response = client.get("/rest/v1/info/menu?from=9999-01-15")
        assertEquals(HttpStatusCode.OK, response.status)
        val menuInfo: MenuInfo = Json.decodeFromString(response.bodyAsText())
        assertEquals("9999-01-15", menuInfo.validFrom)
        assertEquals("9999-01-19", menuInfo.validTo)
        assertEquals(5, menuInfo.dishes.size)
        assertTrue { menuInfo.dishes.all { it.day in 1..5 } }
        assertTrue { menuInfo.dishes.all { it.name.isBlank() } }
    }

    @Test
    fun `current menu endpoint returns 400 on invalid date format`() = testApplication {
        val response = client.get("/rest/v1/info/menu?from=9999-01-03T00:00:00")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `current menu endpoint returns 400 on invalid date`() = testApplication {
        val response = client.get("/rest/v1/info/menu?from=9999-13-00")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `current menu endpoint returns 400 on missing parameter 'from'`() = testApplication {
        val response = client.get("/rest/v1/info/menu")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `current menu endpoint returns 404 when no menu is available for the given date`() = testApplication {
        val response = client.get("/rest/v1/info/menu?from=9999-12-31")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

}
