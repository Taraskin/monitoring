package io.monitoring.repository;

import io.monitoring.model.ServiceEntity;
import io.monitoring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    List<ServiceEntity> findAllByUser(User user);
}
