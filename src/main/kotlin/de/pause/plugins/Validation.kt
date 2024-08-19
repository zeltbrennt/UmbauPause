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
            ValidationResult.Valid
        }
        validate<OrderDto> {
            when {
                it.orders.isEmpty() -> ValidationResult.Invalid("orders cannot be empty")
                it.validFrom.isBlank() || it.validTo.isBlank() -> ValidationResult.Invalid("validFrom or validTo cannot be blank")
                it.validTo <= it.validFrom -> ValidationResult.Invalid("validTo cannot be before validFrom")
                DateTime(it.validTo) < DateTime.now() -> ValidationResult.Invalid("validTo cannot be in the past")
                DateTime(it.validTo) == DateTime.now() && DateTime.now().isAfter(
                    DateTime.now().withTime(10, 30, 0, 0)
                ) -> ValidationResult.Invalid("validTo cannot be in the past")

                else -> ValidationResult.Valid
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