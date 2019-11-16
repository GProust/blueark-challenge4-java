package com.blueark.challenge.challenge4.service;

import com.blueark.challenge.challenge4.data.CSVData;
import com.blueark.challenge.challenge4.data.DataStorage;
import com.blueark.challenge.challenge4.data.SurpriseResponse;
import com.blueark.challenge.challenge4.data.WaterData;
import com.blueark.challenge.challenge4.resource.rest.UserPayload;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SurpriseService {

    private static final double MAX_CONSUMPTION_NIGHT = 0.07d;
    private static final int MAX_WATER_USED_WHEN_LEAVING = 0;

    @Autowired
    private DataStorage dataStorage;

    public SurpriseResponse getSurpriseConsumption(String id, Date startDate, Date endDate) {
        final List<WaterData> waterDataListFiltered = SanitizerUtil.sanitizeDateAndFilterByPeriod(true, (List) dataStorage.getWaterDataById(id), startDate, endDate);
        final List<WaterData> datasFilteredAndSanitized = SanitizerUtil.sanitizeCSVData((List) waterDataListFiltered);
        final Set<Integer> daysToTest = new HashSet<>();
        datasFilteredAndSanitized.forEach(waterData -> {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(waterData.getBeginDate());
            gregorianCalendar.set(Calendar.HOUR, 0);
            gregorianCalendar.set(Calendar.MINUTE, 0);
            daysToTest.add(gregorianCalendar.get(Calendar.DAY_OF_MONTH));
        });
        for (Integer day : daysToTest) {
            final double nightSurprise = getNightSurprise(datasFilteredAndSanitized, day);
            if (nightSurprise > MAX_CONSUMPTION_NIGHT) {
                return new SurpriseResponse("NOK", "There is a over consumption during the night", "NIGHT_OVER_CONSUMPTION", nightSurprise, null);
            }
        }

        return new SurpriseResponse("OK", null, null, null, null);
    }

    private double getNightSurprise(List<WaterData> waterDatas, int expectedDay) {
        final double totalNightConsumption = waterDatas.stream().filter(waterData -> {
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(waterData.getBeginDate());
            int hour = gregorianCalendar.get(Calendar.HOUR);
            int day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
            return hour < 5 && expectedDay == day;
        }).map(WaterData::getConsumption).mapToDouble(Double::doubleValue).sum();
        return totalNightConsumption;
    }

    public SurpriseResponse determineIfSurpriseDuringLeaving(String id) {
        final UserPayload userData = dataStorage.getUserDataById(id);
        final List<WaterData> waterDataListFiltered = SanitizerUtil.sanitizeDateAndFilterByPeriod(true, (List) dataStorage.getWaterDataById(id), userData.getDepartureDate(), userData.getReturnDate());
        final List<WaterData> datasFilteredAndSanitized = SanitizerUtil.sanitizeCSVData((List) waterDataListFiltered);
        final double sumConsumption = datasFilteredAndSanitized.stream().map(CSVData::getConsumption).mapToDouble(Double::doubleValue).sum();
        if (sumConsumption > MAX_WATER_USED_WHEN_LEAVING) {
            return new SurpriseResponse("UNEXPECTED", "There was an unexpected consumption during your absence", "UNEXPECTED_CONSUMPTION", sumConsumption, userData.getDepartureDate());
        }
        return new SurpriseResponse("OK", null, null, null, null);
    }
}
