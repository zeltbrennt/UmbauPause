package de.pause.dao

import de.pause.model.Article
import de.pause.model.Articles
import de.pause.model.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ArticleDao : IArticleDao {

    private fun resultRowToArticle(row: ResultRow) = Article(
        id = row[Articles.id],
        name = row[Articles.name],
        type = row[Articles.type],
        description = row[Articles.description],
        price = row[Articles.price].toFloat(),
    )

    override suspend fun allArticles(): List<Article> = dbQuery {
        Articles.selectAll().map(::resultRowToArticle)
    }

    override suspend fun article(id: Int): Article? = dbQuery {
        Articles.select { Articles.id eq id }.map(::resultRowToArticle).singleOrNull()
    }

    override suspend fun addNewArticle(
        name: String,
        type: String,
        description: String,
        price: Float,
    ): Article? = dbQuery {
        val insertStatement = Articles.insert {
            it[Articles.name] = name
            it[Articles.type] = type
            it[Articles.description] = description
            it[Articles.price] = price.toBigDecimal()
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
    }

    override suspend fun editArticle(
        id: Int,
        name: String,
        type: String,
        description: String,
        price: Float,
    ): Boolean = dbQuery {
        Articles.update({ Articles.id eq id }) {
            it[Articles.name] = name
            it[Articles.type] = type
            it[Articles.description] = description
            it[Articles.price] = price.toBigDecimal()
        } > 0
    }


    override suspend fun deleteArticle(id: Int): Boolean = dbQuery {
        Articles.deleteWhere { Articles.id eq id } > 0
    }
}