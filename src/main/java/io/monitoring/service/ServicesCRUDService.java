package io.monitoring.service;

import io.monitoring.model.ServiceEntity;
import io.monitoring.model.User;
import io.monitoring.repository.ServiceRepository;
import io.monitoring.repository.UserRepository;
import io.monitoring.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServicesCRUDService {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    @Autowired
    public ServicesCRUDService(ServiceRepository serviceRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    public List<ServiceEntity> findAllByLoggedInUser() {
        User loggedInUser = getLoggedInUser();
        return serviceRepository.findAllByUser(loggedInUser);
    }

    public Optional<ServiceEntity> findOneById(Long id) {
        return serviceRepository.findById(id);
    }

    public ServiceEntity addNew(ServiceEntity serviceEntity) {
        serviceEntity.setUser(getLoggedInUser());
        return serviceRepository.save(serviceEntity);
    }

    public ServiceEntity update(ServiceEntity serviceEntity) {
        return serviceRepository.saveAndFlush(serviceEntity);
    }

    public void deleteById(Long id) {
        serviceRepository.deleteById(id);
    }


    private User getLoggedInUser() {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(user.getId()).orElse(null);
    }
}
