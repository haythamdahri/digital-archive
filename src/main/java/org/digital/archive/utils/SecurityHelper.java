package org.digital.archive.utils;

import org.digital.archive.entities.User;
import org.digital.archive.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class SecurityHelper {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession httpSession;


    /*
     * Current connected user
     */
    public User getConnectedUser() {
        SecurityContext securityContext = (SecurityContext) this.httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null) {
            String email = securityContext.getAuthentication().getName();
            User user = this.userService.getUser(email);
            return user;
        }
        return null;
    }

}
