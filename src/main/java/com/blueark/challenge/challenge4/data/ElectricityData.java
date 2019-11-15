package com.blueark.challenge.challenge4.data;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.util.Date;

@Data
public class ElectricityData {

    private static final String DATE_FORMAT_CSV = "dd.MM.yyyy HH:mm";

    @CsvBindByPosition(position = 0)
    @CsvDate(DATE_FORMAT_CSV)
    private Date beginDate;
    @CsvBindByPosition(position = 1)
    @CsvDate(DATE_FORMAT_CSV)
    private Date endDate;
    @CsvBindByPosition(position = 2)
    private Double consumption;
}

