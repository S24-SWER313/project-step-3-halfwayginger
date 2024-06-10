package edu.bethlehem.user_service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtService jwtService;

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
    public ResponseEntity<?> verifiyToken(
            Authentication authentication) {
        HashMap<String, String> response = new HashMap<>();
        response.put("isVerified Jwt Token", String.valueOf(authService.verifyToken(authentication)));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/extractUsername")
    public String extractUsername(
            String jwt) {

        return jwtService.extractUsername(jwt);
    }

    @PostMapping("/extractUserId")
    public ResponseEntity<?> extractUserId(@RequestBody String jwt) {
        try {
            logger.info("Received JWT: " + jwt);
            Long userId = jwtService.extractId(jwt);
            return ResponseEntity.ok(userId);
        } catch (Exception e) {
            logger.error("Error processing request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
