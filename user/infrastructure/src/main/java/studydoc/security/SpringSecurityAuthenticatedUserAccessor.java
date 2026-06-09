package studydoc.security;

import org.springframework.stereotype.Component;
import studydoc.port.AuthenticatedUserAccessor;

import java.util.Optional;

@Component
public class SpringSecurityAuthenticatedUserAccessor implements AuthenticatedUserAccessor {

    @Override
    public Optional<String> getUserId() {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String userId) {
            return Optional.of(userId);
        }
        return Optional.empty();
    }
}
