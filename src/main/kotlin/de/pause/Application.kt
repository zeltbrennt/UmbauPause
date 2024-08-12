package de.pause

import com.typesafe.config.ConfigFactory
import de.pause.database.configureDatabase
import de.pause.features.shop.data.repo.DishRepository
import de.pause.features.shop.data.repo.MenuRepository
import de.pause.features.shop.data.repo.OrderRepository
import de.pause.features.user.data.repo.UserRepository
import de.pause.plugins.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.launch

fun main() {
    val appConfig = HoconApplicationConfig(ConfigFactory.load())
    val port = appConfig.property("ktor.deployment.port").getString().toInt()
    embeddedServer(
        Netty,
        port = port,
        host = "0.0.0.0",
        module = { module(appConfig) }
    )
        .start(wait = true)
}

fun Application.module(appConfig: HoconApplicationConfig) {
    val articleRepository = DishRepository()
    val userRepository = UserRepository()
    val menuRepository = MenuRepository()
    val orderRepository = OrderRepository()
    configureDatabase(appConfig)
    launch {
        userRepository.setDefaultPasswordOfPreloadedUsers()
    }
    configureHTTP()
    configureSerialization()
    configureSecurity(appConfig)
    configureCORS()
    configureWebsockets()
    configureRouting(articleRepository, userRepository, menuRepository, orderRepository)
    configureValidation()
}
