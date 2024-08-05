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
    private val testUserPassword = "password"
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
    fun `registering a new user`() = testApplication {

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

}