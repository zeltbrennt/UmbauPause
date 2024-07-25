package de.pause.db

import de.pause.model.User
import de.pause.model.UserRole
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("shop.user") {
    val userId = varchar("user_uuid", 250)
    val email = varchar("email", 200)
    val password = varchar("password", 250)
    val role = varchar("role", 100)
}

class UserDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDao>(UserTable)

    var userId by UserTable.userId
    var email by UserTable.email
    var password by UserTable.password
    var role by UserTable.role
}

fun daoToModel(dao: UserDao) = User(
    userId = dao.userId,
    email = dao.email,
    password = dao.password,
    role = UserRole.valueOf(dao.role)
)