package fis.baolm2.ctm.spring.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class UserUtil {
    public static String getUserId(Authentication authentication) {
        JwtAuthenticationToken oauthToken = (JwtAuthenticationToken) authentication;
        return oauthToken.getToken().getSubject();
    }
}
