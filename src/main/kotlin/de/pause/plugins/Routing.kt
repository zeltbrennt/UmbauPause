package de.pause.plugins

import de.pause.dao.ArticleDao
import de.pause.routing.article
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        article(ArticleDao())
    }
}
