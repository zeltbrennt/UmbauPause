package de.pause.plugins

import de.pause.features.shop.data.dto.MenuInfo
import de.pause.features.shop.data.dto.OrderDto
import de.pause.features.user.data.dto.RegisterRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import org.joda.time.DateTime

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<RegisterRequest> {
            val passwortRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}\$")
            when {
                it.email.isBlank() || it.password.isBlank() -> ValidationResult.Invalid("Email or password is missing")
                passwortRegex.matches(it.password).not() -> ValidationResult.Invalid("Password not complex enough")
                else -> ValidationResult.Valid
            }
        }
        validate<MenuInfo> {
            try {
                val validFromDate = DateTime(it.validFrom)
                val validToDate = DateTime(it.validTo)
                when {
                    it.dishes
                        .map { dish -> dish.day }
                        .sorted() != IntRange(1, 5)
                        .toList() -> ValidationResult.Invalid("days must be distinct days of week")

                    validToDate.isBefore(validFromDate) -> ValidationResult.Invalid("validTo cannot be before validFrom")
                    validFromDate.isBeforeNow -> ValidationResult.Invalid("validFrom cannot be in the past")
                    else -> ValidationResult.Valid
                }
            } catch (e: IllegalArgumentException) {
                ValidationResult.Invalid("validFrom or validTo is not a valid date")
            }
        }
        validate<OrderDto> {
            try {
                val validFromDate = DateTime(it.validFrom)
                val validToDate = DateTime(it.validTo)
                when {
                    it.orders.isEmpty() -> ValidationResult.Invalid("orders cannot be empty")
                    validToDate.isBefore(validFromDate) -> ValidationResult.Invalid("validTo cannot be before validFrom")
                    validToDate.isBeforeNow -> ValidationResult.Invalid("validTo cannot be in the past")
                    else -> {
                        it.orders.forEach { order ->
                            if (validFromDate.plusDays(order.day - 1).withTime(10, 30, 0, 0).isBeforeNow) {
                                return@validate ValidationResult.Invalid("order cannot be placed after 10:30")
                            }
                        }
                        ValidationResult.Valid
                    }
                }
            } catch (e: IllegalArgumentException) {
                ValidationResult.Invalid("validFrom or validTo is not a valid date")
            }
        }
        validate<String> {
            when {
                it.isBlank() -> ValidationResult.Invalid("cannot be blank")
                else -> ValidationResult.Valid
            }
        }
    }
}