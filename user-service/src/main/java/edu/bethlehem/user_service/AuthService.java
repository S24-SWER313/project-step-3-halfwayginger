package edu.bethlehem.user_service;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
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

    private final AppUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService service;


    private final   AuthenticationManager authenticationManager;





    public RegisterRequest register(RegisterRequest request) {
        AppUser user;

        user = AppUser.builder()
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





        return request;


    }





    public AuthenticationResponse authenticate(AuthenticationRequest request) throws PasswordDoesntMatchException {

        try {
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User Not Found Exception",
                            HttpStatus.NOT_FOUND));
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            var jwtToken = service.generateToken(user);
            return AuthenticationResponse.builder()
                    .jwtToken(jwtToken)
                    .build();
        } catch (AuthenticationException e) {
            throw new PasswordDoesntMatchException();
        }
    }



}