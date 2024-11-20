package de.pause.features.shop

import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import de.pause.features.shop.data.dto.OrderDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object CheckoutService {

    init {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY")
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun createCheckoutLink(orderDto: OrderDto): String {
        val orderObject = Json.encodeToString(orderDto)
        val paramsBuilder: SessionCreateParams.Builder = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(
                "http://localhost:5173/success?orders=${ // TODO: parametrize port
                    Base64.UrlSafe.encode(
                        orderObject.toByteArray()
                    )
                }"
            )
            .setCancelUrl("http://localhost:8080/cancel")
        orderDto.orders.forEach {
            paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("eur")
                            .setUnitAmount(690) // TODO: parametrisieren
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
        return session.url

    }
}