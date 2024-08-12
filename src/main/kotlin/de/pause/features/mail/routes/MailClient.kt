package de.pause.features.mail.routes

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

object NoReplyEmailClient {
    private val appConfig = HoconApplicationConfig(ConfigFactory.load())

    private val hostName = appConfig.property("ktor.email.hostName").getString()
    private val smtpPort = 465
    private val username = appConfig.property("ktor.email.username").getString()
    private val password = appConfig.property("ktor.email.password").getString()
    private val from = appConfig.property("ktor.email.from").getString()
    private val domain = appConfig.property("ktor.server.domain").getString()

    private val validationMailText = """
    Willkommen bei beim Online-Bestellsystem der Kantine im DNT!
    
    klicke auf folgenden Link, um deine Registrierung abzuschließen:
    
    ${domain}/user/verify?id=%s
    
    (falls du dich nicht registriert hast, kannst du diese E-Mail ignorieren)
    
    Viel Spaß beim Bestellen!
    
    Dein Pause-Team
""".trimIndent()

    fun sendVerificationMail(to: String, id: String) {
        val email = SimpleEmail()
        email.hostName = hostName
        email.setSmtpPort(smtpPort)
        email.setAuthenticator(DefaultAuthenticator(username, password))
        email.isSSLOnConnect = true
        email.setFrom(from)
        email.subject = "Anmeldung abschließen"
        email.setMsg(validationMailText.format(id))
        email.addTo(to)
        email.send()
    }
}
