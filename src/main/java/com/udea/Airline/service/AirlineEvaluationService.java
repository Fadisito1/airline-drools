package com.udea.Airline.service;

import com.udea.Airline.model.AirlineRequest;
import com.udea.Airline.model.AirlineResponse;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirlineEvaluationService {

    private final KieContainer kieContainer;

    public AirlineEvaluationService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public AirlineResponse evaluate(AirlineRequest request) {
        List<String> firedRules = new ArrayList<>();
        KieSession kieSession = kieContainer.newKieSession();

        try {
            // Guardamos el nombre de cada regla que se activa
            kieSession.addEventListener(new DefaultAgendaEventListener() {
                @Override
                public void afterMatchFired(AfterMatchFiredEvent event) {
                    firedRules.add(event.getMatch().getRule().getName());
                }
            });

            // Insertamos los hechos en la memoria de trabajo
            kieSession.insert(request.getPassenger());
            kieSession.insert(request.getFlight());
            request.getSeats().forEach(kieSession::insert);

            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }

        return new AirlineResponse(
                request.getPassenger(),
                request.getFlight(),
                request.getSeats(),
                firedRules);
    }
}