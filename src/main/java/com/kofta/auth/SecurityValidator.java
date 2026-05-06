package com.kofta.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("sec")
public class SecurityValidator {

    public boolean isSelfEngineer(Integer pathEngineerId) {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        if (
            !(auth.getPrincipal() instanceof AuthUser currentUser)
        ) return false;

        return pathEngineerId.equals(currentUser.engineerId());
    }

    public boolean belongsToCompany(Integer pathCompanyId) {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        if (
            !(auth.getPrincipal() instanceof AuthUser currentUser)
        ) return false;

        return pathCompanyId.equals(currentUser.companyId());
    }
}
