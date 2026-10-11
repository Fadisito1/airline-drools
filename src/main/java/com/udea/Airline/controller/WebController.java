package com.udea.Airline.controller;

import com.udea.Airline.model.AirlineRequest;
import com.udea.Airline.model.AirlineResponse;
import com.udea.Airline.model.Flight;
import com.udea.Airline.model.Passenger;
import com.udea.Airline.model.Seat;
import com.udea.Airline.service.AirlineEvaluationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.beans.PropertyEditorSupport;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Controller
public class WebController {

    // Nombres de las reglas en espanol (solo para mostrar en pantalla)
    private static final Map<String, String> NOMBRES_REGLAS = Map.of(
        "DenyUpgradeForOverweightLuggage", "Ascenso no permitido: equipaje de m\u00e1s de 23 kg",
        "UpgradeToBusinessClassForFrequentFlyersWithDelays", "Ascenso a clase ejecutiva: pasajero Oro/Platino con retraso de m\u00e1s de 60 minutos",
        "PriorityCheckInForSeniors", "Registro prioritario: pasajero mayor de 65 a\u00f1os",
        "DiscountForLightLuggage", "Descuento del 10% por equipaje liviano (menos de 10 kg)",
        "AssignEmergencyExitSeatToYoungAdults", "Asiento de salida de emergencia asignado: pasajero de 18 a 40 a\u00f1os",
        "CompensationForExtremeDelays", "Compensaci\u00f3n de 200 USD: retraso de m\u00e1s de 3 horas",
        "ExtraLoyaltyPointsForLongFlights", "500 puntos de lealtad extra: vuelo de m\u00e1s de 5 horas",
        "RestrictLuggageOnShortFlights", "Equipaje no permitido: m\u00e1s de 15 kg en vuelo de menos de 2 horas",
        "VipLoungeAccessForPlatinumMembers", "Acceso a la sala VIP: miembro Platino",
        "PreferentialSeatForFamilies", "Asiento preferencial: familia viajando con ni\u00f1os"
    );

    private final AirlineEvaluationService service;

    public WebController(AirlineEvaluationService service) {
        this.service = service;
    }

    // Si un campo numerico llega vacio, se toma como 0
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(int.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null || text.isBlank() ? 0 : Integer.parseInt(text.trim()));
            }
        });
        binder.registerCustomEditor(double.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null || text.isBlank() ? 0.0 : Double.parseDouble(text.trim()));
            }
        });
    }

    @GetMapping("/")
    public String mostrarFormulario(Model model) {
        AirlineRequest request = new AirlineRequest();
        request.setPassenger(new Passenger());
        request.setFlight(new Flight());
        model.addAttribute("airlineRequest", request);
        return "form";
    }

    @PostMapping("/evaluar")
    public String evaluar(@ModelAttribute("airlineRequest") AirlineRequest request,
                          @RequestParam(defaultValue = "false") boolean emergencySeatAvailable,
                          Model model) {

        List<Seat> seats = new ArrayList<>();

        Seat exitSeat = new Seat();
        exitSeat.setNumber("12A");
        exitSeat.setEmergencyExit(true);
        exitSeat.setOccupied(!emergencySeatAvailable);
        seats.add(exitSeat);

        Seat normalSeat = new Seat();
        normalSeat.setNumber("14C");
        normalSeat.setEmergencyExit(false);
        normalSeat.setOccupied(false);
        seats.add(normalSeat);

        request.setSeats(seats);

        AirlineResponse response = service.evaluate(request);
        model.addAttribute("resp", response);
        model.addAttribute("nombresEs", NOMBRES_REGLAS);
        return "resultado";
    }
}