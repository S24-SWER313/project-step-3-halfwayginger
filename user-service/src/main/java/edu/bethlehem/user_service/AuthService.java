package edu.bethlehem.user_service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;
    private final JwtService service;

    private final AuthenticationManager authenticationManager;





    public RegisterRequest register(RegisterRequest request) {
        AppUser user;

        if (request.getRole() == Role.ACADEMIC) {
            user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .bio(request.getBio())
                    .fieldOfWork(request.getFieldOfWork())
                    .education(request.getEducation())
                    .badge(request.getBadge())
                    .position(request.getPosition())
                    .enabled(false)
                    .locked(false)
                    .build();

        } else {
            user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .bio(request.getBio())
                    .fieldOfWork(request.getFieldOfWork())
                    .type(request.getType())
                    .enabled(false)
                    .locked(false)
                    .build();
        }



        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + token;

        emailSender.send(request.getEmail(),
                buildEmail(request.getFirstName(), link));


        return request;


    }





    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        try {
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User Not Found Exception",
                            HttpStatus.NOT_FOUND));
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.trace(String.format("User %s is Authenticated", user.toString()));
            var jwtToken = service.generateToken(user);
            return AuthenticationResponse.builder()
                    .jwtToken(jwtToken)
                    .role(user.getRole())
                    .build();
        } catch (AuthenticationException e) {
            throw new PasswordDoesntMatchException();
        }
    }



}