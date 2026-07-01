package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.exceptions.InvalidCredentialsException;
import com.busticket.app.model.dto.auth.AuthResponseDTO;
import com.busticket.app.model.dto.auth.LoginRequestDTO;
import com.busticket.app.model.dto.auth.RegisterRequestDTO;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.enums.Role;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final KorisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;
    
    public AuthResponseDTO register(RegisterRequestDTO dto){
        if (korisnikRepository.existsByUsername(dto.getUsername())) {
            throw new EntityAlreadyExistsException("Korisnik sa tim username-om vec postoji");
        }
        if (korisnikRepository.existsByEmail(dto.getEmail())) {
            throw new EntityAlreadyExistsException("Korisnik sa tim email-om vec postoji");
        }
        Korisnik korisnik = Korisnik.builder()
                .ime(dto.getIme())
                .prezime(dto.getPrezime())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build();
        korisnikRepository.save(korisnik);
        String token = jwtService.generateToken(korisnik);
        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO login(LoginRequestDTO dto){
        Korisnik korisnik = korisnikRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Korisnik ne postoji"));
        if (!passwordEncoder.matches(dto.getPassword(), korisnik.getPassword())) {
            throw new InvalidCredentialsException("Pogresna lozinka/username");
        }
        String token = jwtService.generateToken(korisnik);
        return new AuthResponseDTO(token);
    }

}
