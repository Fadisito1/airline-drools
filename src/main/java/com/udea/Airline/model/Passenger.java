package com.udea.Airline.model;

import lombok.Data;

@Data
public class Passenger {
    // ----- Datos de entrada -----
    private String name;
    private int age;
    private String membershipStatus;      // Basic, Silver, Gold, Platinum
    private double luggageWeight;         // kg
    private String seatPreference;        // Any, Window, Aisle
    private boolean travelingWithChildren;
    private int loyaltyPoints;

    // ----- Resultados (los llenan las reglas) -----
    private boolean eligibleForUpgrade = true;
    private boolean upgradedToBusiness;
    private boolean priorityCheckIn;
    private double discountPercent;
    private String assignedSeat;
    private double compensation;
    private boolean bonusPointsApplied;
    private boolean luggageAllowed = true;
    private boolean vipLoungeAccess;
    private boolean preferentialSeat;
}