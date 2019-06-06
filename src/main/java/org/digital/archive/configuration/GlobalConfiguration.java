package org.digital.archive.configuration;

import org.digital.archive.entities.User;
import org.digital.archive.utils.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Configuration
@EnableAutoConfiguration
@ControllerAdvice
public class GlobalConfiguration {

    @Autowired
    private SecurityHelper securityHelper;

    /*
     * Bcrypt password encoder instance
     */
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * User model attribute
     */
    @ModelAttribute("user")
    public User sendConnectedUser() {
        return this.securityHelper.getConnectedUser();
    }


}
