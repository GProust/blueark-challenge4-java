package com.blueark.challenge.challenge4.service;

import com.blueark.challenge.challenge4.data.DataStorage;
import com.blueark.challenge.challenge4.data.ElectricityData;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectricityService {

    @Autowired
    private DataStorage dataStorage;

    public List<ElectricityData> getSanitizedData(String id, Date startDate, Date endDate) {
        final List<ElectricityData> electricityDatas = SanitizerUtil.sanitizeCSVData((List) dataStorage.getElectricityDataById(id), false);
        return electricityDatas.stream()
                .filter(electrictyData -> electrictyData.getBeginDate().after(startDate))
                .filter(electrictyData -> electrictyData.getEndDate().before(endDate))
                .collect(Collectors.toList());
    }
}
