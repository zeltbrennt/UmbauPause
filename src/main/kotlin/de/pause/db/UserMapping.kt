package de.pause.db

import de.pause.model.User
import de.pause.model.UserRole
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("shop.user") {
    val username = varchar("username", 200)
    val email = varchar("email", 200)
    val password = varchar("password", 250)
    val role = varchar("role", 100)
}

class UserDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDao>(UserTable)

    var username by UserTable.username
    var email by UserTable.email
    var password by UserTable.password
    var role by UserTable.role
}

fun daoToModel(dao: UserDao) = User(
    username = dao.username,
    email = dao.email,
    password = dao.password,
    role = UserRole.valueOf(dao.role)
)