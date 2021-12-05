package io.monitoring.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.monitoring.model.payload.ExceptionResponse;
import io.monitoring.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .ifPresent(principal -> {
                    UserDetailsImpl user = (UserDetailsImpl) principal;
                    logger.warn("Unauthorized access attempt made by user: id={}, name={}", user.getId(), user.getUsername());
                });
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception, HttpStatus.FORBIDDEN);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getOutputStream().println(objectMapper.writeValueAsString(exceptionResponse));
    }
}
