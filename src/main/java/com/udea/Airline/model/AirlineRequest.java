package com.udea.Airline.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class AirlineRequest {
    private Passenger passenger;
    private Flight flight;
    private List<Seat> seats = new ArrayList<>();
}