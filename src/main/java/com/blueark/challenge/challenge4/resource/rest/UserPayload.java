package com.blueark.challenge.challenge4.resource.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class UserPayload {

    private String userId;
    private Date departureDate;
    private Date returnDate;
    private String residenceType;
    private List<String> notificationSubscribed;
}
