package io.monitoring.service;

import io.monitoring.model.ServiceEntity;
import io.monitoring.repository.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusUpdateService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ServiceRepository serviceRepository;
    private final PoolingService poolingService;

    public StatusUpdateService(ServiceRepository serviceRepository, PoolingService poolingService) {
        this.serviceRepository = serviceRepository;
        this.poolingService = poolingService;
    }

    @Scheduled(fixedDelay = 60000) // update service status each 1 minute
    public void refreshServiceStatus() {
        List<ServiceEntity> serviceEntityList = serviceRepository.findAll();
        logger.debug("Updating services status for {} services", serviceEntityList.size());
        serviceEntityList.forEach(poolingService::updateServiceStatus);
    }

}
