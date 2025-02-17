package com.TLU.SoundVerse.service;

// import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.request.LoginDto;
import com.TLU.SoundVerse.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
// import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
// import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
// import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.AccessLevel;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
  UserRepository userRepository;

  @NonFinal
  @Value("${jwt.signerKey}")
  protected String SIGNER_KEY;

  // @SuppressWarnings("rawtypes")
  // public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
  //   var token = request.getToken();

  //   JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

  //   SignedJWT signedJWT = SignedJWT.parse(token);

  //   Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

  //   var verified = signedJWT.verify(verifier);

  //   return IntrospectResponse.builder()
  //       .valid(verified && expiryTime.after(new Date()))
  //       .build();
  // }

  public String authenticate(LoginDto request) {
    var user = userRepository
        .findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("User Not Found"));

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

    if (!isAuthenticated) {
      throw new Error("Unauthentication");
    }

    var token = generateToken(request.getEmail());

    return token;
  }

  private String generateToken(String email) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
            .subject(email)
            .issuer("SoundVerse.com")
            .issueTime(new Date())
            .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()))
            .claim("customClaim", "khanh")
            .build();

    Payload payload = new Payload(jwtClaimsSet.toJSONObject());

    JWSObject jwsObject = new JWSObject(header, payload);

    try {
        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return jwsObject.serialize();
    } catch (JOSEException e) {
        e.printStackTrace();
        return null;
    }
}
}
