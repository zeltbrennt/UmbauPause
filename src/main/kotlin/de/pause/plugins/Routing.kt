package de.pause.plugins

import de.pause.model.Article
import de.pause.model.ArticleRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*

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
                val articles = articleRepository.getCurrentArticles().sortedBy { it.sortOrder }
                call.respond(ThymeleafContent("allArticles", mapOf("articles" to articles)))
            }
        }
        route("/add") {
            get {
                call.respond(ThymeleafContent("newArticle", emptyMap()))
            }
            post {
                val formParams = call.receiveParameters()
                val name = formParams["name"].toString()
                val available = formParams["available"].toBoolean()
                val scheduled = formParams["scheduled"].toString()
                val price = formParams["price"]!!.toDouble()
                articleRepository.addArticle(Article(name, available, scheduled, price))
                call.respondRedirect("/article/all")
            }
        }
    }
}
