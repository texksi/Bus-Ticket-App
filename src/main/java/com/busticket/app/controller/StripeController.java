package com.busticket.app.controller;

import com.busticket.app.model.dto.ResponseDTOs.PlacanjeResponseDTO;
import com.busticket.app.service.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler za upravljanje placanjima putem Stripe payment gateway-a.
 * Sadrzi REST API endpoint za kreiranje Stripe PaymentIntent-a i cuvanje placanja u sistemu.
 */
@RestController
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    /**
     * Metoda koja kreira novi Stripe PaymentIntent i cuva placanje u sistemu vezano za odredjenu rezervaciju.
     * Iznos i ID rezervacije se prosledjuju kao query parametri.
     *
     * @param rezervacijaId - jedinstveni identifikator rezervacije za koju se vrsi placanje
     * @param iznos         - iznos placanja u dinarima
     * @return ResponseEntity sa PlacanjeResponseDTO objektom i HTTP statusom 201
     * @throws StripeException - ukoliko dodje do greske pri komunikaciji sa Stripe API-jem
     */
    @PostMapping("/api/placanja/create-payment")
    public ResponseEntity<PlacanjeResponseDTO> createPaymentIntent(@RequestParam Long rezervacijaId,
                                                                   @RequestParam double iznos) throws StripeException {
        return ResponseEntity.status(201).body(stripeService.createPlacanje(rezervacijaId, iznos));
    }
}