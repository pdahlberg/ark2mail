package com.raglic.ark2mail

import com.raglic.ark2mail.AddressController.Companion.UI_HOST
import com.raglic.ark2mail.model.ArkAmount
import com.raglic.ark2mail.dao.*
import com.raglic.ark2mail.model.ArkAddress
import com.raglic.ark2mail.model.Email
import com.raglic.ark2mail.model.UserText
import com.raglic.ark2mail.service.AddressService
import com.raglic.ark2mail.service.ForwardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

// todo: PoC cross origin while running separate UI with ngrok
@CrossOrigin(origins = ["http://localhost:4200", "http://localhost:8080", UI_HOST], maxAge = 3600)
@RestController
@RequestMapping("/api")
class AddressController @Autowired constructor(
        private val forwardService: ForwardService,
        private val addressService: AddressService,
        private val appConfig: AppConfiguration) {

    private val log = org.slf4j.LoggerFactory.getLogger(AddressController::class.java)

    companion object {
        const val UI_HOST = "http://c36be7f9.ngrok.io"
    }

    @RequestMapping("/deposit", method = [(RequestMethod.POST)])
    @ResponseBody
    fun create(@RequestBody dao: CreateForwardDao): ResponseEntity<ForwardCreatedDao> {
        log.debug("create: $dao")

        val senderName = UserText(dao.senderName)
        val amount = ArkAmount.fromHumanReadable(dao.amount)
        val receiver = Email(dao.receiver)

        val returnToAddress: ArkAddress = dao.returnAddress
                ?.let { ArkAddress(it) }
                ?: ArkAddress(appConfig.transactionFeeAddress)

        val forward = forwardService.create(senderName, returnToAddress, receiver, amount)

        return ok(ForwardCreatedDao(forward))
    }

    @RequestMapping("/withdraw/{address}/accept/{acceptKey}", method = [(RequestMethod.GET)])
    @ResponseBody
    fun accept(@PathVariable address: String, @PathVariable acceptKey: String): ResponseEntity<WithdrawInfoDao> {
        log.debug("GET /withdraw/$address/accept/$acceptKey")

        val depositAddress = ArkAddress(address)
        val forward = forwardService.accept(depositAddress, acceptKey)
        return ok(WithdrawInfoDao(forward))
    }

    @RequestMapping("/withdraw/{address}/accept/{acceptKey}", method = [(RequestMethod.POST)])
    @ResponseBody
    fun withdraw(@PathVariable address: String, @PathVariable acceptKey: String, @RequestBody dao: WithdrawDao): ResponseEntity<WithdrawCompletedDao> {
        log.debug("POST /withdraw/$address/accept/$acceptKey with body: $dao")

        val depositAddress = ArkAddress(address)
        val receiver = ArkAddress(dao.receiver)

        // todo: make sure this can't be called several times to withdraw x times the amount
        val forward = forwardService.requestWithdraw(depositAddress, acceptKey, receiver)
        //if(forward.transaction failed) {
        //    throw RuntimeException
        //}
        return ok(WithdrawCompletedDao(forward))
    }

}