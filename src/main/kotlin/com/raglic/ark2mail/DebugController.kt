package com.raglic.ark2mail

import com.raglic.ark2mail.service.EmailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class DebugController @Autowired constructor(val emailService: EmailService) {

    private val log = org.slf4j.LoggerFactory.getLogger(DebugController::class.java)

    @GetMapping("/test")
    @ResponseBody
    fun helloSpringBoot(): ResponseEntity<String> {

        // emailService.send(Email("per.dahlberg@gmail.com"), ArkAmount(2 * ONE_ARK), "http://abc.123")

        return ok("{ \"message\": \"...\" }")
    }

}