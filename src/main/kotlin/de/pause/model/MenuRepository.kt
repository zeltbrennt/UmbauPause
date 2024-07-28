package de.pause.model

import de.pause.db.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.joda.time.DateTime

class MenuRepository {

    suspend fun getCurrentMenu() = suspendTransaction {
        val today = DateTime.now()
        (MenuTable innerJoin DishTable innerJoin DayTable innerJoin WeekTable)
            .selectAll()
            .where { (WeekTable.weekStart lessEq today) and (WeekTable.weekEnd greaterEq today) }
            .map {
                MenuInfo(
                    menuId = it[MenuTable.id].value,
                    validFrom = it[WeekTable.weekStart].toString(),
                    validTo = it[WeekTable.weekEnd].toString(),
                    dish = it[DishTable.description].toString(),
                    day = it[DayTable.id].value,
                )

            }

    }

    suspend fun addNewMenu(menu: MenuDto): Boolean = suspendTransaction {
        MenuEntity.new {
            createdAt = DateTime.now()
            updatedAt = DateTime.now()
            weekId = WeekEntity[menu.weekId]
            dishId = DishEntity[menu.dishId]
            dayId = DayEntity[menu.dayId]
        }.id.value > 0
    }
}
