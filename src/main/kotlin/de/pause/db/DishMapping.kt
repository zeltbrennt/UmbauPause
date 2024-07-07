package de.pause.db

import de.pause.model.Dish
import de.pause.model.dayOfWeekToInt
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import java.time.LocalDateTime
import java.time.LocalTime

object DishTable : IntIdTable("shop.dish") {
    val description = varchar("description", 250)
    val available = bool("available")
    val scheduled = varchar("scheduled", 50)
    val price = double("price")
}

class DishDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DishDao>(DishTable)

    var description by DishTable.description
    var available by DishTable.available
    var scheduled by DishTable.scheduled
    var price by DishTable.price
}

fun daoToModel(dao: DishDao) = Dish(
    description = dao.description,
    scheduled = dao.scheduled,
    available = isAvailable(dao.scheduled),
    price = dao.price
)

fun isAvailable(scheduled: String): Boolean {
    val now = LocalDateTime.now()
    val dayOfWeek = scheduled.dayOfWeekToInt()
    if (now.dayOfWeek.value == dayOfWeek && now.toLocalTime().isAfter(LocalTime.of(10, 30))) return false
    return now.dayOfWeek.value <= dayOfWeek
}