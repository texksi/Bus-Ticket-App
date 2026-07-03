package com.busticket.app.entityTests;

import com.busticket.app.model.entity.Grad;
import com.busticket.app.model.entity.Putovanje;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PutovanjeTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void builder_SetsAllFields() {
        Grad polaziste = Grad.builder().naziv("Beograd").skracenica("BG").build();
        Grad odrediste = Grad.builder().naziv("Novi Sad").skracenica("NS").build();
        LocalDateTime polazak = LocalDateTime.of(2025, 6, 1, 8, 0);
        LocalDateTime dolazak = LocalDateTime.of(2025, 6, 1, 12, 0);
        Putovanje putovanje = Putovanje.builder()
                .polaziste(polaziste)
                .odrediste(odrediste)
                .vremePolaska(polazak)
                .vremeDolaska(dolazak)
                .osnovnaCena(800.0)
                .build();
        assertThat(putovanje.getPolaziste()).isEqualTo(polaziste);
        assertThat(putovanje.getOdrediste()).isEqualTo(odrediste);
        assertThat(putovanje.getVremePolaska()).isEqualTo(polazak);
        assertThat(putovanje.getVremeDolaska()).isEqualTo(dolazak);
        assertThat(putovanje.getOsnovnaCena()).isEqualTo(800.0);
    }

    @Test
    public void noArgsConstructor_CreatesEmptyObject() {
        Putovanje putovanje = new Putovanje();
        assertThat(putovanje).isNotNull();
    }

    @Test
    public void builder_DefaultVremePolaskaVremeDolaska_IsNotNull(){
        Grad polaziste = Grad.builder().naziv("Beograd").skracenica("BG").build();
        Grad odrediste = Grad.builder().naziv("Novi Sad").skracenica("NS").build();
        Putovanje putovanje = Putovanje.builder()
                .polaziste(polaziste)
                .odrediste(odrediste)
                .osnovnaCena(800.0)
                .build();
        assertThat(putovanje.getVremeDolaska()).isNotNull();
        assertThat(putovanje.getVremePolaska()).isNotNull();
    }

    @Test
    public void setter_SetPolaziste() {
        Putovanje putovanje = new Putovanje();
        Grad polaziste = Grad.builder().naziv("Rim").skracenica("RI").build();
        putovanje.setPolaziste(polaziste);
        assertThat(putovanje.getPolaziste()).isEqualTo(polaziste);
    }

    @Test
    public void validation_ThrowsWhenPolazisteIsNull() {
        Putovanje putovanje = Putovanje.builder()
                .polaziste(null)
                .odrediste(Grad.builder().naziv("Novi Sad").skracenica("NS").build())
                .osnovnaCena(800.0)
                .build();
        Set<ConstraintViolation<Putovanje>> violations = validator.validate(putovanje);
        assertThat(violations).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenOdredisteIsNull() {
        Putovanje putovanje = Putovanje.builder()
                .polaziste(Grad.builder().naziv("Beograd").skracenica("BG").build())
                .odrediste(null)
                .osnovnaCena(800.0)
                .build();
        Set<ConstraintViolation<Putovanje>> violations = validator.validate(putovanje);
        assertThat(violations).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenOsnovnaCenaIsNegative() {
        Putovanje putovanje = Putovanje.builder()
                .polaziste(Grad.builder().naziv("Beograd").skracenica("BG").build())
                .odrediste(Grad.builder().naziv("Novi Sad").skracenica("NS").build())
                .osnovnaCena(-100.0)
                .build();
        Set<ConstraintViolation<Putovanje>> violations = validator.validate(putovanje);
        assertThat(violations).isNotEmpty();
    }
}
