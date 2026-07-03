package com.busticket.app.entityTests;

import com.busticket.app.model.entity.Karta;
import com.busticket.app.model.entity.enums.TipKarte;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class KartaTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void builder_SetsAllFields() {
        LocalDateTime datum = LocalDateTime.of(2025, 1, 1, 10, 0);
        Karta karta = Karta.builder()
                .brojSedista("A1")
                .finalnaCena(500.0)
                .datumIzdavanja(datum)
                .tip(TipKarte.STANDARD)
                .build();
        assertThat(karta.getBrojSedista()).isEqualTo("A1");
        assertThat(karta.getFinalnaCena()).isEqualTo(500.0);
        assertThat(karta.getDatumIzdavanja()).isEqualTo(datum);
        assertThat(karta.getTip()).isEqualTo(TipKarte.STANDARD);
    }

    @Test
    public void builder_DefaultDatumIzdavanja_IsNotNull() {
        Karta karta = Karta.builder()
                .brojSedista("B2")
                .finalnaCena(300.0)
                .tip(TipKarte.STANDARD)
                .build();

        assertThat(karta.getDatumIzdavanja()).isNotNull();
    }

    @Test
    public void setter_ChangesBrojSedista() {
        Karta karta = new Karta();
        karta.setBrojSedista("C3");
        assertThat(karta.getBrojSedista()).isEqualTo("C3");
    }

    @Test
    public void noArgsConstructor_CreatesEmptyObject() {
        Karta karta = new Karta();
        assertThat(karta).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenBrojSedistaIsBlank() {
        Karta karta = Karta.builder()
                .brojSedista("")
                .finalnaCena(500.0)
                .tip(TipKarte.STANDARD)
                .build();
        Set<ConstraintViolation<Karta>> violations = validator.validate(karta);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenTipIsNull() {
        Karta karta = Karta.builder()
                .brojSedista("A1")
                .finalnaCena(500.0)
                .tip(null)
                .build();
        Set<ConstraintViolation<Karta>> violations = validator.validate(karta);
        assertThat(violations).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenFinalnaCenaIsNegative() {
        Karta karta = Karta.builder()
                .brojSedista("A1")
                .finalnaCena(-100.0)
                .tip(TipKarte.STANDARD)
                .build();
        Set<ConstraintViolation<Karta>> violations = validator.validate(karta);
        assertThat(violations).isNotEmpty();
    }

}
