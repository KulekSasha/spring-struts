package com.nix.controller;

import com.nix.config.AppTestConfig;
import com.nix.config.SecurityConfig;
import com.nix.config.WebAppConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {AppTestConfig.class,
                WebAppConfig.class,
                SecurityConfig.class})
@WebAppConfiguration
public class SecurityTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    @Mock
    private UserDetailsService userDetailsServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity(springSecurityFilterChain))
                .build();
        MockitoAnnotations.initMocks(this);
    }

    @Test(timeout = 2000L)
    public void getAdminPageNotAuthorize() throws Exception {

        mockMvc.perform(get("/admin/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

    }

    @Test(timeout = 2000L)
    @WithMockUser(roles = {"ADMIN"})
    public void userPerformLogin() throws Exception {

//        when(userDetailsServiceMock.loadUserByUsername(anyString()))
//                .thenReturn(any(UserDetails.class));

        mockMvc
                .perform(post("/login").param("login", "admin").param("pwd", "pass"))
//                .perform(formLogin("/login").user("admin").password("pass"))
                .andDo(print());

//        verify(userDetailsServiceMock, times(1)).loadUserByUsername(anyString());

    }


    @Test
    public void requiresAuthentication() throws Exception {
        mockMvc.perform(get("/admin/users").with(user("admin").password("pass").roles("USER", "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void requiresAuthentication2() throws Exception {
        mockMvc.perform(get("/user/user").with(user("user").password("pass").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void requiresAuthentication3() throws Exception {
        mockMvc.perform(get("/admin/users").with(user("user").password("pass").roles("USER")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void authenticationSuccess() throws Exception {
        mockMvc
//                .perform(formLogin().user("login", "admin").password("pwd", "pass"))
                .perform(post("/login").param("login", "admin").param("pwd", "pass")
                        .with(user("admin").password("pass").authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andDo(print())
//                .andExpect(status().isFound())
//                .andExpect(authenticated().withUsername("user"))
        ;
    }
}
