package com.raglic.ark2mail.persistence

import com.raglic.ark2mail.model.ArkAddress
import org.springframework.data.jpa.repository.JpaRepository
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface ArkAddressRepository : JpaRepository<ArkAddress, Long> {

    fun findByAddress(address: String): ArkAddress?

}