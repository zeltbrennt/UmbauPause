package de.pause.features.shop.data.repo


import de.pause.database.suspendTransaction
import de.pause.features.shop.data.dao.Dish
import de.pause.features.shop.data.dao.DishPriceTable
import de.pause.features.shop.data.dao.DishTable
import de.pause.features.shop.data.dto.DishDto
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime

class DishRepository {
    suspend fun allDishes(): List<String> = suspendTransaction {
        Dish.all().map { it.description }.sorted()
    }

    suspend fun findByName(name: String): Dish = suspendTransaction {
        Dish.find { DishTable.description eq name }.first()
    }

    suspend fun addDishByName(dish: String) = suspendTransaction {

        Dish.new {
            description = dish
            createdAt = DateTime.now()
            updatedAt = DateTime.now()
        }

    }

    suspend fun removeDish(dish: DishDto) = suspendTransaction {
        val rowsDeleted = DishTable.deleteWhere {
            DishTable.id eq dish.id
        }
        return@suspendTransaction rowsDeleted == 1
    }

    suspend fun updateDefaultPrice(newPrice: Int) = suspendTransaction {
        DishPriceTable.update({ DishPriceTable.type eq "default" }) {
            it[price] = newPrice

        }
    }

    suspend fun getDefaultPrice(): Int = suspendTransaction {
        DishPriceTable
            .select(DishPriceTable.price)
            .where { DishPriceTable.type eq "default" }
            .map { it[DishPriceTable.price] }
            .single()
    }

}

