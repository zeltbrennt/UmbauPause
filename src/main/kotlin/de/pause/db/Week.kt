package de.pause.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.jodatime.date

object WeekTable : IntIdTable("shop.week") {
    val weekStart = date("week_start")
    val weekEnd = date("week_end")
    val kw = integer("kw")
    val year = integer("year")
    val quarter = integer("quarter")
}

class WeekEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WeekEntity>(WeekTable)

    var weekStart by WeekTable.weekStart
    var weekEnd by WeekTable.weekEnd
    var kw by WeekTable.kw
    var year by WeekTable.year
    var quarter by WeekTable.quarter

}

suspend fun getCurrentWeek() = suspendTransaction {
    val today = org.joda.time.DateTime.now()
    WeekEntity.find {
        (WeekTable.weekStart lessEq today) and (WeekTable.weekEnd greaterEq today)
    }.first().id.value
}