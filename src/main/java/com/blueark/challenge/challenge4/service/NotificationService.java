package com.blueark.challenge.challenge4.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationService {

    public void sendEmail(String message, Double overConsumption, String userId) {
        log.info("Mail sent to client {} with message {}, and overConsumption {}", userId, message, overConsumption);
    }

    public void sendSMS(String message, Double overConsumption, String userId) {
        log.info("SMS sent to client {} with message {}, and overConsumption {}", userId, message, overConsumption);
    }

    public void sendWhatsapp(String message, Double overConsumption, String userId) {
        log.info("Whatsapp sent to client {} with message {}, and overConsumption {}", userId, message, overConsumption);
    }
}
