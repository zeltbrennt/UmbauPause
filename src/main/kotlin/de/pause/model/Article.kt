package de.pause.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table


@Serializable
data class Article(
    val id: Int,
    val name: String,
    val type: String,
    val description: String,
    val price: Float,
)

//DTO
@Serializable
data class ArticleDto(
    val name: String,
    val type: String?,
    val description: String?,
    val price: Float,
)


//table
object Articles : Table("shop.article") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val type = varchar("type", 100)
    val description = varchar("description", 250)
    val price = decimal("price", 5, 2)

}

