package de.pause.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.timestampWithTimeZone


object OrderTable : IntIdTable("shop.order") {
    val userId = uuid("user_id")
    val dishName = varchar("dish_name", 250)
    val status = varchar("status", 100)
    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")
}

class OrderDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderDao>(OrderTable)

    var userId by OrderTable.userId
    var dishId by OrderTable.dishName
    var status by OrderTable.status
    var createdAt by OrderTable.createdAt
    var updatedAt by OrderTable.updatedAt

}

