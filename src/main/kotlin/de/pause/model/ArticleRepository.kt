package de.pause.model

import io.ktor.http.*

interface ArticleRepository {

    suspend fun allArticles(): List<Article>
    suspend fun addArticle(article: Article)
    suspend fun removeArticle(id: Int): Boolean

    suspend fun getCurrentArticles(): List<Article>
    suspend fun resetMenu()
    suspend fun addNewMenu(formParams: Parameters)
    /*
    -activate
    -deactivate
    ...
     */
}
