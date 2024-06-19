package de.pause.plugins

import de.pause.getWeekDatesFollowing
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
                call.respond(
                    ThymeleafContent(
                        "newMenu",
                        mapOf("days" to listOf("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"))
                    )
                )
            }
            post {
                val formParams = call.receiveParameters()
                articleRepository.addNewMenu(formParams)
                call.respondRedirect("/weekly")
            }
        }
        route("/order") {
            post {
                call.respondRedirect("/")
            }
        }
    }
}
