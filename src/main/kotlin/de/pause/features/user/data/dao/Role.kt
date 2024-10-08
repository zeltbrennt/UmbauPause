package de.pause.features.user.data.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object RoleTable : IntIdTable("user.role") {
    val role = varchar("name", 50)
}

class Role(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Role>(RoleTable)

    var role by RoleTable.role
}



