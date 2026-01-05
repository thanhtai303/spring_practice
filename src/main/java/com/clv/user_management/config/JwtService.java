package com.clv.user_management.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.clv.user_management.model.User;
import com.clv.user_management.service.UserService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class JwtService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${server.port}")
    private String serverPort;

    public String generateToken(User usr) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(usr.getId().toString())
                .claim("email", usr.getEmail())
                .issuer(jwtIssuer)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + jwtExpiration))
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes(StandardCharsets.UTF_8)));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Error signing JWT", e);
        }
    }

    public boolean isTokenValid(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes(StandardCharsets.UTF_8));
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified = signedJWT.verify(verifier);
            return verified && expirationTime.after(new Date());
        } catch (Exception e) {
            return false;
        }

    }

    public String loginHandle(String email, String rawPassword) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, rawPassword);
        Authentication authentication = authenticationManager.authenticate(authToken);
        if (authentication == null) {
            throw new RuntimeException("Authentication failed for user: " + email);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User usr = userService.getUserByEmail(email).getData();

        return generateToken(usr);
    }

}