package de.pause.db

import org.jetbrains.exposed.sql.Table

object UserRoleTable : Table("shop.user_role") {
    val user = reference("user_id", UserTable)
    val role = reference("role_id", RoleTable)
    override val primaryKey = PrimaryKey(user, role)
}
