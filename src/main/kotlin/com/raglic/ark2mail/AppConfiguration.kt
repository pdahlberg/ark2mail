package com.raglic.ark2mail

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.apache.tomcat.jni.SSL.setPassword
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.JavaMailSender



@Configuration
@EnableAsync
@EnableScheduling
class AppConfiguration {

    @Value("\${transaction.fee.address}")
    var transactionFeeAddress: String = "?"

    @Bean
    fun configureObjectMapper(): Jackson2ObjectMapperBuilder {
        return Jackson2ObjectMapperBuilder()
                .modulesToInstall(KotlinModule())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

/*    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = "smtp.gmail.com"
        mailSender.port = 587

        mailSender.username = "???"
        mailSender.password = "???"
*/
/*        val props = mailSender.javaMailProperties
        props.put("mail.transport.protocol", "smtp")
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.debug", "true")*/

        //return mailSender
    //}

}