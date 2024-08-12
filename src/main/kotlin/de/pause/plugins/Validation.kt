package de.pause.plugins

import de.pause.features.user.data.dto.RegisterRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<RegisterRequest> {
            if (it.email.isBlank() || it.password.isBlank()) {
                ValidationResult.Invalid("Email or password is missing")
            } else if (Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")
                    .matches(it.password).not()
            ) {
                ValidationResult.Invalid("Password not complex enough")
            } else {
                ValidationResult.Valid
            }
        }
    }
}