package io.monitoring.service;

import io.monitoring.model.ServiceEntity;
import io.monitoring.model.dict.ServiceStatus;
import io.monitoring.repository.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@Service
public class PoolingService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ServiceRepository serviceRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public PoolingService(ServiceRepository serviceRepository, RestTemplate restTemplate) {
        this.serviceRepository = serviceRepository;
        this.restTemplate = restTemplate;
    }

    @Async
    @Transactional
    public void updateServiceStatus(ServiceEntity service) {
        ServiceStatus status = ServiceStatus.FAIL;
        try {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(service.getUrl(), HttpMethod.GET, null, Void.class);
            logger.info("Got response for URL: {}. Status: {}", service.getUrl(), responseEntity.getStatusCode());
            if (responseEntity.getStatusCodeValue() >= 200 && responseEntity.getStatusCodeValue() < 300) {
                status = ServiceStatus.OK;
            }
        } catch (Exception e) {
            logger.error("Failed to update status for service: {} because of: {}", service, e.getMessage());
        } finally {
            service.setUpdateDate(Date.from(Instant.now(Clock.systemDefaultZone())));
            service.setStatus(status);
            serviceRepository.save(service);
            logger.debug("Updated status for service: {}. New status is: {}", service, service.getStatus());
        }
    }
}
