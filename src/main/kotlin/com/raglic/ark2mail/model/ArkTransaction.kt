package com.raglic.ark2mail.model

import io.ark.core.Transaction
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ArkTransaction(val transactionId: String,
                          @Transient val coreTransaction: Transaction,
                          private var _status: Status = Status.CREATED,
                          private var _statusModified: Long = 0,
                          @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null) {

    enum class Status {
        CREATED, SENT, FAILED, UNCONFIRMED, CONFIRMED
    }

    var status: Status = _status
        set(value) {
            if(field != value) {
                field = value
                updateStatusModified()
            }
        }

    var statusModified: Long = _statusModified
        private set

    fun updateStatusModified() {
        statusModified = System.currentTimeMillis()
    }

    fun millisSinceStatusModified(): Long {
        return System.currentTimeMillis() - statusModified
    }

}