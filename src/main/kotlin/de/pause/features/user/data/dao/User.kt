package de.pause.features.user.data.dao

import de.pause.features.user.data.dto.UserPrincipal
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.datetime
import java.util.*

object UserTable : UUIDTable("user.account") {
    val email = varchar("email", 200)
    val created_at = datetime("created_at")
    val updated_at = datetime("updated_at")
    val passwordHash = varchar("password_hash", 250)
}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(UserTable)

    var email by UserTable.email
    var created_at by UserTable.created_at
    var updated_at by UserTable.updated_at
    var passwordHash by UserTable.passwordHash
    var roles by Role via UserRoleTable

    fun toUserPrincipal() = UserPrincipal(
        id = id.value.toString(),
        roles = roles.map { it.role }
    )
}
