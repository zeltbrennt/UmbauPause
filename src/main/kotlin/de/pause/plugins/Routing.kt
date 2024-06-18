package de.pause.plugins

import de.pause.getWeekDatesFollowing
import de.pause.model.Article
import de.pause.model.ArticleRepository
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import java.time.LocalDate


fun Application.configureRouting(articleRepository: ArticleRepository) {

    routing {
        staticResources("/static", "static")
        route("/") {
            get {
                call.respond(ThymeleafContent("landingpage", emptyMap()))
            }
        }
        route("/weekly") {
            get {
                val articles = articleRepository.getCurrentArticles().sortedBy { it.order }
                call.respond(
                    ThymeleafContent(
                        "allArticles", mapOf(
                            "articles" to articles,
                            "week" to getWeekDatesFollowing(LocalDate.now())
                        )
                    )
                )
            }
        }
        route("/newMenu") {
            get {
                call.respond(ThymeleafContent("newMenu", emptyMap()))
            }
            post {
                val formParams = call.receiveParameters()
                articleRepository.resetMenu()
                for (day in listOf("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag")) {
                    articleRepository.addArticle(
                        Article(
                            name = formParams[day].toString(),
                            available = true,
                            scheduled = day,
                            price = 6.50
                        )
                    )
                }
                call.respondRedirect("/weekly")
            }
        }
    }
}
