package de.pause.db

import de.pause.model.Dish
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object DishTable : IntIdTable("shop.dish") {
    val description = varchar("description", 250)
    val price = double("price")
}

class DishDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DishDao>(DishTable)

    var description by DishTable.description
    var price by DishTable.price
}

fun daoToModel(dao: DishDao) = Dish(
    description = dao.description,
    price = dao.price
)
