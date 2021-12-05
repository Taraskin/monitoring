package io.monitoring.controller;

import io.monitoring.model.ServiceEntity;
import io.monitoring.service.ServicesCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServicesCRUDService servicesCRUDService;

    @Autowired
    public ServiceController(ServicesCRUDService servicesCRUDService) {
        this.servicesCRUDService = servicesCRUDService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServiceEntity> all() {
        return servicesCRUDService.findAllByLoggedInUser();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ServiceEntity> findOneById(@PathVariable("id") Long id) {
        return servicesCRUDService.findOneById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ServiceEntity addNew(@RequestBody ServiceEntity serviceEntity) {
        return servicesCRUDService.addNew(serviceEntity);
    }

    @PutMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ServiceEntity> update(@RequestBody ServiceEntity serviceEntity) {
        Optional<ServiceEntity> serviceOptional = servicesCRUDService.findOneById(serviceEntity.getId());
        if (serviceOptional.isPresent()) {
            ServiceEntity serviceEntityToUpdate = serviceOptional.get();
            serviceEntityToUpdate.setName(serviceEntity.getName());
            serviceEntityToUpdate.setUrl(serviceEntity.getUrl());
            return ResponseEntity.ok(servicesCRUDService.update(serviceEntityToUpdate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Optional<ServiceEntity> serviceOptional = servicesCRUDService.findOneById(id);
        if (serviceOptional.isPresent()) {
            servicesCRUDService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
