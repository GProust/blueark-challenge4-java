package com.blueark.challenge.challenge4.service;

import com.blueark.challenge.challenge4.data.DataStorage;
import com.blueark.challenge.challenge4.data.WaterData;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaterService {

    @Autowired
    private DataStorage dataStorage;

    public List<WaterData> getSanitizedData(String id) {
        return SanitizerUtil.sanitizeCSVData((List) dataStorage.getWaterDataById(id));
    }
}
