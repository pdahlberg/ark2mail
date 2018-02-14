package com.raglic.ark2mail.model

import com.raglic.ark2mail.dao.CreateForwardDao
import com.raglic.ark2mail.dao.ForwardCreatedDao
import com.raglic.ark2mail.model.UserText
import io.ark.core.Crypto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.regex.Pattern


class ArkForwardTest {

    @Test
    fun valid() {
        val result = ArkForward(
                UserText("abc"),
                ArkAddress("DA7L28NcKAXL6JW3zKAw9mBdKwsKL8NXSA"),
                Receiver(Email("mail@test.com")),
                ArkAmount(1),
                ArkAmount(1),
                ArkAmount(1),
                ArkAddress("DA7L28NcKAXL6JW3zKAw9mBdKwsKL8NXSA"),
                "pw",
                "key")

        System.out.println(result)
        //assertEquals("abc drop table abc", result.address)
    }

}