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

@Component
public class SecurityHelper {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession httpSession;

    private static final Map<RoleType, String> roles = Stream.of(
            new AbstractMap.SimpleEntry<>(RoleType.ROLE_PROFESSOR, "Professeur"),
            new AbstractMap.SimpleEntry<>(RoleType.ROLE_ADMINISTRATOR, "Administrateur"),
            new AbstractMap.SimpleEntry<>(RoleType.ROLE_MODERATOR, "Moderateur"),
            new AbstractMap.SimpleEntry<>(RoleType.ROLE_STUDENT, "Étudiant"),
            new AbstractMap.SimpleEntry<>(RoleType.ROLE_USER, "Utilisateur"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


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

    /*
     * Users roles
     */
    @Bean
    public Map<RoleType, String> getRoles() {
        return roles;
    }

}
