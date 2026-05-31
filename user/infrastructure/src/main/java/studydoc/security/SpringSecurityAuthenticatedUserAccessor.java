package studydoc.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import studydoc.port.AuthenticatedUserAccessor;

import java.util.Optional;

@Component
public class SpringSecurityAuthenticatedUserAccessor implements AuthenticatedUserAccessor {

    @Override
    public Optional<String> getBearerToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            return Optional.of(jwtAuthentication.getToken().getTokenValue());
        }
        return Optional.empty();
    }
}
