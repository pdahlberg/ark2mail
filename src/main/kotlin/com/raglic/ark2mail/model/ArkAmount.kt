package com.raglic.ark2mail.model

import javax.persistence.AttributeConverter
import javax.persistence.Converter

data class ArkAmount(val amount: Long) {

    companion object {
        val ONE_ARK: Long = 100000000

        fun fromHumanReadable(humanReadableAmount: Double): ArkAmount {
            return ArkAmount((humanReadableAmount * ONE_ARK).toLong())
        }
    }

    init {
        if(amount < 0) {
            throw InvalidDataException("Amount can't be negative")
        }
    }

    fun getHumanReadable(): Double {
        if (amount > 0) {
            return amount.toDouble() / ONE_ARK
        }
        return amount.toDouble()
    }

    operator fun plus(add: ArkAmount): ArkAmount {
        return ArkAmount(amount + add.amount)
    }

    operator fun minus(subtract: ArkAmount): ArkAmount {
        return ArkAmount(amount - subtract.amount)
    }

    operator fun times(multiply: ArkAmount): ArkAmount {
        return ArkAmount(amount * multiply.amount)
    }

    operator fun times(multiply: Long): ArkAmount {
        return ArkAmount(amount * multiply)
    }

    operator fun div(div: Long): ArkAmount {
        return ArkAmount(amount / div)
    }

    operator fun compareTo(other: ArkAmount): Int {
        return amount.compareTo(other.amount)
    }

}

@Converter(autoApply = true)
class AmountConverter : AttributeConverter<ArkAmount, Long> {

    override fun convertToDatabaseColumn(amount: ArkAmount): Long = amount.amount

    override fun convertToEntityAttribute(dbData: Long): ArkAmount = ArkAmount(dbData)

}