package com.codingbottle.domain.region.controller;

import com.codingbottle.domain.region.model.RegionResponse;
import com.codingbottle.domain.region.restapi.RegionApi;
import com.codingbottle.domain.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RegionController implements RegionApi {
    private final RegionService regionService;

    @Override
    public RegionResponse findAll() {
       return RegionResponse.of(regionService.findAll());
    }
}
