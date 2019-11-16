package com.blueark.challenge.challenge4.data;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataStorage {

    final Map<String, List<WaterData>> waterDataById = new HashMap<>();
    final Map<String, List<ElectricityData>> electricityDataById = new HashMap<>();


    void addDataForElectricity(String key, List<ElectricityData> csvData) {
        electricityDataById.put(key, csvData);
    }

    void addDataForWater(String key, List<WaterData> csvData) {
        waterDataById.put(key, csvData);
    }

    public List<WaterData> getWaterDataById(String id) {
        return waterDataById.get(id);
    }

    public List<ElectricityData> getElectricityDataById(String id) {
        return electricityDataById.get(id);
    }
}
