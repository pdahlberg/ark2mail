package com.raglic.ark2mail.model

import javax.persistence.*

@Entity
data class ArkForward(val senderName: UserText,
                      @OneToOne(fetch = FetchType.EAGER) @JoinColumn(name = "return_address_id") val returnAddress: ArkAddress,
                      @OneToOne(fetch = FetchType.EAGER) @JoinColumn(name = "receiver_id") val receiver: Receiver,
                      val receiverAmount: ArkAmount,
                      val feeAmount: ArkAmount,
                      val totalAmount: ArkAmount,
                      @OneToOne(fetch = FetchType.EAGER) @JoinColumn(name = "deposit_address_id") val deposit: ArkAddress,
                      val cancelPassword: String,
                      val acceptKey: String,
                      @OneToOne(fetch = FetchType.EAGER) @JoinColumn(name = "transaction_id") var transaction: ArkTransaction? = null,
                      var status: Status = Status.CREATED,
                      @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
        // val trackUntil: Long // Make sure we can remove spam, i.e. a timeout until we ignore the address
        // val trackFrom: Long // Pay: Send the link after this time
) // Sign message and send to /cancel?
{
    enum class Status {
        CREATED, DEPOSITED, ACCEPTED, WITHDRAW_REQUESTED, WITHDRAWN
    }

}

