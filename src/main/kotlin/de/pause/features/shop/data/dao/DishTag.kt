package de.pause.features.shop.data.dao

import org.jetbrains.exposed.sql.Table


object DishTagTable : Table("shop.dish_tag") {
    val dish = reference("dish_id", DishTable)
    val tag = reference("tag_id", TagTable)
    override val primaryKey = PrimaryKey(dish, tag)
}
