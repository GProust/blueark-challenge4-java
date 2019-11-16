package com.blueark.challenge.challenge4.cron;

import com.blueark.challenge.challenge4.data.LeakResponse;
import com.blueark.challenge.challenge4.entity.UserData;
import com.blueark.challenge.challenge4.entity.UserNotification;
import com.blueark.challenge.challenge4.repository.UsersNotificationRepository;
import com.blueark.challenge.challenge4.repository.UsersRepository;
import com.blueark.challenge.challenge4.service.LeakService;
import com.blueark.challenge.challenge4.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LeakCron {

    @Autowired
    private LeakService leakService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersNotificationRepository usersNotificationRepository;
    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "10/30 * * * * *")
    @Transactional
    public void checkForLeak() {
        log.info("Begin to check the potential leak");
        final List<UserData> allUsers = usersRepository.findAll();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        gregorianCalendar.set(Calendar.MONTH, gregorianCalendar.get(Calendar.MONTH) - 2);
        allUsers.forEach(user -> {
            final LeakResponse leakResponse = leakService.isReturningToNoConsumption(user.getUserId(), gregorianCalendar.getTime(), new Date());
            final List<String> listNotification = usersNotificationRepository.findByUserId(user.getUserId()).stream().map(UserNotification::getNotificationType).collect(Collectors.toList());
            if (!leakResponse.getStatus().equalsIgnoreCase("OK") && listNotification.contains("sms")) {
                notificationService.sendSMS(leakResponse.getError(), null, user.getUserId());
            }
            if (!leakResponse.getStatus().equalsIgnoreCase("OK") && listNotification.contains("mail")) {
                notificationService.sendEmail(leakResponse.getError(), null, user.getUserId());
            }
            if (!leakResponse.getStatus().equalsIgnoreCase("OK") && listNotification.contains("whatsap")) {
                notificationService.sendWhatsapp(leakResponse.getError(), null, user.getUserId());
            }
        });
    }
}
