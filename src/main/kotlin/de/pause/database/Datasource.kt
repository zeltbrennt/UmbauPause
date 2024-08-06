package de.pause.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


fun Application.configureDatabase(appConfig: HoconApplicationConfig) {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = appConfig.property("ktor.datasource.jdbcUrl").getString()
        driverClassName = appConfig.property("ktor.datasource.driverClassName").getString()
        username = appConfig.property("ktor.datasource.username").getString()
        password = appConfig.property("ktor.datasource.password").getString()
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
    val dataSource = HikariDataSource(hikariConfig)
    Database.connect(dataSource)
    Flyway.configure().dataSource(dataSource).load().migrate()
}


suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)
