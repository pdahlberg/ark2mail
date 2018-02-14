package com.raglic.ark2mail

import com.raglic.ark2mail.dao.CreateForwardDao
import com.raglic.ark2mail.dao.ForwardCreatedDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(App::class)], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class KotlinDemoApplicationTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun whenCalled_shouldReturnHello() {
        val responseEntity = restTemplate.postForEntity("/api/address", CreateForwardDao("sender", "receiver", "mail@test.com", 10.0), ForwardCreatedDao::class.java)
        val client = responseEntity.getBody()

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode())
        assertEquals("receiver", client.receiver)
    }

}