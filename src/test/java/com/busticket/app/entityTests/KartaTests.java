package com.busticket.app.entityTests;

import com.busticket.app.model.entity.Karta;
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
                .osnovnaCena(500.0)
                .datumIzdavanja(datum)
                .tip("regular")
                .build();
        assertThat(karta.getBrojSedista()).isEqualTo("A1");
        assertThat(karta.getOsnovnaCena()).isEqualTo(500.0);
        assertThat(karta.getDatumIzdavanja()).isEqualTo(datum);
        assertThat(karta.getTip()).isEqualTo("regular");
    }

    @Test
    public void builder_DefaultDatumIzdavanja_IsNotNull() {
        Karta karta = Karta.builder()
                .brojSedista("B2")
                .osnovnaCena(300.0)
                .tip("student")
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
                .osnovnaCena(500.0)
                .tip("regular")
                .build();
        Set<ConstraintViolation<Karta>> violations = validator.validate(karta);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenTipIsBlank() {
        Karta karta = Karta.builder()
                .brojSedista("A1")
                .osnovnaCena(500.0)
                .tip("")
                .build();
        Set<ConstraintViolation<Karta>> violations = validator.validate(karta);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenOsnovnaCenaIsNegative() {
        Karta karta = Karta.builder()
                .brojSedista("A1")
                .osnovnaCena(-100.0)
                .tip("regular")
                .build();
        Set<ConstraintViolation<Karta>> violations = validator.validate(karta);
        assertThat(violations).isNotEmpty();
    }

}
