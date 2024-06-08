package edu.bethlehem.user_service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Autowired
    private AppUserRepository userRepository;

    private static final String SECRET_KEY = "491ef82099212d650f7da939a3473c3ae12bb6ca0004a0b635245b9c04e4a94f";
    Logger logger = LoggerFactory.getLogger(JwtService.class);

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public Long extractId(Authentication authentication) {
        Map<String, Object> attributes;

     if (authentication instanceof JwtAuthenticationToken) {
            attributes = ((JwtAuthenticationToken) authentication).getTokenAttributes();
            logger.trace("The Auth Token Type is From JwtAuthenticationToken, the User who tries to authenticate is "
                    + attributes.toString());
            return Long.parseLong((String) attributes.get("id"));
        }
        logger.debug("IF THE TRACE REACHED HERE, I THINK AN ERROR WILL HAPPEN HERE");
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    public AppUser getUser(Authentication authentication) {
        Long userId = extractId(authentication);
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));
    }

    public AppUser getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));
    }

    public Instant extractIssuedAt(String jwtToken) {
        return extractClaim(jwtToken, Claims::getIssuedAt).toInstant();
    }

    public Instant extractExpiresAt(String token) {
        return extractClaim(token, Claims::getExpiration).toInstant();
    }

    @SuppressWarnings("deprecation")
    public Map<String, Object> extractHeaders(String token) {
        // Parse the token and extract the headers
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token);

        // Extract the headers from the parsed token
        Map<String, Object> headers = jws.getHeader();

        // Return the extracted headers
        return headers;
    }

    public Long extractId(String jwtToken) {
        Claims claim = extractClaim(jwtToken.substring(7), claims -> {
            return claims;
        });
        JSONObject json = new JSONObject(claim);
        return Long.parseLong(json.get("id").toString());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetailsImpl userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", userDetails.getId().toString());
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetailsImpl userDetails) {

        return Jwts
                .builder()
                .claims()
                .add(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (15 * 60 * 1000000))) // The Jwt Expiry Date Will
                // be 24 Hours WhileT Testing,
                // Must Change to
                // 15 minutes When Turning in The
                // Project
                .and()
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();

    }

    public boolean isTokenValid(String token, UserDetailsImpl userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    Claims extractAllClaims(String token) throws SignatureException, MalformedJwtException {

        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }

}