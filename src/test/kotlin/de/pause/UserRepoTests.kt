package de.pause

import de.pause.model.Constraints
import de.pause.model.RegisterRequest
import de.pause.model.UserRepository
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Order
import kotlin.test.*


class UserRepoTests {

    private val testUserName = "test"
    private val testUserPassword = "Pas\$w0rd"
    private val testUserMailSuffix = Constraints.VALID_USER_EMAIL_SUFFIX
    private val userRepo = UserRepository()


    @AfterEach
    fun cleanup(): Unit {
        runBlocking {
            userRepo.deleteUserByMailPattern(testUserName)
        }
    }

    @Test
    @Order(1)
    fun `registering a new user with valid data should succeed`() = testApplication {

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
        val roles = userRepo.getUserRolesByUserId(userId)
        assertTrue { roles.contains("USER") }
        assertFalse { roles.contains("ADMIN") }
    }

    @Test
    @Order(2)
    fun `registering a new user with existing email should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData = RegisterRequest("$testUserName@$testUserMailSuffix", testUserPassword)
        client.post("/rest/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        val response = client.post("/rest/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)

    }

    @Test
    @Order(4)
    fun `registering a new user with invalid email should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData = RegisterRequest("$testUserName@invalid.com", testUserPassword)
        val response = client.post("/rest/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `registering a new user with a short password should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData = RegisterRequest("$testUserName@$testUserMailSuffix", testUserPassword.substring(0, 7))
        val response = client.post("/rest/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `registering a new user with a blank password should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData = RegisterRequest("$testUserName@$testUserMailSuffix", "")
        val response = client.post("/rest/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `registering a new user with a blank email should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData = RegisterRequest("", testUserPassword)
        val response = client.post("/rest/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `registering a new user with password without numbers should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData =
            RegisterRequest("$testUserName@$testUserMailSuffix", testUserPassword.replace("0", "o"))
        val response = client.post("/rest/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `registering a new user with password without special characters should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData =
            RegisterRequest("$testUserName@$testUserMailSuffix", testUserPassword.replace("$", "s"))
        val response = client.post("/rest/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `registering a new user with password without upper case letters should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData = RegisterRequest("$testUserName@$testUserMailSuffix", testUserPassword.lowercase())
        val response = client.post("/rest/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `registering a new user with password without lower case letters should fail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerRequestData = RegisterRequest("$testUserName@$testUserMailSuffix", testUserPassword.uppercase())
        val response = client.post("/rest/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequestData)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

}