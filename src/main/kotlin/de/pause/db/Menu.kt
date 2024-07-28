package de.pause.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime


object MenuTable : IntIdTable("shop.menu") {
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val weekId = reference("week_id", WeekTable)
    val dishId = reference("dish_id", DishTable)
    val dayId = reference("day_id", DayTable)
}

class MenuEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MenuEntity>(MenuTable)

    var createdAt by MenuTable.createdAt
    var updatedAt by MenuTable.updatedAt
    var weekId by WeekEntity referencedOn MenuTable.weekId
    var dishId by DishEntity referencedOn MenuTable.dishId
    var dayId by DayEntity referencedOn MenuTable.dayId

}

