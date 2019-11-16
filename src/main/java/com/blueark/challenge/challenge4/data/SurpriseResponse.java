package com.blueark.challenge.challenge4.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SurpriseResponse {

    private String status;
    private String message;
    private String typeOfSurprise;
    private Double surpriseConsumption;
    private Date surpriseDate;
}
