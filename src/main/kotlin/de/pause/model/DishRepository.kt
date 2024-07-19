package de.pause.model

import de.pause.db.DishDao
import de.pause.db.DishTable
import de.pause.db.daoToModel
import de.pause.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class DishRepository {
    suspend fun allDishes(): List<Dish> = suspendTransaction {
        DishDao.all().map(::daoToModel)
    }

    suspend fun addDish(dish: Dish): Unit = suspendTransaction {
        DishDao.new {
            description = dish.description
            price = dish.price
        }
    }

    suspend fun removeDish(id: Int) = suspendTransaction {
        val rowsDeleted = DishTable.deleteWhere {
            DishTable.id eq id
        }
        return@suspendTransaction rowsDeleted == 1
    }

}

