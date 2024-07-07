package de.pause.model

import io.ktor.http.*

interface DishRepository {

    suspend fun allDishes(): List<Dish>
    suspend fun addDish(dish: Dish)
    suspend fun removeDish(id: Int): Boolean

    suspend fun getAvailableDishes(): List<Dish>
    suspend fun resetMenu()
    suspend fun addNewMenu(formParams: Parameters)
    /*
    -activate
    -deactivate
    ...
     */
}
