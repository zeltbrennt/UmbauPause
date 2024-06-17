package de.pause.model

interface ArticleRepository {

    suspend fun allArticles(): List<Article>
    suspend fun addArticle(article: Article)
    suspend fun removeArticle(id: Int): Boolean
    /*
    -activate
    -deactivate
    ...
     */
}
