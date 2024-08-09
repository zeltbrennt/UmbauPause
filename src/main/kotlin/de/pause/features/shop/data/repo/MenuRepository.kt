package de.pause.features.shop.data.repo

import de.pause.database.suspendTransaction
import de.pause.features.shop.data.dao.Dish
import de.pause.features.shop.data.dao.Menu
import de.pause.features.shop.data.dao.MenuTable
import de.pause.features.shop.data.dto.MenuInfo
import de.pause.features.shop.data.dto.MenuItem

import org.jetbrains.exposed.sql.and
import org.joda.time.DateTime

class MenuRepository {

    suspend fun getScheduledMenuFrom(day: DateTime): MenuInfo? = suspendTransaction {
        val validFrom: DateTime
        val validTo: DateTime
        val info = Menu
            .find { (MenuTable.validFrom lessEq day) and (MenuTable.validTo greaterEq day) }
            .sortedBy { it.dayOfWeek }
        if (info.isEmpty()) {
            return@suspendTransaction null
        } else {
            validTo = info.first().validTo
            validFrom = info.first().validFrom
        }
        MenuInfo(
            validFrom = validFrom.toString("yyyy-MM-dd"),
            validTo = validTo.toString("yyyy-MM-dd"),
            dishes = info.map {
                MenuItem(
                    id = it.id.value,
                    name = it.dishId.description,
                    day = it.dayOfWeek,
                )
            }
        )
    }

    suspend fun addNewMenu(validFrom: String, validTo: String, dayOfWeek: Int, dishId: Int) = suspendTransaction {
        Menu.new {
            createdAt = DateTime.now()
            updatedAt = DateTime.now()
            this.validFrom = DateTime.parse(validFrom)
            this.validTo = DateTime.parse(validTo)
            this.dayOfWeek = dayOfWeek
            this.dishId = Dish[dishId]
        }
    }
}
