package edu.bethlehem.user_service;

import java.util.Collections;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {



    private final AuthService service;
    private final JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterRequest> register(
           @RequestBody RegisterRequest request) throws RegisterRequestException {
        try {
            return new ResponseEntity<>(service.register(request), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException exception) {
            throw new RegisterRequestException(exception.getLocalizedMessage(), HttpStatus.CONFLICT);

        }

    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request) throws PasswordDoesntMatchException {

        return ResponseEntity.ok(service.authenticate(request));

    }






}