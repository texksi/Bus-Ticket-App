package com.busticket.app.entityTests;

import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Vozilo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class VoziloTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void builder_SetsAllFields() {
        Kompanija kompanija = Kompanija.builder().naziv("Lasta").kontakt("011123456").build();
        Vozilo vozilo = Vozilo.builder()
                .registracija("BG")
                .kapacitet(50)
                .brojRedova(10)
                .brojKolona(5)
                .kompanija(kompanija)
                .build();
        assertThat(vozilo.getRegistracija()).isEqualTo("BG");
        assertThat(vozilo.getKapacitet()).isEqualTo(50);
        assertThat(vozilo.getBrojRedova()).isEqualTo(10);
        assertThat(vozilo.getBrojKolona()).isEqualTo(5);
        assertThat(vozilo.getKompanija()).isEqualTo(kompanija);
    }

    @Test
    public void setter_ChangesRegistracija() {
        Vozilo vozilo = new Vozilo();
        vozilo.setRegistracija("NS");
        assertThat(vozilo.getRegistracija()).isEqualTo("NS");
    }

    @Test
    public void noArgsConstructor_CreatesEmptyObject() {
        Vozilo vozilo = new Vozilo();
        assertThat(vozilo).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenRegistracijaIsBlank() {
        Vozilo vozilo = Vozilo.builder()
                .registracija("")
                .kapacitet(50)
                .brojRedova(10)
                .brojKolona(5)
                .build();
        Set<ConstraintViolation<Vozilo>> violations = validator.validate(vozilo);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenKapacitetIsNegative() {
        Vozilo vozilo = Vozilo.builder()
                .registracija("BG")
                .kapacitet(-1)
                .brojRedova(10)
                .brojKolona(5)
                .build();
        Set<ConstraintViolation<Vozilo>> violations = validator.validate(vozilo);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenBrojRedovaIsNegative() {
        Vozilo vozilo = Vozilo.builder()
                .registracija("BG")
                .kapacitet(50)
                .brojRedova(-10)
                .brojKolona(5)
                .build();
        Set<ConstraintViolation<Vozilo>> violations = validator.validate(vozilo);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenBrojKolonaIsNegative() {
        Vozilo vozilo = Vozilo.builder()
                .registracija("BG")
                .kapacitet(50)
                .brojRedova(10)
                .brojKolona(-5)
                .build();
        Set<ConstraintViolation<Vozilo>> violations = validator.validate(vozilo);
        assertThat(violations).isNotEmpty();
    }


}
