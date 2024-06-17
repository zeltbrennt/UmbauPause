package de.pause.plugins

import de.pause.model.Article
import de.pause.model.ArticleRepository
import de.pause.model.Weekday
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
        route("/weekly") {
            get {
                val articles = articleRepository.getCurrentArticles().sortedBy { it.scheduled.ordinal }
                call.respond(ThymeleafContent("allArticles", mapOf("articles" to articles)))
            }
        }
        route("/add") {
            get {
                call.respond(ThymeleafContent("newArticle", emptyMap()))
            }
            post {
                // TODO: fix this!
                val formParams = call.receiveParameters()
                val name = formParams["name"].toString()
                val available = formParams["available"].toBoolean()
                val scheduled = formParams["scheduled"]!!.toInt()
                val price = formParams["price"]!!.toDouble()
                articleRepository.addArticle(Article(name, available, Weekday.MONTAG, price))
                call.respondRedirect("/article/all")
            }
        }
    }
}
