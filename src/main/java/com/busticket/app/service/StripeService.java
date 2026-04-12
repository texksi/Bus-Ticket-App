package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.model.entity.Placanje;
import com.busticket.app.repository.PlacanjeRepository;
import com.busticket.app.repository.RezervacijaRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.secret-key}")
    private String secretKey;
    private RezervacijaRepository rezervacijaRepository;
    private PlacanjeRepository placanjeRepository;

    @PostConstruct
    public void initStripe() {
        Stripe.apiKey = secretKey;
    }

    public PaymentIntent createPaymentIntent(double iznos) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (iznos * 100))
                .setCurrency("rsd")
                .build();
        return PaymentIntent.create(params);
    }

    public Placanje createPlacanje(Long rezervacijaId, double iznos) throws StripeException {
        PaymentIntent paymentIntent = createPaymentIntent(iznos);
        Placanje placanje = Placanje.builder()
                .stripePaymentId(paymentIntent.getId())
                .iznos(iznos)
                .status(paymentIntent.getStatus())
                .rezervacija(rezervacijaRepository.findById(rezervacijaId)
                        .orElseThrow(() -> new EntityNotFoundException("Rezervacija ne postoji")))
                .build();
        return placanjeRepository.save(placanje);
    }
}

