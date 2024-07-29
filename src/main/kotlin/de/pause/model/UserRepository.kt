package de.pause.model

import de.pause.db.User
import de.pause.db.UserTable
import de.pause.db.suspendTransaction
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class UserRepository {

    suspend fun setDefaultPasswordOfPreloadedUsers() = suspendTransaction {
        User.all().forEach {
            if (it.passwordHash.isBlank()) {
                it.passwordHash = BCrypt.hashpw(System.getenv("USER_DEFAULT_PASSWORD"), BCrypt.gensalt())
            }
        }
    }


    suspend fun login(req: LoginRequest): UserPrincipal? = suspendTransaction {
        val user = User
            .find { UserTable.email eq req.email }
            .firstOrNull()
        when {
            user != null && BCrypt.checkpw(req.password, user.passwordHash) ->
                return@suspendTransaction user.toUserPrincipal()

            else -> return@suspendTransaction null
        }
    }

    suspend fun logout(uuidString: String): Boolean = suspendTransaction {
        val uuid = UUID.fromString(uuidString)
        val user = User.find { UserTable.id eq uuid }.firstOrNull()
        when {
            user != null -> return@suspendTransaction true
            else -> return@suspendTransaction false
        }
    }

    suspend fun register(loginRequest: RegisterRequest): Boolean = suspendTransaction {
        val hashedPassword = BCrypt.hashpw(loginRequest.password, BCrypt.gensalt())
        try {
            return@suspendTransaction User.new {
                created_at = DateTime.now()
                updated_at = DateTime.now()
                email = loginRequest.email
                passwordHash = hashedPassword
                role = Enums.USER.toString()
            }.id.value.toString().isNotBlank()
        } catch (e: Exception) {
            return@suspendTransaction false
        }
    }

}