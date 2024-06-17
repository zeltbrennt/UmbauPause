package de.pause.plugins

import de.pause.model.Article
import de.pause.model.ArticleRepository
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import java.io.File

fun Application.configureRouting(articleRepository: ArticleRepository) {
    routing {
        //staticResources("/", "static/files", index = "index.html")
        route("/") {
            get {
                call.respond(ThymeleafContent("landingpage", emptyMap()))
            }
        }
        route("/article") {
            get("/all") {
                val articles = articleRepository.allArticles()
                call.respond(ThymeleafContent("allArticles", mapOf("articles" to articles) ))
            }
        }
        route("/add") {
            get {
                call.respond(ThymeleafContent("newArticle", emptyMap() ))
            }
            post {
                val formParams = call.receiveParameters()
                val name = formParams["name"].toString()
                val description = formParams["description"].toString()
                val type = formParams["type"].toString()
                val price = formParams["price"]?.toFloat() ?: 0F
                articleRepository.addArticle(Article(name, type, description, price))
                call.respondRedirect("/article/all")
            }
        }
    }
}
