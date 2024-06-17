package de.pause.model

import de.pause.db.ArticleDAO
import de.pause.db.ArticleTable
import de.pause.db.daoToModel
import de.pause.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import java.math.BigDecimal

class PostgresArticleRepository : ArticleRepository {
    override suspend fun allArticles(): List<Article> = suspendTransaction {
        ArticleDAO.all().map(::daoToModel)
    }

    override suspend fun addArticle(article: Article): Unit = suspendTransaction {
        ArticleDAO.new {
            type = article.type
            name = article.name
            description = article.description
            price = BigDecimal(article.price.toDouble())
        }
    }

    override suspend fun removeArticle(id: Int) = suspendTransaction {
        val rowsDeleted = ArticleTable.deleteWhere {
            ArticleTable.id eq id
        }
        return@suspendTransaction rowsDeleted == 1
    }
}

