package com.raglic.ark2mail.dao

import com.raglic.ark2mail.model.ArkForward

data class ForwardCreatedDao(
        val sender: String,
        val receiver: String,
        val receiverAmount: Double,
        val feeAmount: Double,
        val totalAmount: Double,
        val depositAddress: String,
        val cancelPassword: String
        //val timeout: Long, // IAP?
) {
    constructor(forward: ArkForward) : this(
            forward.returnAddress.address,
            forward.receiver.email.address,
            forward.receiverAmount.getHumanReadable(),
            forward.feeAmount.getHumanReadable(),
            forward.totalAmount.getHumanReadable(),
            forward.deposit.address,
            forward.cancelPassword)
}