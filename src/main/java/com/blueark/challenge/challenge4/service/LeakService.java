package com.blueark.challenge.challenge4.service;

import com.blueark.challenge.challenge4.data.DataStorage;
import com.blueark.challenge.challenge4.data.LeakResponse;
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
    private static final float MIN_ZERO_NUMBER_PERCENTAGE = 0.1f;
    private static final int NUMBER_OF_DAYS_WITHOUT_CONSUMPTION_LESS = 1;

    @Autowired
    private DataStorage dataStorage;

    public LeakResponse isReturningToNoConsumption(String id, Date startDate, Date endDate) {
        final List<WaterData> waterDataListFiltered = SanitizerUtil.sanitizeDateAndFilterByPeriod(true, (List) dataStorage.getWaterDataById(id), startDate, endDate);
        final int originalSize = waterDataListFiltered.size();
        final List<WaterData> sanitizeWaterData = SanitizerUtil.sanitizeCSVData((List) waterDataListFiltered);
        final int sanitizedSize = sanitizeWaterData.size();
        final int diff = Double.valueOf(originalSize * MAX_NUMBER_OF_UNDEFINED_VALUES_POURCENTAGE).intValue();
        final int originalWithout20Pourcent = originalSize - diff;
        if (originalWithout20Pourcent > sanitizedSize)
            return new LeakResponse("UNKNOWN", "Data are not trustful enough", null);
        final List<WaterData> datasWithoutConsumption = sanitizeWaterData.stream().filter(waterData -> waterData.getConsumption() == 0).collect(Collectors.toList());
        final int numberOfZero = datasWithoutConsumption.size();
        final WaterData lastZeroConsumption = numberOfZero == 0 ? new WaterData() : datasWithoutConsumption.get(numberOfZero - 1);
        final int minZeroExpected = Double.valueOf(originalSize * MIN_ZERO_NUMBER_PERCENTAGE).intValue();
        if (numberOfZero < minZeroExpected) {
            return new LeakResponse("FAILURE", "There is a potential leak", numberOfZero == 0 ? startDate : lastZeroConsumption.getEndDate());
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(lastZeroConsumption.getEndDate());
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, NUMBER_OF_DAYS_WITHOUT_CONSUMPTION_LESS);
        final Date time = gregorianCalendar.getTime();
        final WaterData lastWaterData = sanitizeWaterData.get(sanitizeWaterData.size() - 1);
        if (time.before(lastWaterData.getBeginDate())) {
            return new LeakResponse("FAILURE", "There is a potential leak", lastZeroConsumption.getEndDate());
        }
        return new LeakResponse("OK", null, null);
    }
}
