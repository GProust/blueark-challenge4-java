package com.blueark.challenge.challenge4.data;

import lombok.Data;

import java.util.List;

@Data
public class UserData {

    private Long userId;
    private List<String> notificationSubscribed;

}
