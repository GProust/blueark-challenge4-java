package com.blueark.challenge.challenge4.resource.rest;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserPayload {

    private String userId;
    private Date departureDate;
    private Date returnDate;
    private String residenceType;
    private List<String> notificationSubscribed;
}
