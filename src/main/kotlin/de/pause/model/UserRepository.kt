package de.pause.model

import de.pause.db.UserDao
import de.pause.db.UserTable
import de.pause.db.daoToModel
import de.pause.db.suspendTransaction
import org.mindrot.jbcrypt.BCrypt

class UserRepository {

    suspend fun setDefaultPasswordOfPreloadedUsers() = suspendTransaction {
        UserDao.all().forEach {
            if (it.password.isBlank()) {
                it.password = BCrypt.hashpw(System.getenv("USER_DEFAULT_PASSWORD"), BCrypt.gensalt())
            }
        }
    }

    suspend fun login(req: LoginRequest): User? = suspendTransaction {// TODO: do send JWT instead of full user data
        val user = UserDao
            .find { UserTable.email eq req.email }
            .firstOrNull()
        if (user != null && BCrypt.checkpw(req.password, user.password)) {
            return@suspendTransaction daoToModel(user)
        }
        return@suspendTransaction null
    }

}