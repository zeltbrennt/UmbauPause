package de.pause.plugins

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    routing {
        route("/chatgpt") {
            get {
                val client = HttpClient(CIO)
                val body = this::class.java.classLoader.getResource("chat_gpt_prompt.json").readText()
                val response = client.post("https://api.openai.com/v1/chat/completions") {
                    headers {
                        append(HttpHeaders.ContentType, "application/json")
                        append(HttpHeaders.Authorization, "Bearer ${System.getenv("OPENAI_API_KEY")}")
                        append("OpenAI-Organization", System.getenv("OPENAI_ORGANIZATION_ID"))
                        append("OpenAI-Project", System.getenv("OPENAI_PROJECT_ID"))
                    }
                    setBody(body)
                }

                call.respondText { response.body() }
                client.close()
            }
        }
    }

}

