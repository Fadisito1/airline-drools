package com.udea.Airline.model;

import lombok.Data;

@Data
public class Flight {
    private String flightNumber;
    private int delayMinutes;       // minutos de retraso
    private double durationHours;   // duracion del vuelo en horas
}