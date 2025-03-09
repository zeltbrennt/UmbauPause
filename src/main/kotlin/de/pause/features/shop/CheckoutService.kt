package de.pause.features.shop

import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import de.pause.features.shop.data.dto.OrderDto
import de.pause.features.shop.data.repo.DishPriceSingleton

object CheckoutService {

    init {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY")
    }

    fun createCheckoutLink(orderDto: OrderDto): Session {
        val paramsBuilder: SessionCreateParams.Builder = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:5173/success")
            .setCancelUrl("http://localhost:8080/cancel")
        orderDto.orders.forEach {
            paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("eur")
                            .setUnitAmount(DishPriceSingleton.defaultPrice.toLong())
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setDescription("${it.dayName},  ${it.locationName}")
                                    .setName(it.itemName)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
        }
        val params = paramsBuilder.build()
        val session = Session.create(params)
        return session

    }
}