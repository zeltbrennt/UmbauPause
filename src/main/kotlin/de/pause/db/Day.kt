package de.pause.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object DayTable : IntIdTable("shop.day") {
    val name = varchar("name", 10)
}

class DayEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DayEntity>(DayTable)

    var name by DayTable.name

}