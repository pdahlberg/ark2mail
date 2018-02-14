package com.raglic.ark2mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
public class ThymeleafConfig {

    @Bean
    public org.thymeleaf.templateresolver.TemplateResolver templateResolver() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    //@Bean
    // public IDialect java8TimeDialect() {
    //    return new Java8TimeDialect();
    //}

}
