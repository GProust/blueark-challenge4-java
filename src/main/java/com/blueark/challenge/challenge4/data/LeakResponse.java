package com.blueark.challenge.challenge4.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class LeakResponse {

    private String status;
    private String error;
    private Date potentialLeakDate;
}
