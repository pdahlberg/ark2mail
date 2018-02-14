package com.raglic.ark2mail.model

import java.util.regex.Pattern
import javax.persistence.AttributeConverter
import javax.persistence.Converter

data class Email(val address: String) {

    companion object {
        fun isEmailValid(email: String): Boolean {
            return Pattern.compile(
                    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }
    }

    init {
        if(!isEmailValid(address)) {
            throw InvalidDataException("Not a valid email address: '$address'")
        }
    }

}

@Converter(autoApply = true)
class EmailConverter : AttributeConverter<Email, String> {

    override fun convertToDatabaseColumn(text: Email): String = text.address

    override fun convertToEntityAttribute(dbData: String): Email = Email(dbData)

}
