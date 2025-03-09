package de.pause.features.app.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime.now

object FeedbackTable : IntIdTable("app.feedback") {
    val createdAt = datetime("created_at").default(now())
    val author = varchar("author", 100)
    val feedback = text("feedback")
}

class Feedback(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Feedback>(FeedbackTable)

    var createdAt by FeedbackTable.createdAt
    var author by FeedbackTable.author
    var feedback by FeedbackTable.feedback
}