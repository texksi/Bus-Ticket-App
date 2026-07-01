package com.busticket.app.entityTests;

import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.model.entity.enums.NacinPlacanja;
import com.busticket.app.model.entity.enums.Role;
import com.busticket.app.model.entity.enums.StatusRezervacije;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RezervacijaTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void builder_SetsAllFields() {
        Korisnik korisnik = Korisnik.builder()
                .ime("Marko").prezime("Markovic")
                .email("marko@email.com").username("marko123")
                .password("pass").role(Role.USER).build();
        LocalDateTime datum = LocalDateTime.of(2025, 5, 10, 9, 0);
        Rezervacija rezervacija = Rezervacija.builder()
                .datumKreiranja(datum)
                .ukupanIznos(1500.0)
                .nacinPlacanja(NacinPlacanja.KARTICA)
                .status(StatusRezervacije.AKTIVNA)
                .korisnik(korisnik)
                .build();
        assertThat(rezervacija.getDatumKreiranja()).isEqualTo(datum);
        assertThat(rezervacija.getUkupanIznos()).isEqualTo(1500.0);
        assertThat(rezervacija.getNacinPlacanja()).isEqualTo(NacinPlacanja.KARTICA);
        assertThat(rezervacija.getStatus()).isEqualTo(StatusRezervacije.AKTIVNA);
        assertThat(rezervacija.getKorisnik()).isEqualTo(korisnik);
    }

    @Test
    public void builder_DefaultDatumKreiranja_IsNotNull() {
        Rezervacija rezervacija = Rezervacija.builder()
                .ukupanIznos(500.0)
                .nacinPlacanja(NacinPlacanja.GOTOVINA)
                .status(StatusRezervacije.AKTIVNA)
                .build();
        assertThat(rezervacija.getDatumKreiranja()).isNotNull();
    }

    @Test
    public void setter_ChangesStatus() {
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setStatus(StatusRezervacije.AKTIVNA);
        assertThat(rezervacija.getStatus()).isEqualTo(StatusRezervacije.AKTIVNA);
    }

    @Test
    public void noArgsConstructor_CreatesEmptyObject() {
        Rezervacija rezervacija = new Rezervacija();
        assertThat(rezervacija).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenStatusIsNull() {
        Rezervacija rezervacija = Rezervacija.builder()
                .ukupanIznos(1500.0)
                .nacinPlacanja(NacinPlacanja.KARTICA)
                .status(null)
                .build();
        Set<ConstraintViolation<Rezervacija>> violations = validator.validate(rezervacija);
        assertThat(violations).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenNacinPlacanjaIsNull() {
        Rezervacija rezervacija = Rezervacija.builder()
                .ukupanIznos(1500.0)
                .nacinPlacanja(null)
                .status(StatusRezervacije.AKTIVNA)
                .build();
        Set<ConstraintViolation<Rezervacija>> violations = validator.validate(rezervacija);
        assertThat(violations).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenUkupanIznosIsNegative() {
        Rezervacija rezervacija = Rezervacija.builder()
                .ukupanIznos(-500.0)
                .nacinPlacanja(NacinPlacanja.KARTICA)
                .status(StatusRezervacije.AKTIVNA)
                .build();
        Set<ConstraintViolation<Rezervacija>> violations = validator.validate(rezervacija);
        assertThat(violations).isNotEmpty();
    }

}
