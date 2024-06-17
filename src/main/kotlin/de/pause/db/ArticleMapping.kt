package de.pause.db

import de.pause.model.Article
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ArticleTable : IntIdTable("shop.article") {
    val name = varchar("name", 250)
    val available = bool("available")
    val scheduled = varchar("scheduled", 50)
    val price = double("price")
}

class ArticleDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ArticleDAO>(ArticleTable)

    var name by ArticleTable.name
    var available by ArticleTable.available
    var scheduled by ArticleTable.scheduled
    var price by ArticleTable.price
}

fun daoToModel(dao: ArticleDAO) = Article(
    name = dao.name,
    available = dao.available,
    scheduled = enumValueOf(dao.scheduled.uppercase()),
    price = dao.price
)