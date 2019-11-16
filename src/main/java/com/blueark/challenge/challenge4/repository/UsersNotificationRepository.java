package com.blueark.challenge.challenge4.repository;

import com.blueark.challenge.challenge4.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersNotificationRepository extends JpaRepository<UserNotification, Long> {

    List<UserNotification> findByUserId(String userId);
}
