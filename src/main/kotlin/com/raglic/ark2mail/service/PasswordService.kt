package com.raglic.ark2mail.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class PasswordService @Autowired constructor(private val passwordEncoder: PasswordEncoder) {

    fun generatePassword(): String {
        return passwordEncoder.encode(UUID.randomUUID().toString())
    }

}