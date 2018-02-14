package com.raglic.ark2mail.service

import com.raglic.ark2mail.AddressController
import com.raglic.ark2mail.AppConfiguration
import com.raglic.ark2mail.model.ArkAmount
import com.raglic.ark2mail.model.ArkAmount.Companion.ONE_ARK
import com.raglic.ark2mail.model.*
import com.raglic.ark2mail.persistence.ArkForwardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class ForwardService @Autowired constructor(
        private val arkService: ArkService,
        private val appConfig: AppConfiguration,
        private val passwordService: PasswordService,
        private val arkForwardRepository: ArkForwardRepository,
        private val receiverService: ReceiverService,
        private val addressService: AddressService) {

    private val log = org.slf4j.LoggerFactory.getLogger(ForwardService::class.java)

    fun create(senderName: UserText, returnToAddress: ArkAddress, receiverEmail: Email, receiverAmount: ArkAmount): ArkForward {
        val receiver = receiverService.getOrCreate(receiverEmail)
        val feeAmount = calculateFee()
        val totalAmount: ArkAmount = receiverAmount + feeAmount
        val depositAddress = addressService.createAddress()
        val cancelPassword = generateCancelPassword()
        val acceptKey = generateAcceptKey()
        val savedReturnAddress = addressService.getOrCreate(returnToAddress)

        verifyInput(receiverAmount)

        val forward = ArkForward(senderName, savedReturnAddress, receiver, receiverAmount, feeAmount, totalAmount,
                depositAddress, cancelPassword, acceptKey)

        arkForwardRepository.save(forward)

        return forward
    }

    private fun verifyInput(receiverAmount: ArkAmount) {
        val minimum = ArkAmount(ONE_ARK) / 100
        if(receiverAmount < minimum) {
            throw InvalidDataException("Receiver amount must be at least ${minimum.getHumanReadable()} ARK")
        }
    }

    private fun generateCancelPassword(): String {
        return passwordService.generatePassword()
    }

    private fun calculateFee(): ArkAmount {
        val networkFee = arkService.getNetworkFee()
        return networkFee * 5
    }

    private fun generateAcceptKey(): String {
        return UUID.randomUUID().toString()
    }

    fun getAcceptLink(forward: ArkForward): String {
        val host = AddressController.UI_HOST
        //val host = "http://localhost:4200"
        return "$host/withdraw/${forward.deposit.address}/accept/${forward.acceptKey}"
    }

    fun accept(depositAddress: ArkAddress, acceptKey: String): ArkForward {
        val forward = arkForwardRepository.findByAcceptKeyAndStatusIn(acceptKey, listOf(ArkForward.Status.DEPOSITED, ArkForward.Status.ACCEPTED))
        if(forward.deposit.address != depositAddress.address) {
            throw RuntimeException("Incorrect address")
        }

        forward.status = ArkForward.Status.ACCEPTED

        // todo: change ownership to receiver, i.e. the amount will no longer revert back to the sender
        return forward
    }

    // todo: save transaction entity which enables retry etc.
    fun requestWithdraw(depositAddress: ArkAddress, acceptKey: String, withdrawAddress: ArkAddress): ArkForward {
        val forward = arkForwardRepository.findByAcceptKeyAndStatusIn(acceptKey, listOf(ArkForward.Status.DEPOSITED, ArkForward.Status.ACCEPTED))
        if(forward.deposit.address != depositAddress.address) {
            throw RuntimeException("Incorrect address")
        }
        log.debug("Transfer the receiver amount (${forward.receiverAmount} ARK) to $withdrawAddress")
        val tx = arkService.createTransaction(forward.deposit, forward.receiverAmount, withdrawAddress)
        forward.transaction = tx
        forward.status = ArkForward.Status.WITHDRAW_REQUESTED

        return forward
    }

    fun payFees(forward: ArkForward) {
        // todo: get the balance after the createTransaction, get all that is left?
        val feeAddress = ArkAddress(appConfig.transactionFeeAddress)
        val fee = forward.feeAmount - (arkService.getNetworkFee() * 2)

        log.debug("Transfer the Ark2Mail fee ($fee ARK) to it's own account")
        arkService.createTransaction(forward.deposit, fee, feeAddress)

    }

}