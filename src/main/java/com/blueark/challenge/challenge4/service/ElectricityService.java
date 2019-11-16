package com.blueark.challenge.challenge4.service;

import com.blueark.challenge.challenge4.data.DataStorage;
import com.blueark.challenge.challenge4.data.ElectricityData;
import com.blueark.challenge.challenge4.data.WaterData;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ElectricityService {

    @Autowired
    private DataStorage dataStorage;

    public List<ElectricityData> getSanitizedData(String id, Date startDate, Date endDate) {
        final List<WaterData> waterDatas = SanitizerUtil.sanitizeDateAndFilterByPeriod(false, (List) dataStorage.getElectricityDataById(id), startDate, endDate);
        return SanitizerUtil.sanitizeCSVData((List) waterDatas);
    }
}
