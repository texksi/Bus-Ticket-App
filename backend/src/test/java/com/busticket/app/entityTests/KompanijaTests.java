package com.busticket.app.entityTests;

import com.busticket.app.model.entity.Kompanija;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class KompanijaTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void builder_SetsAllFields() {
        Kompanija kompanija = Kompanija.builder()
                .naziv("Lasta")
                .kontakt("011123456")
                .build();
        assertThat(kompanija.getNaziv()).isEqualTo("Lasta");
        assertThat(kompanija.getKontakt()).isEqualTo("011123456");
    }

    @Test
    public void setter_ChangesNaziv() {
        Kompanija kompanija = new Kompanija();
        kompanija.setNaziv("novi naziv");
        assertThat(kompanija.getNaziv()).isEqualTo("novi naziv");
    }

    @Test
    public void noArgsConstructor_CreatesEmptyObject() {
        Kompanija kompanija = new Kompanija();
        assertThat(kompanija).isNotNull();
    }

    @Test
    public void validation_ThrowsWhenNazivIsBlank() {
        Kompanija kompanija = Kompanija.builder()
                .naziv("")
                .kontakt("011123456")
                .build();
        Set<ConstraintViolation<Kompanija>> violations = validator.validate(kompanija);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void validation_ThrowsWhenKontaktIsBlank() {
        Kompanija kompanija = Kompanija.builder()
                .naziv("Lasta")
                .kontakt("")
                .build();
        Set<ConstraintViolation<Kompanija>> violations = validator.validate(kompanija);
        assertThat(violations).isNotEmpty();
    }
}
