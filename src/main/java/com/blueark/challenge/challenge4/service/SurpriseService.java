package com.blueark.challenge.challenge4.service;

import com.blueark.challenge.challenge4.data.CSVData;
import com.blueark.challenge.challenge4.data.DataStorage;
import com.blueark.challenge.challenge4.data.SurpriseResponse;
import com.blueark.challenge.challenge4.data.WaterData;
import com.blueark.challenge.challenge4.resource.rest.UserPayload;
import com.blueark.challenge.challenge4.util.DateUtils;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SurpriseService {

    private static final double MAX_CONSUMPTION_NIGHT = 0.07d;
    private static final int MAX_WATER_USED_WHEN_LEAVING = 0;
    private static final double MAX_AUTHORIZED_UPPER_LIMIT_PERCENT = 1.3d;
    private static final int MIN_VALUE_NEEDED_TO_PROCESS = 8000;

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

    public SurpriseResponse calculateDiffBetweenLastYearSameMonth(String id, Date startDate, Date endDate) {
        final List waterDataById = dataStorage.getWaterDataById(id);
        final Date startDateLastYear = DateUtils.changeDate(-1, 0, 0, startDate);
        final Date endDateLastYear = DateUtils.changeDate(0, 0, 30, startDateLastYear);
        final List<WaterData> waterDataListFiltered = SanitizerUtil.sanitizeDateAndFilterByPeriod(true, waterDataById, startDate, endDate);
        final List<WaterData> waterDataListFilteredLastYear = SanitizerUtil.sanitizeDateAndFilterByPeriod(true, waterDataById, startDateLastYear, endDateLastYear);
        final Double moyenneCurrent = getMoyenne(waterDataListFiltered);
        final Double moyenneLimit = getMoyenne(waterDataListFilteredLastYear) * MAX_AUTHORIZED_UPPER_LIMIT_PERCENT;
        if (moyenneLimit > moyenneCurrent)
            return new SurpriseResponse("UNEXPECTED", "You consume too much resources this week compare to last year month", "OVERCONSUME", null, startDate);
        return new SurpriseResponse("OK", null, null, null, null);
    }

    public SurpriseResponse calculateDiffBetweenLastYear(String id, Date lastDateObtain) {
        final List waterDataById = dataStorage.getWaterDataById(id);
        final GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(DateUtils.getYear(lastDateObtain) - 1, Calendar.JANUARY, 1);
        final Date startLastYearDate = gregorianCalendar.getTime();
        gregorianCalendar.set(DateUtils.getYear(lastDateObtain), Calendar.JANUARY, 1);
        final Date startLaCurrentYearDate = gregorianCalendar.getTime();
        gregorianCalendar.set(DateUtils.getYear(lastDateObtain) - 1, Calendar.DECEMBER, 31);
        final Date endLastYearDate = gregorianCalendar.getTime();
        final List<WaterData> waterDataListFiltered = SanitizerUtil.sanitizeDateAndFilterByPeriod(true, waterDataById, startLaCurrentYearDate, lastDateObtain);
        final List<WaterData> waterDataListFilteredLastYear = SanitizerUtil.sanitizeDateAndFilterByPeriod(true, waterDataById, startLastYearDate, endLastYearDate);
        if (waterDataListFilteredLastYear.size() < MIN_VALUE_NEEDED_TO_PROCESS)
            return new SurpriseResponse("FAIL", "Unable to process due to the lack of data", "LACK_OF_DATA", null, null);
        final Double sumCurrentYear = waterDataListFiltered.stream().filter(waterData -> waterData.getConsumption() != null).map(WaterData::getConsumption).mapToDouble(Double::doubleValue).sum();
        final Double sumLastYear = waterDataListFilteredLastYear.stream().filter(waterData -> waterData.getConsumption() != null).map(WaterData::getConsumption).mapToDouble(Double::doubleValue).sum();
        final Double quarterlyPastYear = sumLastYear / 4;
        if (sumCurrentYear < quarterlyPastYear) return new SurpriseResponse("OK", null, null, null, null);
        Double moyenneHour = sumCurrentYear / waterDataListFiltered.size();
        Double projection = moyenneHour * waterDataListFilteredLastYear.size();
        if (projection > sumLastYear)
            return new SurpriseResponse("FAILURE", "Be careful, after projection you use morewater than the previous year.", "OVER_CONSUMPTION", projection - sumLastYear, lastDateObtain);
        return new SurpriseResponse("OK", null, null, null, null);
    }

    private Double getMoyenne(List<WaterData> waterDatas) {
        final double totalConsumption = waterDatas.stream()
                .filter(waterData -> waterData.getConsumption() != null)
                .map(WaterData::getConsumption)
                .mapToDouble(Double::doubleValue)
                .sum();
        return totalConsumption / waterDatas.size();
    }
}
