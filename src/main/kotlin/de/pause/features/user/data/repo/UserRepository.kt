package de.pause.features.user.data.repo

import de.pause.database.suspendTransaction
import de.pause.features.user.data.dao.*
import de.pause.features.user.data.dto.LoginRequest
import de.pause.features.user.data.dto.RegisterRequest
import de.pause.features.user.data.dto.UserDto
import de.pause.features.user.data.dto.UserPrincipal
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
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
            .find { (UserTable.email eq req.email) and (UserTable.emailVerified eq true) }
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

    suspend fun register(loginRequest: RegisterRequest): User? = suspendTransaction {
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
            return@suspendTransaction user
        } catch (e: Exception) {
            return@suspendTransaction null
        }
    }

    suspend fun getUserByMail(email: String): User? = suspendTransaction {
        User.find { UserTable.email eq email }.firstOrNull()
    }

    suspend fun getUserRolesByUserId(userId: UUID) = suspendTransaction {
        User.findById(userId)?.roles?.map { it.role } ?: emptyList()
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


    suspend fun deleteUserById(userId: UUID) = suspendTransaction {
        val user = User.findById(userId)
        if (user != null) {
            UserRoleTable.deleteWhere { UserRoleTable.user eq user.id }
            user.delete()
            return@suspendTransaction true
        } else {
            return@suspendTransaction false
        }
    }

    suspend fun getUserById(userId: UUID): User? = suspendTransaction {
        User.findById(userId)
    }

    suspend fun updateUserRoles(userId: UUID, roles: List<String>) = suspendTransaction {
        val user = User.findById(userId)
        if (user != null) {
            val newRoles = Role.find { RoleTable.role inList roles }.toList()
            user.roles = SizedCollection(newRoles)
            return@suspendTransaction true
        } else {
            return@suspendTransaction false
        }
    }

    suspend fun updateUserPassword(id: UUID, newPassword: String) = suspendTransaction {
        val user = User.findById(id)
        if (user != null) {
            user.passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt())
            return@suspendTransaction true
        } else {
            return@suspendTransaction false
        }
    }

    suspend fun updateUserData(userDto: UserDto) = suspendTransaction {
        val user = User.find { UserTable.email eq userDto.email }.firstOrNull()
        if (user != null) {
            user.email = userDto.email
            return@suspendTransaction true
        } else {
            return@suspendTransaction false
        }
    }
}