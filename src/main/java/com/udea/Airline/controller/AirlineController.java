package com.udea.Airline.controller;

import com.udea.Airline.model.AirlineRequest;
import com.udea.Airline.model.AirlineResponse;
import com.udea.Airline.service.AirlineEvaluationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/airline")
public class AirlineController {

    private final AirlineEvaluationService service;

    public AirlineController(AirlineEvaluationService service) {
        this.service = service;
    }

    @PostMapping("/evaluate")
    public AirlineResponse evaluate(@RequestBody AirlineRequest request) {
        return service.evaluate(request);
    }
}