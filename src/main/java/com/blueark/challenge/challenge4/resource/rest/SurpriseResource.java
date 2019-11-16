package com.blueark.challenge.challenge4.resource.rest;

import com.blueark.challenge.challenge4.data.SurpriseResponse;
import com.blueark.challenge.challenge4.service.SurpriseService;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SurpriseResource {

    @Autowired
    private SurpriseService surpriseService;

    @RequestMapping(value = "surprise", method = RequestMethod.GET, params = {"id", "date_start", "date_end"})
    public SurpriseResponse getSurprise(@RequestParam("id") String id,
                                        @RequestParam(value = "date_start", required = false) String startDate,
                                        @RequestParam(value = "date_end", required = false) String endDate) {
        return surpriseService.getSurpriseConsumption(id, SanitizerUtil.convertFromString(startDate), SanitizerUtil.convertFromString(endDate));
    }

    @RequestMapping(value = "holiday", method = RequestMethod.GET, params = {"id"})
    public SurpriseResponse getSurpriseOnHoliday(@RequestParam("id") String id) {
        return surpriseService.determineIfSurpriseDuringLeaving(id);
    }
}
