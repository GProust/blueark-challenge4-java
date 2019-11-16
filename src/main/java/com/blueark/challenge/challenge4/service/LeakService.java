package com.blueark.challenge.challenge4.service;

import com.blueark.challenge.challenge4.data.DataStorage;
import com.blueark.challenge.challenge4.data.WaterData;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LeakService {

    private static final float MAX_NUMBER_OF_UNDEFINED_VALUES_POURCENTAGE = 0.2f;
    private static final float MIN_ZERO_NUMBER_POURCENTAGE = 0.1f;
    private static final int NUMBER_OF_DAYS_WITHOUT_CONSUMPTION_LESS = 1;

    @Autowired
    private DataStorage dataStorage;

    public WaterData isReturningToNoConsumption(String id, Date startDate, Date endDate) {
        final List<WaterData> waterDataById = SanitizerUtil.sanitizeDate(true, (List) dataStorage.getWaterDataById(id));
        final List<WaterData> waterDataListFiltered = waterDataById.stream()
                .filter(waterData -> waterData.getBeginDate().after(startDate))
                .filter(waterData -> waterData.getEndDate().before(endDate))
                .collect(Collectors.toList());
        final int originalSize = waterDataListFiltered.size();
        final List<WaterData> sanitizeWaterData = SanitizerUtil.sanitizeCSVData((List) waterDataListFiltered, true);
        final int sanitizedSize = sanitizeWaterData.size();
        final int diff = Double.valueOf(originalSize * MAX_NUMBER_OF_UNDEFINED_VALUES_POURCENTAGE).intValue();
        final int originalWithout20Pourcent = originalSize - diff;
        if (originalWithout20Pourcent > sanitizedSize) return new WaterData();
        final List<WaterData> datasWithoutConsumption = sanitizeWaterData.stream().filter(waterData -> waterData.getConsumption() == 0).collect(Collectors.toList());
        final int numberOfZero = datasWithoutConsumption.size();
        final WaterData lastZeroConsumption = numberOfZero == 0 ? new WaterData() : datasWithoutConsumption.get(numberOfZero - 1);
        if (numberOfZero < MIN_ZERO_NUMBER_POURCENTAGE) return lastZeroConsumption;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(lastZeroConsumption.getEndDate());
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, NUMBER_OF_DAYS_WITHOUT_CONSUMPTION_LESS);
        final Date time = gregorianCalendar.getTime();
        final WaterData lastWaterData = sanitizeWaterData.get(sanitizeWaterData.size() - 1);
        if (time.before(lastWaterData.getBeginDate())) return lastZeroConsumption;
        return null;
    }
}
