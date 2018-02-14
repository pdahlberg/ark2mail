package com.raglic.ark2mail.service

import com.raglic.ark2mail.model.ArkAmount
import com.raglic.ark2mail.model.Email
import com.raglic.ark2mail.model.UserText
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class EmailService @Autowired constructor(private val mailSender: JavaMailSender) {

    private val log = org.slf4j.LoggerFactory.getLogger(EmailService::class.java)

    // todo: PoC, to be a full template
    @Async
    fun send(senderName: UserText, receiver: Email, amount: ArkAmount, acceptUrl: String) {
        try {
            val message = SimpleMailMessage()
            message.from = "sender@ark2mail.com"
            message.setTo(receiver.address)
            message.subject = "ARK delivery"
            message.text = "${senderName.text} have sent ${amount.getHumanReadable()} ARK which you can get at $acceptUrl"
            mailSender.send(message)
        } catch (e: Exception) {
            log.error("error", e)
        }
    }

}