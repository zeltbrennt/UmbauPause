package de.pause.db

import de.pause.model.UserPrincipal
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.datetime
import java.util.*

object UserTable : UUIDTable("shop.user") {
    val email = varchar("email", 200)
    val created_at = datetime("created_at")
    val updated_at = datetime("updated_at")
    val passwordHash = varchar("password_hash", 250)
    val role = varchar("role", 100)
}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(UserTable)

    var email by UserTable.email
    var created_at by UserTable.created_at
    var updated_at by UserTable.updated_at
    var passwordHash by UserTable.passwordHash
    var role by UserTable.role

    fun toUserPrincipal() = UserPrincipal(
        id = id.value.toString(),
        role = role
    )
}

