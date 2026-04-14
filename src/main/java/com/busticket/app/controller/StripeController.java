package com.busticket.app.controller;

import com.busticket.app.model.dto.ResponseDTOs.PlacanjeResponseDTO;
import com.busticket.app.service.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/api/placanja/create-payment")
    public ResponseEntity<PlacanjeResponseDTO> createPaymentIntent(@RequestParam Long rezervacijaId,
                                                                   @RequestParam double iznos) throws StripeException {
        return ResponseEntity.status(201).body(stripeService.createPlacanje(rezervacijaId, iznos));
    }
}