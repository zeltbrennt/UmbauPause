package de.pause.features.mail.routes

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

object NoReplyEmailClient {
    private val appConfig = HoconApplicationConfig(ConfigFactory.load())

    val hostName = appConfig.property("ktor.email.hostName").getString()
    val smtpPort = 465
    val username = appConfig.property("ktor.email.username").getString()
    val password = appConfig.property("ktor.email.password").getString()
    val from = appConfig.property("ktor.email.from").getString()

    fun sendTestMail(to: String) {
        sendVerificationMail(to, "test1234")
    }

    fun sendVerificationMail(to: String, id: String) {
        val email = SimpleEmail()
        email.hostName = hostName
        email.setSmtpPort(smtpPort)
        email.setAuthenticator(DefaultAuthenticator(username, password))
        email.isSSLOnConnect = true
        email.setFrom(from)
        email.subject = "testMail"
        email.setMsg("Das ist ein Test <br><a href='http://localhost:8080/verify/$id'>Verify</a>")
        email.addTo(to)
        email.send()
    }
}
