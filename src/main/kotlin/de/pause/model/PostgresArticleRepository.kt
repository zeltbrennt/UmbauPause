package de.pause.model

import de.pause.db.ArticleDAO
import de.pause.db.ArticleTable
import de.pause.db.daoToModel
import de.pause.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update

class PostgresArticleRepository : ArticleRepository {
    override suspend fun allArticles(): List<Article> = suspendTransaction {
        ArticleDAO.all().map(::daoToModel)
    }

    override suspend fun addArticle(article: Article): Unit = suspendTransaction {
        ArticleDAO.new {
            name = article.name
            available = article.available
            scheduled = article.scheduled.toString()
            price = article.price
        }
    }

    override suspend fun removeArticle(id: Int) = suspendTransaction {
        val rowsDeleted = ArticleTable.deleteWhere {
            ArticleTable.id eq id
        }
        return@suspendTransaction rowsDeleted == 1
    }

    override suspend fun getCurrentArticles(): List<Article> = suspendTransaction {
        ArticleDAO.find(ArticleTable.available eq true).map(::daoToModel)
    }

    override suspend fun resetMenu(): Unit = suspendTransaction {
        ArticleTable.update { it[available] = false }
    }
}

