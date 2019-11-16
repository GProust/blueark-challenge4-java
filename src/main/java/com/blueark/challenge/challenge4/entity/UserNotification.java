package com.blueark.challenge.challenge4.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "NOTIFICATION_USERS")
@Data
@NoArgsConstructor
public class UserNotification {

    @Column
    @Id
    @GeneratedValue
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
