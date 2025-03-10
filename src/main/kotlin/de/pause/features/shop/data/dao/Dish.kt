package de.pause.features.shop.data.dao


import de.pause.features.shop.data.dto.DishDto
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

object DishTable : IntIdTable("shop.dish") {
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val description = varchar("description", 250)
}

class Dish(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Dish>(DishTable)

    var createdAt by DishTable.createdAt
    var updatedAt by DishTable.updatedAt
    var description by DishTable.description

    fun toDishDto() = DishDto(
        id = id.value,
        description = description,
    )
}

object DishPriceTable : IntIdTable("shop.price") {
    val type = varchar("type", 255)
    val price = integer("price")

}
