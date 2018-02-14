package com.raglic.ark2mail.persistence

import com.raglic.ark2mail.model.ArkForward
import org.springframework.data.jpa.repository.JpaRepository
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface ArkForwardRepository : JpaRepository<ArkForward, Long> {

    fun findByStatus(status: ArkForward.Status): List<ArkForward>

    fun findByAcceptKeyAndStatusIn(acceptKey: String, statuses: List<ArkForward.Status>): ArkForward

}