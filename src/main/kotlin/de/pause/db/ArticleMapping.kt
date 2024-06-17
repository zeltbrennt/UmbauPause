package de.pause.db

import de.pause.model.Article
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ArticleTable : IntIdTable("shop.article") {
    val name = varchar("name", 100)
    val type = varchar("type", 100)
    val description = varchar("description", 250)
    val price = decimal("price", 5, 2)
}

class ArticleDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ArticleDAO>(ArticleTable)

    var type by ArticleTable.type
    var name by ArticleTable.name
    var description by ArticleTable.description
    var price by ArticleTable.price
}

fun daoToModel(dao: ArticleDAO) = Article(
    type = dao.type,
    name = dao.name,
    description = dao.description,
    price = dao.price.toFloat()
)