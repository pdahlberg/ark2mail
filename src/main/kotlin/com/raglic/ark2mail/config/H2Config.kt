package com.raglic.ark2mail.config

import org.h2.server.web.WebServlet
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("dev")
@Configuration
internal class H2Configuration {

    @Bean
    fun h2servletRegistration(): ServletRegistrationBean {
        val webServlet = WebServlet()
        val bean = ServletRegistrationBean(webServlet)
        bean.addUrlMappings("/h2console/*")
        return bean
    }

}