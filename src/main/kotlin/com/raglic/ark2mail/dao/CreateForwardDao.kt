package com.raglic.ark2mail.dao

data class CreateForwardDao(
        val senderName: String?,
        val returnAddress: String?,
        val receiver: String,
        val amount: Double
        //val timeout: Long, // IAP?
        )