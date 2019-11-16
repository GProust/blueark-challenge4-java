package com.blueark.challenge.challenge4.resource.rest;

import com.blueark.challenge.challenge4.data.LeakResponse;
import com.blueark.challenge.challenge4.service.LeakService;
import com.blueark.challenge.challenge4.util.SanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeakResource {

    @Autowired
    private LeakService leakService;

    @RequestMapping(value = "leak", method = RequestMethod.GET, params = {"id", "date_start", "date_end"})
    public LeakResponse getLeak(@RequestParam("id") String id,
                                @RequestParam(value = "date_start", required = false) String startDate,
                                @RequestParam(value = "date_end", required = false) String endDate) {
        return leakService.isReturningToNoConsumption(id, SanitizerUtil.convertFromString(startDate), SanitizerUtil.convertFromString(endDate));
    }
}
