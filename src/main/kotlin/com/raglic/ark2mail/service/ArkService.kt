package com.raglic.ark2mail.service

import com.raglic.ark2mail.model.ArkAmount
import com.raglic.ark2mail.model.ArkAmount.Companion.ONE_ARK
import com.raglic.ark2mail.model.ArkAddress
import com.raglic.ark2mail.model.ArkTransaction
import com.raglic.ark2mail.persistence.ArkAddressRepository
import io.ark.core.Account
import io.ark.core.Crypto
import io.ark.core.Network
import io.ark.core.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class ArkService {

    private val log = org.slf4j.LoggerFactory.getLogger(ArkService::class.java)

    val network: Network

    // todo: PoC
    init {
        network = Network.getDevnet()
        log.info("Ark network starting")
        network.warmup(12)
        log.info("Ark network warmup done")
    }

    // todo: get fee from network
    fun getNetworkFee(): ArkAmount {
        return ArkAmount(ONE_ARK) / 10
    }

    fun updateBalance(address: ArkAddress) {
        val account = Account(address.address, network)
        val updateSuccessful = account.updateBalance()  // todo: handle retry and fail
        if(updateSuccessful) {
            if (address.balance.amount != account.balance) {
                address.balance = ArkAmount(account.balance)
            }
        } else {
            log.debug("Account balance not updated")
        }
    }

    fun createTransaction(deposit: ArkAddress, receiverAmount: ArkAmount, withdrawTo: ArkAddress): ArkTransaction {
        val tx = Transaction.createTransaction(withdrawTo.address, receiverAmount.amount, "Ark2Mail", deposit.passphrase)

        return ArkTransaction(tx.id!!, tx)
    }

    fun sendTransaction(tx: ArkTransaction) {
        try {
            network.leftShift(tx.coreTransaction) // todo: handle retry and fail
            tx.status = ArkTransaction.Status.SENT
        } catch (e: Exception) {
            tx.status = ArkTransaction.Status.FAILED
        }
    }

    fun verifyTransaction(tx: ArkTransaction, requiredConfirms: Int): Boolean {
        return try {
            val transaction = network.randomPeer.getTransaction(tx.transactionId)
            if(transaction.confirmations >= requiredConfirms) {
                tx.status = ArkTransaction.Status.CONFIRMED
                return true
            } else if(transaction.confirmations > 0) {
                tx.status = ArkTransaction.Status.UNCONFIRMED
            }
            false
        } catch (e: Exception) {
            log.debug("Error getting transaction status: ${e.message} for Tx $tx")
            false
        }
    }

}