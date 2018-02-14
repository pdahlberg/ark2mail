package com.raglic.ark2mail

import com.raglic.ark2mail.model.ArkForward
import com.raglic.ark2mail.model.ArkTransaction
import com.raglic.ark2mail.service.ArkService
import com.raglic.ark2mail.service.EmailService
import com.raglic.ark2mail.service.ForwardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
internal class AddressTrackerTask @Autowired constructor(
        val arkDb: FakeDatabase,
        val arkService: ArkService,
        val forwardService: ForwardService,
        val emailService: EmailService) {

    private val log = org.slf4j.LoggerFactory.getLogger(AddressTrackerTask::class.java)

    // todo: PoC, real implementation coming
    @Scheduled(fixedDelay = 20000)
    public fun trackBalance() {
        arkDb.getAwaitingDeposit().forEach { forward ->
            val address = forward.deposit
            log.debug("Waiting for: ${address.address} [${address.balance.getHumanReadable()}]")
            arkService.updateBalance(address)
            if(address.balance.amount >= forward.totalAmount.amount) {
                forward.status = ArkForward.Status.DEPOSITED
                val acceptLink = forwardService.getAcceptLink(forward)
                emailService.send(forward.senderName, forward.receiver.email, forward.receiverAmount, acceptLink)
                log.debug("Amount is enough! ${address.balance.getHumanReadable()} and accept link for email: ${acceptLink}")
            }
        }

        arkDb.getWithdrawInProgress().forEach { forward ->
            val tx = forward.transaction!!

            if(tx.status == ArkTransaction.Status.CREATED || tx.status == ArkTransaction.Status.FAILED) {
                log.debug("Sending ${tx.status} transaction: $forward")
                arkService.sendTransaction(tx)
            } else {
                val confirmed = arkService.verifyTransaction(tx, 16)

                if (confirmed) {
                    forward.status = ArkForward.Status.WITHDRAWN
                    log.debug("Withdraw successful: $forward")
                } else {
                    log.debug("Status ${forward.status}/${tx.status} for ${tx.millisSinceStatusModified() / 1000} seconds")
                    if(tx.status == ArkTransaction.Status.SENT && tx.millisSinceStatusModified() > 1000 * 60 * 1) {
                        log.debug("Transaction seems to be stalled: $forward")
                        tx.status = ArkTransaction.Status.FAILED
                    } else if(tx.status == ArkTransaction.Status.UNCONFIRMED && tx.millisSinceStatusModified() > 1000 * 60 * 5) {
                        log.debug("Transaction has been UNCONFIRMED for very long: $forward")
                        tx.status = ArkTransaction.Status.FAILED
                    }
                }
            }

        }
    }

}

internal class FakeDatabase {
    fun getAwaitingDeposit(): List<ArkForward> {
        throw RuntimeException("Fake db removed")
    }

    fun getWithdrawInProgress(): List<ArkForward> {
        throw RuntimeException("Fake db removed")
    }

}