package com.busticket.app.entityTests;

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
        LocalDateTime polazak = LocalDateTime.of(2025, 6, 1, 8, 0);
        LocalDateTime dolazak = LocalDateTime.of(2025, 6, 1, 12, 0);
        Putovanje putovanje = Putovanje.builder()
                .polaziste("Beograd")
                .odrediste("Novi Sad")
                .vremePolaska(polazak)
                .vremeDolaska(dolazak)
                .osnovnaCena(800.0)
                .build();
        assertThat(putovanje.getPolaziste()).isEqualTo("Beograd");
        assertThat(putovanje.getOdrediste()).isEqualTo("Novi Sad");
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
        Putovanje putovanje = Putovanje.builder()
                .polaziste("Beograd")
                .odrediste("Novi Sad")
                .osnovnaCena(800.0)
                .build();
        assertThat(putovanje.getVremeDolaska()).isNotNull();
        assertThat(putovanje.getVremePolaska()).isNotNull();
    }

    @Test
    public void setter_SetPolaziste() {
        Putovanje putovanje = new Putovanje();
        putovanje.setPolaziste("Rim");
        assertThat(putovanje.getPolaziste()).isEqualTo("Rim");
    }

    @Test
    public void validation_ThrowsWhenPolazisteIsBlank() {
        Putovanje putovanje = Putovanje.builder()
                .polaziste("")
                .odrediste("Novi Sad")
                .osnovnaCena(800.0)
                .build();
        Set<ConstraintViolation<Putovanje>> violations = validator.validate(putovanje);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenOdredisteIsBlank() {
        Putovanje putovanje = Putovanje.builder()
                .polaziste("Beograd")
                .odrediste("")
                .osnovnaCena(800.0)
                .build();
        Set<ConstraintViolation<Putovanje>> violations = validator.validate(putovanje);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenOsnovnaCenaIsNegative() {
        Putovanje putovanje = Putovanje.builder()
                .polaziste("Beograd")
                .odrediste("Novi Sad")
                .osnovnaCena(-100.0)
                .build();
        Set<ConstraintViolation<Putovanje>> violations = validator.validate(putovanje);
        assertThat(violations).isNotEmpty();
    }
}
