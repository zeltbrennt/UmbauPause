package de.pause.features.shop.data.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object LocationTable : IntIdTable("shop.location") {
    val name = varchar("name", 100)
    val active = bool("active").default(true)
}

class Location(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Location>(LocationTable)

    var name by LocationTable.name
    var active by LocationTable.active
}