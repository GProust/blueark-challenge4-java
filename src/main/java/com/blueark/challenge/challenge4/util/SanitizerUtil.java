package com.blueark.challenge.challenge4.util;

import com.blueark.challenge.challenge4.data.CSVData;

import java.util.List;
import java.util.stream.Collectors;

public class SanitizerUtil {

    public static List<CSVData> sanitizeCSVData(List<CSVData> csvDatas) {
        final List<Integer> indexesOfNull = csvDatas.stream().filter(data -> data.getConsumption() == null).map(csvDatas::indexOf).collect(Collectors.toList());
        int size = indexesOfNull.size();
        for (int i = 0; i < size; i++) {
            indexesOfNull.add(indexesOfNull.get(i) + 1);
        }
        final List<CSVData> csvDataToRemove = indexesOfNull.stream().map(csvDatas::get).collect(Collectors.toList());
        return csvDatas.stream().filter(csvData -> !csvDataToRemove.contains(csvData)).collect(Collectors.toList());
    }
}
