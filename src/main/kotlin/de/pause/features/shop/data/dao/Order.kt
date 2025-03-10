package de.pause.features.shop.data.dao

import de.pause.features.user.data.dao.User
import de.pause.features.user.data.dao.UserTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.datetime
import java.util.*


object OrderTable : UUIDTable("shop.order") {
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val userId = reference("user_id", UserTable)
    val menuId = reference("menu_id", MenuTable)
    val status = varchar("status", 50)
    val paymentSession = varchar("payment_session_id", 255)
    val location = reference("location_id", LocationTable)
    val validFrom = datetime("valid_from")
    val validTo = datetime("valid_to")
}

class Order(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Order>(OrderTable)

    var createdAt by OrderTable.createdAt
    var updatedAt by OrderTable.updatedAt
    var userId by User referencedOn OrderTable.userId
    var menuId by Menu referencedOn OrderTable.menuId
    var status by OrderTable.status
    var paymentSession by OrderTable.paymentSession
    var location by Location referencedOn OrderTable.location
    var validFrom by OrderTable.validFrom
    var validTo by OrderTable.validTo
}

