package com.raglic.ark2mail.model

import java.util.regex.Pattern
import javax.persistence.AttributeConverter
import javax.persistence.Converter

class UserText {

    val text: String

    constructor(textIn: String?) {
        this.text = textIn
                ?.replace("[^A-Za-z0-9 ]".toRegex(), "")
                ?: ""

        if(!isTextValid(text)) {
            throw InvalidDataException("Max 100 characters")
        }
    }

    companion object {
        fun isTextValid(text: String): Boolean {
            require(text.length < 100) {
                "Text should be max 100!"
            }

            return true
        }
    }

}

@Converter(autoApply = true)
class UserTextConverter : AttributeConverter<UserText, String> {

    override fun convertToDatabaseColumn(text: UserText): String = text.text

    override fun convertToEntityAttribute(dbData: String): UserText = UserText(dbData)

}