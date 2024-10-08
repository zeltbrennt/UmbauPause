package de.pause.features.shop.data.repo

import de.pause.database.suspendTransaction
import de.pause.features.shop.data.dao.*
import de.pause.features.shop.data.dto.*
import de.pause.features.user.data.dao.User
import de.pause.util.OrderStatus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.joda.time.DateTime
import java.util.*

class OrderRepository {

    suspend fun addOrderByMenuId(order: SingleOrderDto, user: String, from: String, to: String) = suspendTransaction {
        Order.new {
            menuId = Menu[order.item]
            userId = User[UUID.fromString(user)]
            createdAt = DateTime.now()
            updatedAt = DateTime.now()
            status = OrderStatus.OPEN.toString()
            location = Location[order.location]
            validFrom = DateTime(from)
            validTo = DateTime(to)
        }
    }

    suspend fun getAllOrdersByDate(day: DateTime) = suspendTransaction {
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

    suspend fun getAllOrdersByUser(user: UUID) = suspendTransaction {
        return@suspendTransaction OrderTable
            .innerJoin(MenuTable)
            .innerJoin(LocationTable)
            .innerJoin(DishTable)
            .select(
                OrderTable.id,
                MenuTable.validFrom,
                MenuTable.dayOfWeek,
                LocationTable.name,
                DishTable.description,
                OrderTable.status,
            ).where {
                OrderTable.userId eq user
            }
            .map {
                UserOrderDto(
                    id = it[OrderTable.id].value.toString(),
                    date = it[MenuTable.validFrom].plusDays(it[MenuTable.dayOfWeek] - 1).toString("yyyy-MM-dd"),
                    dish = it[DishTable.description],
                    location = it[LocationTable.name],
                    status = it[OrderTable.status]
                )
            }
            .sortedBy { it.date }
            .reversed()
    }


    suspend fun getAllLocations() = suspendTransaction {
        Location.all().map { LocationDto(it.id.value, it.name) }
    }

    suspend fun getCountCurrentOrders() = suspendTransaction {
        Order.count()
    }

    suspend fun cancelOrderById(uuid: UUID) = suspendTransaction {
        val order = Order.findById(uuid) ?: return@suspendTransaction false
        try {
            order.status = OrderStatus.CANCELED.toString()
        } catch (e: Exception) {
            return@suspendTransaction false
        }
        return@suspendTransaction true
    }

}