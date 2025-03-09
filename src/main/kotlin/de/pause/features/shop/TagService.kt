package de.pause.features.shop

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.runBlocking

object TagService {

    private val openAI = OpenAI(token = System.getenv("OPENAI_API_KEY"))
    private val systemMessage = ChatMessage(
        role = ChatRole.System,
        content = """You are a helpful assistant. You are helping a user generate tags for a dish.
                    The dish names are in german.
                    The tags should contain the primary ingredients for a dish. 
                    For example, if the dish is 'cremige Polenta mit gebratenen Champignons und Tomaten-Mangold', you should respond with 'Polenta, Champignon, Tomate, Mangold'.
                    Each ingredient should be singular and in title case.
                    Compound words should be split, for example 'Tomatentofu' becomes 'Tomate Tofu'. 
                    Common names should be used as is, for example 'Reisnudeln' should not become 'Reis Nudel'.
                    The response should be a comma-separated list of ingredients.
                    """
    )

    fun generateTagsForDish(dishName: String): List<String> {

        if (dishName.isBlank()) {
            return emptyList()
        }

        val userMessage = ChatMessage(
            role = ChatRole.User,
            content = dishName
        )

        val chatCompleteRequest = ChatCompletionRequest(
            model = ModelId("gpt-4o-mini"),
            messages = listOf(
                systemMessage,
                userMessage
            ),
            temperature = 0.0,
        )

        return runBlocking {
            val completion: ChatCompletion = openAI.chatCompletion(chatCompleteRequest)
            val message = completion.choices.first().message
            message.content?.split(",")?.map { it.trim() } ?: emptyList()
        }
    }
}