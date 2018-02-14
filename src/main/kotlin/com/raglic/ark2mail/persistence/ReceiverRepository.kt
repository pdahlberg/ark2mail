package com.raglic.ark2mail.persistence

import com.raglic.ark2mail.model.ArkAddress
import com.raglic.ark2mail.model.Email
import com.raglic.ark2mail.model.Receiver
import org.springframework.data.jpa.repository.JpaRepository
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface ReceiverRepository : JpaRepository<Receiver, Long> {

    fun findByEmail(email: Email): Receiver?

}