package io.monitoring.component.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // log the http request
        logger.info("URI: {}", request.getURI());
        logger.info("HTTP Method: {}", request.getMethodValue());
        logger.info("HTTP Headers: {}", request.getHeaders());
        logger.info("HTTP Body: {}", new String(body));
        return execution.execute(request, body);
    }
}