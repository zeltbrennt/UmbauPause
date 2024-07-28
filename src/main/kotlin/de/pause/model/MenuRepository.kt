package de.pause.model

import de.pause.db.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.joda.time.DateTime

class MenuRepository {

    suspend fun getCurrentMenu(): MenuInfo = suspendTransaction {
        val today = DateTime.now()
        val validFrom: DateTime
        val validTo: DateTime
        val info = (MenuTable innerJoin DishTable innerJoin DayTable innerJoin WeekTable)
            .selectAll()
            .where { (WeekTable.weekStart lessEq today) and (WeekTable.weekEnd greaterEq today) }
            .orderBy(DayTable.id)
            .also {
                validFrom = it.first()[WeekTable.weekStart]
                validTo = it.first()[WeekTable.weekEnd]
            }
            .map {
                MenuItem(
                    id = it[MenuTable.id].value,
                    name = it[DishTable.description].toString(),
                    day = it[DayTable.name],
                )
            }
        MenuInfo(
            validFrom = validFrom.toString("yyyy-MM-dd"),
            validTo = validTo.toString("yyyy-MM-dd"),
            dishes = info
        )
    }
    /*
        suspend fun addNewMenu(menu: MenuDto): Boolean = suspendTransaction {
            MenuEntity.new {
                createdAt = DateTime.now()
                updatedAt = DateTime.now()
                weekId = WeekEntity[menu.weekId]
                dishId = DishEntity[menu.dishId]
                dayId = DayEntity[menu.dayId]
            }.id.value > 0
        }*/
}
