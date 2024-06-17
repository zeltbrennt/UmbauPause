package de.pause.plugins

import de.pause.model.Article
import de.pause.model.ArticleRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*

fun Application.configureRouting(articleRepository: ArticleRepository) {
    routing {
        route("/") {
            get {
                val allArticles: List<Article> = articleRepository.allArticles()
                call.respond(ThymeleafContent("index", mapOf("articles" to allArticles)))
            }
        }
        route("/article") {
            get("/all") {
                val articles = articleRepository.allArticles()
                call.respond(articles)
            }
        }
        route("/add") {
            get {
                call.respond(ThymeleafContent("newArticle", emptyMap() ))
            }
        }
    }
}
