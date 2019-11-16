package com.blueark.challenge.challenge4.cron;

import com.blueark.challenge.challenge4.data.SurpriseResponse;
import com.blueark.challenge.challenge4.entity.UserData;
import com.blueark.challenge.challenge4.entity.UserNotification;
import com.blueark.challenge.challenge4.repository.UsersNotificationRepository;
import com.blueark.challenge.challenge4.repository.UsersRepository;
import com.blueark.challenge.challenge4.service.NotificationService;
import com.blueark.challenge.challenge4.service.SurpriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class HolidayCheckCron {

    @Autowired
    private SurpriseService surpriseService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersNotificationRepository usersNotificationRepository;
    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "0/30 * * * * *")
    @Transactional
    public void checkForOverConsumptionOnHolidays() {
        log.info("Begin to check the potential unknown consumption during holidays of the clients");
        final List<UserData> allUsers = usersRepository.findAll();
        allUsers.forEach(user -> {
            if (user.getDepartureDate() != null && user.getReturnDate() != null) {
                final SurpriseResponse surpriseResponse = surpriseService.determineIfSurpriseDuringLeaving(user.getUserId());
                final List<String> listNotification = usersNotificationRepository.findByUserId(user.getUserId()).stream().map(UserNotification::getNotificationType).collect(Collectors.toList());
                if (!surpriseResponse.getStatus().equalsIgnoreCase("OK") && listNotification.contains("sms")) {
                    notificationService.sendSMS(surpriseResponse.getMessage(), surpriseResponse.getSurpriseConsumption(), user.getUserId());
                }
                if (!surpriseResponse.getStatus().equalsIgnoreCase("OK") && listNotification.contains("mail")) {
                    notificationService.sendEmail(surpriseResponse.getMessage(), surpriseResponse.getSurpriseConsumption(), user.getUserId());
                }
                if (!surpriseResponse.getStatus().equalsIgnoreCase("OK") && listNotification.contains("whatsap")) {
                    notificationService.sendWhatsapp(surpriseResponse.getMessage(), surpriseResponse.getSurpriseConsumption(), user.getUserId());
                }
            }
        });
    }
}
