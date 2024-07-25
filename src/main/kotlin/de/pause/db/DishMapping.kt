package de.pause.db

import de.pause.model.Dish
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object DishTable : IntIdTable("shop.dish") {
    val description = varchar("description", 250)
    val price = double("price")
    val dishId = uuid("dish_id")
}

class DishDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DishDao>(DishTable)

    var description by DishTable.description
    var price by DishTable.price
    val dishId by DishTable.dishId
}

fun daoToModel(dao: DishDao) = Dish(
    description = dao.description,
    price = dao.price,
    dishId = dao.dishId.toString()
)
