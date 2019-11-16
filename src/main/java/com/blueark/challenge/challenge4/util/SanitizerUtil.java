package com.blueark.challenge.challenge4.util;

import com.blueark.challenge.challenge4.data.CSVData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

public class SanitizerUtil {
    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    public static List<CSVData> sanitizeCSVData(List<CSVData> csvDatas, boolean isWaterData) {
        final List<Integer> indexesOfNull = csvDatas.stream()
                .filter(data -> data.getConsumption() == null)
                .map(csvDatas::indexOf)
                .collect(Collectors.toList());
        int size = indexesOfNull.size();
        for (int i = 0; i < size; i++) {
            indexesOfNull.add(indexesOfNull.get(i) + 1);
        }
        final List<CSVData> csvDataToRemove = indexesOfNull.stream().map(csvDatas::get).collect(Collectors.toList());
        final List<CSVData> filteredDatas = csvDatas.stream().filter(csvData -> !csvDataToRemove.contains(csvData)).collect(Collectors.toList());
        if (isWaterData) {
            filteredDatas.stream().filter(csvData -> csvData.getEndDate() == null).forEach(csvData -> {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(csvData.getBeginDate());
                gregorianCalendar.add(Calendar.HOUR, 1);
                csvData.setEndDate(gregorianCalendar.getTime());
            });
        } else {
            filteredDatas.stream().filter(csvData -> csvData.getEndDate() == null).forEach(csvData -> {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(csvData.getBeginDate());
                gregorianCalendar.add(Calendar.MINUTE, 15);
                csvData.setEndDate(gregorianCalendar.getTime());
            });
        }
        return filteredDatas;
    }

    public static Date convertFromString(String date) {
        try {
            return DATE_FORMATTER.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Issue with parsing string date");
        }
    }
}
