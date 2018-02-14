package com.raglic.ark2mail.dao

import com.raglic.ark2mail.model.ArkForward

class WithdrawCompletedDao(val message: String,
                           val transactionId: String)
{
    constructor(forward: ArkForward) : this(
            "completed message",
            forward.transaction
                    ?.transactionId
                    ?: throw RuntimeException("Transaction missing"))
}