package edu.bethlehem.user_service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody RegisterRequest request) {
        logger.debug("Received register request: {}", request);
        try {
            AppUser user = authService.register(request);
            logger.debug("User registered successfully: {}", user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException exception) {
            logger.error("DataIntegrityViolationException: {}", exception.getMessage());
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (Exception exception) {
            logger.error("Exception: {}", exception.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        logger.debug("Received authenticate request: {}", request);
        try {
            AuthenticationResponse response = authService.authenticate(request);
            logger.debug("User authenticated successfully: {}", response);
            return ResponseEntity.ok(response);
        } catch (PasswordDoesntMatchException e) {
            logger.error("PasswordDoesntMatchException: {}", e.getMessage());
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception exception) {
            logger.error("Exception: {}", exception.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/verify-token")
    public ResponseEntity verifiyToken(
            Authentication authentication
    ){
        HashMap<String,String> response = new HashMap<>();
        response.put("isVerified",String.valueOf(authService.verifyToken(authentication)));
        return ResponseEntity.ok(response);
    }
}
