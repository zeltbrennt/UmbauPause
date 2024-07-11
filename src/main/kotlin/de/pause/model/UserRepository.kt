package de.pause.model

import de.pause.db.UserDao
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

    suspend fun login(email: String, password: String) = suspendTransaction {

    }

}