package de.pause.routing

import de.pause.dao.ArticleDao
import de.pause.model.ArticleDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.article(dao: ArticleDao) {
    route("/article") {
        get {
            call.respond(dao.allArticles())
        }
        post {
            try {
                val dto = call.receive<ArticleDto>()
                val inserted = dao.addNewArticle(dto.name ?: "", dto.type ?: "", dto.description ?: "", dto.price ?: 0F)
                call.respondNullable(inserted)
            } catch (v: RequestValidationException) {
                call.respond(HttpStatusCode.BadRequest, "Input validation failed")
            }
        }
    }
}