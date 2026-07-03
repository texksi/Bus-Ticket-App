package com.busticket.app.entityTests;

import com.busticket.app.model.entity.Placanje;
import com.busticket.app.model.entity.Rezervacija;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PlacanjeTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void builder_SetsAllFields() {
        Rezervacija rezervacija = new Rezervacija();
        LocalDateTime datum = LocalDateTime.of(2025, 3, 15, 14, 30);
        Placanje placanje = Placanje.builder()
                .stripePaymentId("123abc")
                .iznos(2000.0)
                .datum(datum)
                .status("succeeded")
                .rezervacija(rezervacija)
                .build();
        assertThat(placanje.getStripePaymentId()).isEqualTo("123abc");
        assertThat(placanje.getIznos()).isEqualTo(2000.0);
        assertThat(placanje.getDatum()).isEqualTo(datum);
        assertThat(placanje.getStatus()).isEqualTo("succeeded");
        assertThat(placanje.getRezervacija()).isEqualTo(rezervacija);
    }

    @Test
    public void builder_DefaultDatum_IsNotNull() {
        Placanje placanje = Placanje.builder()
                .stripePaymentId("123")
                .iznos(1000.0)
                .status("failed")
                .build();
        assertThat(placanje.getDatum()).isNotNull();
    }

    @Test
    public void setter_ChangesStatus() {
        Placanje placanje = new Placanje();
        placanje.setStatus("failed");
        assertThat(placanje.getStatus()).isEqualTo("failed");
    }

    @Test
    public void noArgsConstructor_CreatesEmptyObject() {
        Placanje placanje = new Placanje();
        assertThat(placanje).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenIznosIsNegative() {
        Placanje placanje = Placanje.builder()
                .stripePaymentId("123abc")
                .iznos(-500.0)
                .status("succeeded")
                .build();
        Set<ConstraintViolation<Placanje>> violations = validator.validate(placanje);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenStripePaymentIdIsBlank() {
        Placanje placanje = Placanje.builder()
                .stripePaymentId("")
                .iznos(2000.0)
                .status("succeeded")
                .build();
        Set<ConstraintViolation<Placanje>> violations = validator.validate(placanje);
        assertThat(violations).isNotEmpty();
    }
}
