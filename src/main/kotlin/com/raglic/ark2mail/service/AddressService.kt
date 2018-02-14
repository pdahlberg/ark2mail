package com.raglic.ark2mail.service

import com.raglic.ark2mail.model.ArkAddress
import com.raglic.ark2mail.model.Email
import com.raglic.ark2mail.model.Receiver
import com.raglic.ark2mail.persistence.ArkAddressRepository
import io.ark.core.Crypto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class AddressService(@Autowired val arkAddressRepository: ArkAddressRepository,
                     private val arkService: ArkService) {

    // todo: PoC, real implementation coming
    private val log = org.slf4j.LoggerFactory.getLogger(AddressService::class.java)
    val testPasswords = arrayOf("abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vxyz")
    var testPasswordPointer = 0

    fun getOrCreate(address: ArkAddress): ArkAddress {
        return arkAddressRepository.findByAddress(address.address) ?: save(address)
    }

    fun createAddress(): ArkAddress {
        val passphrase = getTestPassword()
        //UUID.randomUUID().toString()

        Crypto.setNetworkVersion(arkService.network.prefix)
        val address = Crypto.getAddress(Crypto.getKeys(passphrase));

        return save(ArkAddress(address, passphrase))
    }

    fun save(address: ArkAddress): ArkAddress = arkAddressRepository.save(address)

    // todo: only if devnet
    private fun getTestPassword(): String {
        val selected = testPasswords[testPasswordPointer]
        testPasswordPointer += 1
        if(testPasswordPointer >= testPasswords.size) {
            testPasswordPointer = 0
        }
        return selected
    }

}