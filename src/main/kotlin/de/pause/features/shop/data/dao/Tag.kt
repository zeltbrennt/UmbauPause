package de.pause.features.shop.data.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object TagTable : IntIdTable("shop.tag") {
    val name = varchar("name", 255)
    val timesUsed = integer("times_used").default(0)
}

class Tag(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Tag>(TagTable)

    var name by TagTable.name
    var timesUsed by TagTable.timesUsed

}

