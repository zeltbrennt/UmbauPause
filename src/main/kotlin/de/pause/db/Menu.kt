package de.pause.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.jodatime.datetime


object MenuTable : IntIdTable("shop.menu") {
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val validFrom = date("valid_from")
    val validTo = date("valid_to")
    val dayOfWeek = integer("day_of_week")
    val dishId = reference("dish_id", DishTable)
}

class Menu(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Menu>(MenuTable)

    var createdAt by MenuTable.createdAt
    var updatedAt by MenuTable.updatedAt
    var validFrom by MenuTable.validFrom
    var validTo by MenuTable.validTo
    var dayOfWeek by MenuTable.dayOfWeek
    var dishId by Dish referencedOn MenuTable.dishId

}

