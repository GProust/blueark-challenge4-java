package com.blueark.challenge.challenge4.resource.rest;

import com.blueark.challenge.challenge4.data.ElectricityData;
import com.blueark.challenge.challenge4.data.WaterData;
import com.blueark.challenge.challenge4.service.ElectricityService;
import com.blueark.challenge.challenge4.service.WaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class DataResource {

    @Autowired
    private ElectricityService electricityService;
    @Autowired
    private WaterService waterService;

    @RequestMapping(value = "water", method = RequestMethod.GET, params = "id")
    public List<WaterData> getWaterDataById(@RequestParam("id") String id) {
        log.info("Getting data for id {}", id);
        return waterService.getSanitizedData(id);
    }

    @RequestMapping(value = "electricity", method = RequestMethod.GET, params = "id")
    public List<ElectricityData> getElectricityDataById(@RequestParam("id") String id) {
        log.info("Getting data for id {}", id);
        return electricityService.getSanitizedData(id);
    }
}
