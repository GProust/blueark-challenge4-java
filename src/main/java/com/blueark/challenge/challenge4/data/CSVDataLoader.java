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

    Map<String, List<WaterData>> dataById = new HashMap<>();
    private Map<String, String> dataToLoad = Map.of("32336_16857", "data/32336_16857_Eau.csv",
            "32740", "data/32740_Eau.csv",
            "34199", "data/34199_Eau.csv",
            "45088_46311", "data/45088_46311_Eau.csv",
            "45607_44239", "data/45607_44239_Eau.csv",
            "51679_12620", "data/51679_12620_Eau.csv",
            "57130", "data/57130_Eau.csv",
            "55677_55735", "data/BATIMENT_55677_55735_Eau.csv");

    public CSVDataLoader() {
        initDataLoading();
    }

    private void initDataLoading() {

        dataToLoad.entrySet().forEach(entry ->
        {
            try {
                dataById.put(entry.getKey(),
                        new CsvToBeanBuilder(
                                new FileReader(
                                        getClass().getClassLoader().getResource(entry.getValue()).getPath()))
                                .withType(WaterData.class).build().parse());
            } catch (FileNotFoundException e) {
                log.error("Unknown file.");
            }
        });
        log.info("Map generated : {}", dataById);
    }
}
