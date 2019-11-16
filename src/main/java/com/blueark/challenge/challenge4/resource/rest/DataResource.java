package com.blueark.challenge.challenge4.resource.rest;

import com.blueark.challenge.challenge4.data.ElectricityData;
import com.blueark.challenge.challenge4.data.UserData;
import com.blueark.challenge.challenge4.data.WaterData;
import com.blueark.challenge.challenge4.service.ElectricityService;
import com.blueark.challenge.challenge4.service.WaterService;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
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

    @RequestMapping(value = "water", method = RequestMethod.GET, params = {"id", "date_start", "date_end"})
    public List<WaterData> getWaterDataById(@RequestParam(value = "id") String id,
                                            @RequestParam(value = "date_start", required = false) String startDate,
                                            @RequestParam(value = "date_end", required = false) String endDate) {
        log.info("Getting data for water id {} and period {} -> {}", id, startDate, endDate);
        return waterService.getSanitizedData(id, SanitizerUtil.convertFromString(startDate), SanitizerUtil.convertFromString(endDate));
    }

    @RequestMapping(value = "electricity", method = RequestMethod.GET, params = {"id", "date_start", "date_end"})
    public List<ElectricityData> getElectricityDataById(@RequestParam("id") String id,
                                                        @RequestParam(value = "date_start", required = false) String startDate,
                                                        @RequestParam(value = "date_end", required = false) String endDate) {
        log.info("Getting data for electricity id {} and period {} -> {}", id, startDate, endDate);
        return electricityService.getSanitizedData(id, SanitizerUtil.convertFromString(startDate), SanitizerUtil.convertFromString(endDate));
    }

    @RequestMapping(value = "user", method = RequestMethod.GET, params = {"id"})
    public UserData getUser(@RequestParam("id") Long id) {
        final UserData userData = new UserData();
        userData.setUserId(id);
        return userData;
    }
}
