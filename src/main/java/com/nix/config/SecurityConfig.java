package com.nix.config;

import com.nix.security.AuthenticationSuccessHandlerImpl;
import net.tanesha.recaptcha.ReCaptchaImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private UserDetailsService userDetailsService;

    @Autowired
    Environment env;

    @Resource(name = "userDetailsService")
    public void setUserDetailsService(@Qualifier("userDetailsService") UserDetailsService userDetailsService) {
        log.info("inject userDetailsService bean, class - {}", userDetailsService.getClass().getCanonicalName());
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        log.info("set up configureGlobalSecurity with injected bean userDetailsService");
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("invoke configure method");
        // @formatter:off
        http
                .authorizeRequests()
                .antMatchers("/resources/**", "/", "/login", "/logout", "/errors/**", // TODO remove ** in /**
                        "/registration/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").access("hasRole('ADMIN') or hasRole('USER')")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("login")
                .passwordParameter("pwd")
                .successHandler(authenticationSuccessHandler())
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access_denied")
                .and()
                .csrf().disable();
        // @formatter:on
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        log.info("creation of {} bean", DaoAuthenticationProvider.class.getCanonicalName());
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        log.info("creation of {} bean", AuthenticationSuccessHandler.class.getCanonicalName());
        return new AuthenticationSuccessHandlerImpl();
    }

    @Bean
    public ReCaptchaImpl reCaptcha() {
        log.info("creation of {} bean", ReCaptchaImpl.class.getCanonicalName());
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(env.getProperty("recaptcha.secret-key"));
        reCaptcha.setPublicKey(env.getProperty("recaptcha.site-key"));
        return reCaptcha;
    }
}
