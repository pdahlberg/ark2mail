package com.raglic.ark2mail.dao

import com.raglic.ark2mail.model.ArkForward

data class WithdrawInfoDao(val message: String,
                      val acceptKey: String) {
    constructor(forward: ArkForward) : this(
            "test message",
            forward.acceptKey)
}