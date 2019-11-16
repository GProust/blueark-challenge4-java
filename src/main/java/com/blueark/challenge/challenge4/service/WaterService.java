package com.blueark.challenge.challenge4.service;

import com.blueark.challenge.challenge4.data.CSVData;
import com.blueark.challenge.challenge4.data.DataStorage;
import com.blueark.challenge.challenge4.data.WaterData;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WaterService {

    @Autowired
    private DataStorage dataStorage;

    public List<WaterData> getSanitizedData(String id, Date startDate, Date endDate) {
        final List<WaterData> waterDatas = SanitizerUtil.sanitizeDateAndFilterByPeriod(true, (List) dataStorage.getWaterDataById(id), startDate, endDate);
        return SanitizerUtil.sanitizeCSVData((List) waterDatas);
    }

    public Double getAverageConsumption(String id) {
        final List<WaterData> waterDatas = SanitizerUtil.sanitizeEndDate(true, (List) dataStorage.getWaterDataById(id));
        final List<WaterData> waterDatasSanitized = SanitizerUtil.sanitizeCSVData((List) waterDatas);
        final double sum = waterDatasSanitized.stream().filter(waterData -> waterData.getConsumption() != null).map(CSVData::getConsumption).mapToDouble(Double::doubleValue).sum();
        return sum / waterDatasSanitized.size();
    }
}
