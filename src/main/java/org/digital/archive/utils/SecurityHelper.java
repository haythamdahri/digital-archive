package org.digital.archive.utils;

import org.digital.archive.entities.RoleType;
import org.digital.archive.entities.User;
import org.digital.archive.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Haytham DAHRI
 */
@Component
public class SecurityHelper {

    private static final Map<RoleType, String> roles = Stream.of(
            new AbstractMap.SimpleEntry<>(RoleType.ROLE_PROFESSOR, "Professeur"),
            new AbstractMap.SimpleEntry<>(RoleType.ROLE_STUDENT, "Étudiant"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    private UserService userService;
    private HttpSession httpSession;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * Retrieve current authenticated user
     *
     * @return User
     */
    public User getConnectedUser() {
        SecurityContext securityContext = (SecurityContext) this.httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null) {
            String email = securityContext.getAuthentication().getName();
            return this.userService.getUser(email);
        }
        return null;
    }

    /**
     * Roles Bean
     * <p>
     * return: Map<RoleType, String>
     */
    @Bean
    public Map<RoleType, String> getRoles() {
        return roles;
    }

}
