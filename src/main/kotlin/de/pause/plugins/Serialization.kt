package de.pause.plugins

import de.pause.model.ArticleRepository
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization(articleRepository: ArticleRepository) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    routing {
        route("/article") {
            get("/all") {
                val articles = articleRepository.allArticles()
                call.respond(articles)
            }
        }
    }
}

