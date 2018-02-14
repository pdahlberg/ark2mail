package com.raglic.ark2mail.model

import java.util.regex.Pattern
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ArkAddress(val address: String,
                               val passphrase: String? = null,
                               var balance: ArkAmount = ArkAmount(0),
                               @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null) {

    companion object {
        fun isAddressValid(address: String): Boolean {
            require(address.length == 34) {
                "Address should be size 34!"
            }
            require(Pattern.compile("^[a-zA-Z0-9]*\$").matcher(address).matches()) {
                "Address should only contain letters and numbers"
            }
            return true
        }
    }

    init {
        if (!isAddressValid(address)) {
            throw InvalidDataException("Not a valid email address: '$address'")
        }
    }

}

/*
companion object {

    fun fromDto(dto: CityDto) = CityEntity(
      id = dto.id,
      name = dto.name,
      description = dto.description)

    fun fromDto(dto: CreateCityDto) = CityEntity(
      name = dto.name,
      description = dto.description)

    fun fromDto(dto: UpdateCityDto, defaultCity: CityEntity) = CityEntity(
                id = defaultCity.id!!,
                name = dto.name ?: defaultCity.name,
                description = dto.description ?: defaultCity.description)
  }
 */