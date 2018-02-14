package com.raglic.ark2mail.model

import com.raglic.ark2mail.dao.CreateForwardDao
import com.raglic.ark2mail.dao.ForwardCreatedDao
import com.raglic.ark2mail.model.UserText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.regex.Pattern


class UserTextTest {

    @Test
    fun valid() {
        val result = UserText("abc; drop table abc;")

        assertEquals("abc drop table abc", result.text)
    }

}