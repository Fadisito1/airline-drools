package com.udea.Airline.model;

import lombok.Data;

@Data
public class Seat {
    private String number;
    private boolean emergencyExit;
    private boolean occupied;
}