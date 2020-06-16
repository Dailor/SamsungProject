package com.samsungproject.SamsungProject.repo;

import com.samsungproject.SamsungProject.models.EventNotification;
import com.samsungproject.SamsungProject.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface EventNotificationRepository extends CrudRepository<EventNotification, Long> {
    Integer countEventNotificationByUserAndStatusIsFalse(User user);
    Set<EventNotification> findAllByUserAndStatusIsFalse(User user);
}
