package com.busticket.app.entityTests;

import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Ocena;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.enums.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class OcenaTests {

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
                .ime("Ana").prezime("Anic")
                .email("ana@email.com").username("ana123")
                .password("pass").role(Role.USER).build();
        Putovanje putovanje = Putovanje.builder()
                .polaziste("Beograd").odrediste("Nis")
                .osnovnaCena(700.0).build();
        Ocena ocena = Ocena.builder()
                .komentar("Odlicno")
                .ocena(5)
                .korisnik(korisnik)
                .putovanje(putovanje)
                .build();
        assertThat(ocena.getKomentar()).isEqualTo("Odlicno");
        assertThat(ocena.getOcena()).isEqualTo(5);
        assertThat(ocena.getKorisnik()).isEqualTo(korisnik);
        assertThat(ocena.getPutovanje()).isEqualTo(putovanje);
    }

    @Test
    public void setter_ChangesKomentar() {
        Ocena ocena = new Ocena();
        ocena.setKomentar("ok");
        assertThat(ocena.getKomentar()).isEqualTo("ok");
    }

    @Test
    public void noArgsConstructor_CreatesEmptyObject() {
        Ocena ocena = new Ocena();
        assertThat(ocena).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenKomentarIsBlank() {
        Ocena ocena = Ocena.builder()
                .komentar("")
                .ocena(4)
                .build();
        Set<ConstraintViolation<Ocena>> violations = validator.validate(ocena);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenOcenaIsNegative() {
        Ocena ocena = Ocena.builder()
                .komentar("Odlicno")
                .ocena(-1)
                .build();
        Set<ConstraintViolation<Ocena>> violations = validator.validate(ocena);
        assertThat(violations).isNotEmpty();
    }
}
