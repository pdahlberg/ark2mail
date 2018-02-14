package com.raglic.ark2mail.service

import com.raglic.ark2mail.model.ArkAmount
import com.raglic.ark2mail.model.Email
import com.raglic.ark2mail.model.Receiver
import com.raglic.ark2mail.model.UserText
import com.raglic.ark2mail.persistence.ReceiverRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class ReceiverService @Autowired constructor(private val receiverRepository: ReceiverRepository) {

    private val log = org.slf4j.LoggerFactory.getLogger(ReceiverService::class.java)

    fun getOrCreate(email: Email): Receiver {
        return receiverRepository.findByEmail(email) ?: receiverRepository.save(Receiver(email))
    }

}