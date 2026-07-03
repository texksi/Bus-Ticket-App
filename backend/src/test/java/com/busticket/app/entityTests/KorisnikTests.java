package com.busticket.app.entityTests;

import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.enums.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class KorisnikTests {

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
                .ime("Marko")
                .prezime("Markovic")
                .email("marko@email.com")
                .username("marko123")
                .password("lozinka")
                .role(Role.USER)
                .build();

        assertThat(korisnik.getIme()).isEqualTo("Marko");
        assertThat(korisnik.getPrezime()).isEqualTo("Markovic");
        assertThat(korisnik.getEmail()).isEqualTo("marko@email.com");
        assertThat(korisnik.getUsername()).isEqualTo("marko123");
        assertThat(korisnik.getPassword()).isEqualTo("lozinka");
        assertThat(korisnik.getRole()).isEqualTo(Role.USER);
    }

    @Test
    public void setter_ChangesEmail() {
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("novi@email.com");
        assertThat(korisnik.getEmail()).isEqualTo("novi@email.com");
    }

    @Test
    public void noArgsConstructor_CreatesEmptyObject() {
        Korisnik korisnik = new Korisnik();
        assertThat(korisnik).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenImeIsBlank() {
        Korisnik korisnik = Korisnik.builder()
                .ime("")
                .prezime("Markovic")
                .email("marko@email.com")
                .username("marko123")
                .password("pass")
                .role(Role.USER)
                .build();
            Set<ConstraintViolation<Korisnik>> violations = validator.validate(korisnik);
            assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenPrezimeIsBlank() {
        Korisnik korisnik = Korisnik.builder()
                .ime("Marko")
                .prezime("")
                .email("marko@email.com")
                .username("marko123")
                .password("pass")
                .role(Role.USER)
                .build();
        Set<ConstraintViolation<Korisnik>> violations = validator.validate(korisnik);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenUsernameIsBlank() {
        Korisnik korisnik = Korisnik.builder()
                .ime("Marko")
                .prezime("Markovic")
                .email("marko@email.com")
                .username("")
                .password("pass")
                .role(Role.USER)
                .build();
        Set<ConstraintViolation<Korisnik>> violations = validator.validate(korisnik);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenPasswordIsBlank() {
        Korisnik korisnik = Korisnik.builder()
                .ime("Marko")
                .prezime("")
                .email("marko@email.com")
                .username("marko123")
                .password("")
                .role(Role.USER)
                .build();
        Set<ConstraintViolation<Korisnik>> violations = validator.validate(korisnik);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenEmailIsBlank() {
        Korisnik korisnik = Korisnik.builder()
                .ime("Marko")
                .prezime("Markovic")
                .email("")
                .username("marko123")
                .password("pass")
                .role(Role.USER)
                .build();
        Set<ConstraintViolation<Korisnik>> violations = validator.validate(korisnik);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenRoleIsNull() {
        Korisnik korisnik = Korisnik.builder()
                .ime("Marko")
                .prezime("Markovic")
                .email("marko@email.com")
                .username("marko123")
                .password("pass")
                .role(null)
                .build();
        Set<ConstraintViolation<Korisnik>> violations = validator.validate(korisnik);
        assertThat(violations).isNotEmpty();
    }



}
