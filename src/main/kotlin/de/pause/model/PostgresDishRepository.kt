package de.pause.model

import de.pause.db.DishDao
import de.pause.db.DishTable
import de.pause.db.daoToModel
import de.pause.db.suspendTransaction
import io.ktor.http.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update

class PostgresDishRepository : DishRepository {
    override suspend fun allDishes(): List<Dish> = suspendTransaction {
        DishDao.all().map(::daoToModel)
    }

    override suspend fun addDish(dish: Dish): Unit = suspendTransaction {
        DishDao.new {
            description = dish.description
            available = dish.available
            scheduled = dish.scheduled
            price = dish.price
        }
    }

    override suspend fun removeDish(id: Int) = suspendTransaction {
        val rowsDeleted = DishTable.deleteWhere {
            DishTable.id eq id
        }
        return@suspendTransaction rowsDeleted == 1
    }

    override suspend fun getAvailableDishes(): List<Dish> = suspendTransaction {
        DishDao.find(DishTable.available eq true).map(::daoToModel)
    }

    override suspend fun resetMenu(): Unit = suspendTransaction {
        DishTable.update { it[available] = false }
    }

    override suspend fun addNewMenu(formParams: Parameters) {
        resetMenu()
        for (day in listOf("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag")) {
            val newDishName = formParams[day].toString()
            addDish(
                Dish(
                    description = newDishName.ifBlank { "Kantine geschlossen" },
                    available = true,
                    scheduled = day,
                    price = 6.50
                )
            )
        }
    }
}

