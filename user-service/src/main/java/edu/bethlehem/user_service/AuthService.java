package edu.bethlehem.user_service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AppUser register(RegisterRequest request) {
        logger.debug("Registering user with email: {}", request.getEmail());
        AppUser user = AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .bio(request.getBio())
                .fieldOfWork(request.getFieldOfWork())
                .enabled(false)
                .locked(false)
                .build();

        AppUser savedUser = userRepository.save(user);
        logger.debug("User registered: {}", savedUser);
        return savedUser;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws PasswordDoesntMatchException {
        logger.debug("Authenticating user with email: {}", request.getEmail());
        try {
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User Not Found Exception"));

            System.out.println(("Comparing Passwords, Does Match : "+String.valueOf(passwordEncoder.matches(request.getPassword(), "$2a$10$v3oQwG/cKAzAYpKjV2EpsODZ6IfWIMqfyzj9rIMfoqER8M/Zq6vSq"))));
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().jwtToken(jwtToken).build();
        } catch (AuthenticationException e) {
            logger.error("AuthenticationException: {}", e.getMessage());
            throw new PasswordDoesntMatchException();
        }
    }

    public Boolean verifyToken(Authentication authentication){

        return userRepository.findById(jwtService.extractId(authentication)).isPresent();
    }
}
