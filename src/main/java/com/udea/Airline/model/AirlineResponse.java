package com.udea.Airline.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class AirlineResponse {
    private Passenger passenger;
    private Flight flight;
    private List<Seat> seats;
    private List<String> firedRules;
}
