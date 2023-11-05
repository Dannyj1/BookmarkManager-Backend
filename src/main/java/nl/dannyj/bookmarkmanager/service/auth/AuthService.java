package nl.dannyj.bookmarkmanager.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final SecurityService securityService;

    @Autowired
    public AuthService(SecurityService securityService) {
        this.securityService = securityService;
    }



}
