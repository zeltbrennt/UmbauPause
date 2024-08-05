package de.pause.model

import de.pause.db.*
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
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
            val user = User.new {
                created_at = DateTime.now()
                updated_at = DateTime.now()
                email = loginRequest.email
                passwordHash = hashedPassword
            }
            val roles = Role.find { RoleTable.role eq "USER" }.toList()
            user.roles = SizedCollection(roles)
            return@suspendTransaction user.id.value.toString().isNotBlank()
        } catch (e: Exception) {
            return@suspendTransaction false
        }
    }

    suspend fun getUserByMail(email: String): User? = suspendTransaction {
        User.find { UserTable.email eq email }.firstOrNull()
    }

    suspend fun getUserRolesByUserId(userId: UUID) = suspendTransaction {
        UserRoleTable
            .innerJoin(RoleTable)
            .selectAll()
            .where { UserRoleTable.user eq userId }
            .map {
                it[RoleTable.role]
            }
    }

    suspend fun deleteUserByMailPattern(email: String) = suspendTransaction {
        val user = User.find { Op.build { UserTable.email like "$email%" } }.firstOrNull()
        if (user != null) {
            UserRoleTable.deleteWhere { UserRoleTable.user eq user.id }
            user.delete()
            return@suspendTransaction true
        } else {
            return@suspendTransaction false
        }

    }

}