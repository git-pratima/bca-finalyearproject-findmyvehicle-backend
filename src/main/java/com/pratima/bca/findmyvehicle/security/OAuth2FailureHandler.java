package com.pratima.bca.findmyvehicle.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        System.out.println("❌ OAuth2 FAILED!");
        System.out.println("❌ Exception type: {}"+exception.getClass().getName());
        System.out.println("❌ Exception message: {}"+exception.getMessage());
        System.out.println("❌ Root cause: {}"+exception.getCause() != null ? exception.getCause().getMessage() : "none");


        System.out.println("OAuth2 login failed");
        log.error("OAuth2 login failed: {}", exception.getMessage());

        String redirectUrl = frontendUrl + "/login?error=oauth2_failed";
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
