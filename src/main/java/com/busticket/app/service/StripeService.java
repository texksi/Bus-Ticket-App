package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.PlacanjeMapper;
import com.busticket.app.model.dto.ResponseDTOs.PlacanjeResponseDTO;
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

/**
 * Servis za upravljanje placanjima putem Stripe payment gateway-a.
 * Omogucava kreiranje PaymentIntent-a na Stripe platformi i cuvanje placanja u bazi podataka.
 */
@Service
@RequiredArgsConstructor
public class StripeService {


    /**
     * Inicijalizuje Stripe API kljuc pri pokretanju aplikacije.
     */
    @Value("${stripe.secret-key}")
    private String secretKey;
    private final RezervacijaRepository rezervacijaRepository;
    private final PlacanjeRepository placanjeRepository;
    private final PlacanjeMapper placanjeMapper;

    @PostConstruct
    public void initStripe() {
        Stripe.apiKey = secretKey;
    }

    /**
     * Kreira novi Stripe PaymentIntent za prosledjeni iznos.
     *
     * @param iznos - iznos placanja u dinarima
     * @return PaymentIntent - Stripe objekat koji predstavlja nameru za placanje
     * @throws StripeException - ukoliko dodje do greske pri komunikaciji sa Stripe API-jem
     */
    public PaymentIntent createPaymentIntent(double iznos) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (iznos * 100))
                .setCurrency("rsd")
                .build();
        return PaymentIntent.create(params);
    }

    /**
     * Kreira PaymentIntent na Stripe platformi i cuva placanje u bazi podataka vezano za odredjenu rezervaciju.
     *
     * @param rezervacijaId - jedinstveni identifikator rezervacije za koju se vrsi placanje
     * @param iznos         - iznos placanja u dinarima
     * @return PlacanjeResponseDTO - objekat koji sadrzi podatke o kreiranom placanju
     * @throws StripeException         - ukoliko dodje do greske pri komunikaciji sa Stripe API-jem
     * @throws EntityNotFoundException - ukoliko rezervacija sa datim ID-om ne postoji u sistemu
     */
    public PlacanjeResponseDTO createPlacanje(Long rezervacijaId, double iznos) throws StripeException {
        PaymentIntent paymentIntent = createPaymentIntent(iznos);
        Placanje placanje = Placanje.builder()
                .stripePaymentId(paymentIntent.getId())
                .iznos(iznos)
                .status(paymentIntent.getStatus())
                .rezervacija(rezervacijaRepository.findById(rezervacijaId)
                        .orElseThrow(() -> new EntityNotFoundException("Rezervacija ne postoji")))
                .build();
        Placanje placanjeSaved = placanjeRepository.save(placanje);
        return placanjeMapper.toResponse(placanjeSaved);
    }
}

