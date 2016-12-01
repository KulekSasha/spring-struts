package com.nix.config;

import net.tanesha.recaptcha.ReCaptchaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@Import(value = {DaoConfig.class, SecurityConfig.class})
@ComponentScan(basePackages = {
        "com.nix.service",
        "com.nix.dao",
        "com.nix.tag",
        "com.nix.struts.*",
})
public class AppConfig {

    @Autowired
    Environment env;

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("messages");
        messageSource.setCacheSeconds(60);
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource());
        return validatorFactoryBean;
    }

    @Bean
    public ReCaptchaImpl reCaptcha() {
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(env.getProperty("recaptcha.secret-key"));
        reCaptcha.setPublicKey(env.getProperty("recaptcha.site-key"));
        return reCaptcha;
    }
}
