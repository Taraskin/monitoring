package io.monitoring.component.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class CustomClientErrorHandler implements ResponseErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return clientHttpResponse.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        logger.error("HTTP Status Code: " + clientHttpResponse.getStatusCode().value());
    }
}