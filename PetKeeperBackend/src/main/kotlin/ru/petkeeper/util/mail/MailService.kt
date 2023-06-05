package ru.petkeeper.util.mail

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object MailService {

    const val username = "morphylix@gmail.com" // DANGEROUS
    const val password = "bjmloygqgjgmghvi"  // DANGEROUS

    private fun createSessionObject(): Session {
        val properties = Properties()
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = "587"

        return Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
    }

    private fun createMessage(email: String, subject: String, messageBody: String, session: Session): Message {
        val message = MimeMessage(session)
        message.setFrom(InternetAddress("morphylix@gmail.com", "Dmitry Borovik"))
        message.addRecipient(Message.RecipientType.TO, InternetAddress(email, email))
        message.subject = subject
        message.setText(messageBody)
        return message
    }

    fun sendMail(email: String, subject: String, messageBody: String) {
        val session = createSessionObject()

        try {
            val message = createMessage(email, subject, messageBody, session)
            Transport.send(message)
        } catch (e: Exception) {
            print(e.toString())
        }
    }
}