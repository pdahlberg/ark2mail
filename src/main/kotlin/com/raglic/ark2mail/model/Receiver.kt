package com.raglic.ark2mail.model

import com.raglic.ark2mail.model.Email
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

// Email, SMS etc for receiver
// Include some other chat system?
// Deliver in order:
//   1) ArkAddress
//   2) Email
//   3) SMS
@Entity
data class Receiver(val email: Email,
                    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null)
