package com.blueark.challenge.challenge4.data;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CSVDataLoader {

    final Map<String, List<WaterData>> waterDataById = new HashMap<>();
    final Map<String, List<ElectricityData>> electricityDataById = new HashMap<>();
    private Map<String, String> waterDataToLoad = Map.of("32336_16857", "data/32336_16857_Eau.csv",
            "32740", "data/32740_Eau.csv",
            "34199", "data/34199_Eau.csv",
            "45088_46311", "data/45088_46311_Eau.csv",
            "45607_44239", "data/45607_44239_Eau.csv",
            "51679_12620", "data/51679_12620_Eau.csv",
            "57130", "data/57130_Eau.csv",
            "55677_55735", "data/BATIMENT_55677_55735_Eau.csv");
    private Map<String, String> electricityDataToLoad = Map.of("32336_16857", "data/32336_16857_Electricite.csv",
            "45088_46311", "data/45088_46311_Electricite.csv",
            "45607_44239", "data/45607_44239_Electricite.csv",
            "51679_12620", "data/51679_12620_Electricite.csv",
            "55677_55733", "data/BATIMENT_55677_55733_Electricite.csv",
            "55677_55734", "data/BATIMENT_55677_55734_Electricite.csv",
            "55677_55735", "data/BATIMENT_55677_55735_Electricite.csv");

    public CSVDataLoader() {
        initDataLoading();
    }

    private void initDataLoading() {

        waterDataToLoad.forEach((key, value) -> {
            try {
                waterDataById.put(key,
                        new CsvToBeanBuilder(
                                new FileReader(
                                        getClass().getClassLoader().getResource(value).getPath()))
                                .withType(WaterData.class).build().parse());
            } catch (FileNotFoundException e) {
                log.error("Unknown file.");
            }
        });
        electricityDataToLoad.forEach((key, value) -> {
            try {
                electricityDataById.put(key,
                        new CsvToBeanBuilder(
                                new FileReader(
                                        getClass().getClassLoader().getResource(value).getPath()))
                                .withType(ElectricityData.class).build().parse());
            } catch (FileNotFoundException e) {
                log.error("Unknown file.");
            }
        });
    }
}
