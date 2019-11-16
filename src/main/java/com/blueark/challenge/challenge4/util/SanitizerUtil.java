package com.blueark.challenge.challenge4.util;

import com.blueark.challenge.challenge4.data.CSVData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SanitizerUtil {
    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    public static List<CSVData> sanitizeCSVData(List<CSVData> csvDatas) {
        final List<Integer> indexesOfNull = csvDatas.stream()
                .filter(data -> data.getConsumption() == null)
                .map(csvDatas::indexOf)
                .collect(Collectors.toList());
        int size = indexesOfNull.size();
        for (int i = 0; i < size; i++) {
            indexesOfNull.add(indexesOfNull.get(i) + 1);
        }
        final List<CSVData> csvDataToRemove = indexesOfNull.stream().map(csvDatas::get).collect(Collectors.toList());
        return csvDatas.stream().filter(csvData -> !csvDataToRemove.contains(csvData)).collect(Collectors.toList());
    }

    public static List<CSVData> sanitizeDateAndFilterByPeriod(boolean isWaterData, List<CSVData> filteredDatas, Date startDate, Date endDate) {
        if (filteredDatas == null || filteredDatas.isEmpty()) return new ArrayList<>();
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
        return filterByPeriod(filteredDatas, startDate, endDate);
    }

    public static List<CSVData> filterByPeriod(List<CSVData> csvData, Date startDate, Date endDate) {
        return csvData.stream()
                .filter(waterData -> waterData.getBeginDate().after(startDate))
                .filter(waterData -> waterData.getEndDate().before(endDate))
                .collect(Collectors.toList());
    }


    public static Date convertFromString(String date) {
        try {
            return DATE_FORMATTER.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Issue with parsing string date");
        }
    }
}
