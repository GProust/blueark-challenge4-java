package com.blueark.challenge.challenge4.service;

import com.blueark.challenge.challenge4.data.DataStorage;
import com.blueark.challenge.challenge4.data.ElectricityData;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElectricityService {

    @Autowired
    private DataStorage dataStorage;

    public List<ElectricityData> getSanitizedData(String id) {
        return SanitizerUtil.sanitizeCSVData((List) dataStorage.getElectricityDataById(id));
    }
}
