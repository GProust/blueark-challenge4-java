package com.blueark.challenge.challenge4.service;

import com.blueark.challenge.challenge4.data.DataStorage;
import com.blueark.challenge.challenge4.data.WaterData;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaterService {

    @Autowired
    private DataStorage dataStorage;

    public List<WaterData> getSanitizedData(String id, Date startDate, Date endDate) {
        final List<WaterData> waterDatas = SanitizerUtil.sanitizeCSVData((List) dataStorage.getWaterDataById(id), true);
        return waterDatas.stream()
                .filter(waterData -> waterData.getBeginDate().after(startDate))
                .filter(waterData -> waterData.getEndDate().before(endDate))
                .collect(Collectors.toList());
    }
}
