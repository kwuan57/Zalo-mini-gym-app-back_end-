package com.example.gym.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.gym.dto.request.requestService.AuthenticationRequest;
import com.example.gym.dto.request.requestService.IntrospectRequest;
import com.example.gym.dto.request.requestService.LogoutRequest;
import com.example.gym.dto.request.requestService.RefreshRequest;
import com.example.gym.dto.response.respinseService.AuthenticationResponse;
import com.example.gym.dto.response.respinseService.IntrospectResponse;
import com.example.gym.entity.InvalidateToken;
import com.example.gym.entity.User;
import com.example.gym.exception.AppException;
import com.example.gym.exception.ErrorCode;
import com.example.gym.repository.InvalidateTokenRepository;
import com.example.gym.repository.UserRepository;
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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidateTokenRepository invalidateTokenRepository;
    PasswordEncoder passwordEncoder;
    @NonFinal
    @Value("${jwt.signerkey}")
    protected String signerKey;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected Long validDuration;

    @NonFinal
    @Value("${jwt.refresh-duration}")
    protected Long refreshDuration;

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();

        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expirytime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidateToken invalidateToken = InvalidateToken.builder()
                    .id(jit)
                    .expityTime(expirytime)
                    .build();
            invalidateTokenRepository.save(invalidateToken);
        } catch (AppException e) {
            log.info("Token alredy expired");
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken(), true);
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expityTime = signJWT.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidateToken = InvalidateToken.builder()
                .id(jit)
                .expityTime(expityTime)
                .build();
        invalidateTokenRepository.save(invalidateToken);
        var phoneNumber = signJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new AppException(ErrorCode.NOT_USER));
        var token = genarateToken(user);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByPhoneNumber(request.getPhoneNumber()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = genarateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expityTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(refreshDuration, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verifiered = signedJWT.verify(verifier);

        if (!(verifiered && expityTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    // Ham viet ra token cho user
    private String genarateToken(User user) {
        // chuoi dau cua token
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getPhoneNumber())
                .issuer("Gym&Fitness.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", builScope(user))
                .claim("userId", user.getId())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot creat token", e);
            throw new RuntimeException(e);
        }
    }

    private String builScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        Optional.ofNullable(user.getRole()).ifPresent(role -> stringJoiner.add("ROLE_" + role.getName()));

        return stringJoiner.toString();
    }
}
