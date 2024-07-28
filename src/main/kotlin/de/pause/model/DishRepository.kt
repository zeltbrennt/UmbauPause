package de.pause.model


import de.pause.db.DishEntity
import de.pause.db.DishTable
import de.pause.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class DishRepository {
    suspend fun allDishes(): List<DishDto> = suspendTransaction {
        DishEntity.all().map { it.toDishDto() }
    }

    suspend fun addDish(dish: DishDto): Unit = suspendTransaction {
        DishEntity.new {
            description = dish.description
        }
    }

    suspend fun removeDish(dish: DishDto) = suspendTransaction {
        val rowsDeleted = DishTable.deleteWhere {
            DishTable.id eq dish.id
        }
        return@suspendTransaction rowsDeleted == 1
    }

}

