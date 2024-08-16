package de.pause.features.app.data

import de.pause.database.suspendTransaction

class AppRepository {
    suspend fun submitFeedback(author: String, feedback: String) = suspendTransaction {
        Feedback.new {
            this.author = author
            this.feedback = feedback
        }.id.value > 0
    }
}