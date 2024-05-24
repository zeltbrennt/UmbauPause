package de.pause.plugins

import de.pause.model.ArticleDto
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<ArticleDto> { article ->
            if (article.name.startsWith("A")) {
                ValidationResult.Valid
            } else ValidationResult.Invalid("name does not start with 'A'")
        }
    }
}