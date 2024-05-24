package de.pause.dao

import de.pause.model.Article


interface IArticleDao {
    suspend fun allArticles(): List<Article>
    suspend fun article(id: Int): Article?
    suspend fun addNewArticle(name: String, type: String, description: String, price: Float): Article?
    suspend fun editArticle(
        id: Int,
        name: String,
        type: String,
        description: String,
        price: Float,
    ): Boolean

    suspend fun deleteArticle(id: Int): Boolean
}