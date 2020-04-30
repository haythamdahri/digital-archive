package org.digital.archive.configuration;

import org.digital.archive.entities.Archive;
import org.digital.archive.entities.User;
import org.digital.archive.services.ArchiveService;
import org.digital.archive.utils.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author Haytham DAHRI
 */
@Configuration
@EnableAutoConfiguration
@ControllerAdvice
public class GlobalConfiguration {

    private SecurityHelper securityHelper;
    private ArchiveService archiveService;

    @Autowired
    public void setSecurityHelper(SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }

    @Autowired
    public void setArchiveService(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    /**
     * BCrypt Password Encoder Bean
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * User model attribute
     * @return User
     */
    @ModelAttribute("user")
    public User sendConnectedUser() {
        return this.securityHelper.getConnectedUser();
    }

    /**
     * Trending Archives
     * @return Page<Archive>
     */
    @ModelAttribute("trendingArchives")
    public Page<Archive> sendTrendingArchives() {
        return this.archiveService.getTrendingArchives(5);
    }

}
