package com.blueark.challenge.challenge4.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NOTIFICATION_USERS")
@Data
public class UserNotification {

    @Column
    @Id
    private Long notificationId;

    @Column
    private String userId;

    @Column
    private String notificationType;

    public UserNotification(String userId, String notificationType) {
        this.userId = userId;
        this.notificationType = notificationType;
    }
}
