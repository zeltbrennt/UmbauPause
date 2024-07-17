package de.pause.db

import de.pause.model.Menu
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.date
import org.joda.time.format.DateTimeFormat


object MenuTable : IntIdTable("shop.weekly_menu") {
    val monday = varchar("monday", 200)
    val tuesday = varchar("tuesday", 200)
    val wednesday = varchar("wednesday", 200)
    val thursday = varchar("thursday", 200)
    val friday = varchar("friday", 200)
    val validFrom = date("valid_from")
    val validTo = date("valid_to")
}

class MenuDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MenuDao>(MenuTable)

    var monday by MenuTable.monday
    var tuesday by MenuTable.tuesday
    var wednesday by MenuTable.wednesday
    var thursday by MenuTable.thursday
    var friday by MenuTable.friday
    var validFrom by MenuTable.validFrom
    var validTo by MenuTable.validTo


}

fun daoToModel(dao: MenuDao): Menu {
    val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")
    return Menu(
        monday = dao.monday,
        tuesday = dao.tuesday,
        wednesday = dao.wednesday,
        thursday = dao.thursday,
        friday = dao.friday,
        validFrom = formatter.print(dao.validFrom),
        validTo = formatter.print(dao.validTo)
    )
}
