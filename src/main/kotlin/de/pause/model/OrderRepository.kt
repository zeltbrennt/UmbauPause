package de.pause.model

import de.pause.db.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.joda.time.DateTime
import java.util.*

class OrderRepository {

    suspend fun addOrderByMenuId(order: OrderDto, user: String) = suspendTransaction {
        Order.new {
            menuId = Menu[order.item]
            userId = User[UUID.fromString(user)]
            createdAt = DateTime.now()
            updatedAt = DateTime.now()
            status = OrderStatus.OPEN.toString()
            location = Location[order.location]
        }
    }

    suspend fun getAllOrdersFrom(day: DateTime) = suspendTransaction {
        val validFrom: String
        val validTo: String
        val orderCounts = OrderTable
            .innerJoin(MenuTable)
            .innerJoin(LocationTable)
            .select(
                MenuTable.validFrom,
                MenuTable.validTo,
                MenuTable.dayOfWeek,
                LocationTable.name,
                OrderTable.id.count()
            )
            .where {
                (MenuTable.validFrom lessEq day) and
                        (MenuTable.validTo greaterEq day) and
                        (OrderTable.status neq OrderStatus.CANCELED.name)
            }
            .groupBy(MenuTable.dayOfWeek, MenuTable.validFrom, MenuTable.validTo, LocationTable.name)
            .orderBy(MenuTable.dayOfWeek)
            .also {
                if (it.empty()) {
                    return@suspendTransaction null
                }
                validTo = it.first()[MenuTable.validTo].toString("yyyy-MM-dd")
                validFrom = it.first()[MenuTable.validFrom].toString("yyyy-MM-dd")
            }
            .map {
                OrderCounts(
                    day = it[MenuTable.dayOfWeek],
                    location = it[LocationTable.name],
                    orderCount = it[OrderTable.id.count()].toInt()
                )
            }
        OrderOverview(
            validFrom = validFrom,
            validTo = validTo,
            timestamp = DateTime.now().toString(),
            orders = orderCounts
        )
    }


    suspend fun getAllLocations() = suspendTransaction {
        Location.all().map { LocationDto(it.id.value, it.name) }
    }

}