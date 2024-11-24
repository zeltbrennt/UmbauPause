package de.pause.features.shop.data.repo

import kotlinx.coroutines.runBlocking

object DishPriceSingleton {

    private val repo = DishRepository()

    //default price of meal in cents
    var defaultPrice = runBlocking { repo.getDefaultPrice() }
        set(newPrice) {
            if (newPrice > 0) {
                field = newPrice;
                runBlocking {
                    repo.updateDefaultPrice(newPrice)
                }
            }
        }
}