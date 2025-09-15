package huyvu.identityservice.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import huyvu.identityservice.dto.request.AuthenticationRequest;
import huyvu.identityservice.dto.request.IntrospectRequest;
import huyvu.identityservice.dto.request.LogoutRequest;
import huyvu.identityservice.dto.request.RefreshTokenRequest;
import huyvu.identityservice.dto.response.AuthenticationResponse;
import huyvu.identityservice.dto.response.IntrospectResponse;
import huyvu.identityservice.exception.AppException;
import huyvu.identityservice.enums.ErrorCode;
import huyvu.identityservice.model.InvalidatedToken;
import huyvu.identityservice.model.User;
import huyvu.identityservice.repository.InvalidatedTokenRepository;
import huyvu.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
//    lấy thông tin từ file cấu hình
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;
    @NonFinal
    @Value("${jwt.refreshable-duration}")

    protected long REFRESHABLE_DURATION;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);

        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);


        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }

    public void logout(LogoutRequest tokenRequest) throws ParseException, JOSEException {
        ///  bọc nó lại vì không muốn lộ thông tin chính xác, chỉ cẩn trả về ok là đủ
        try {
                // tính theo thời gian của refresh token
            SignedJWT signToken = verifyToken(tokenRequest.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            invalidatedTokenRepository.save(InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build());

        } catch (AppException e) {
            log.info("Token already expired");
        }


    }


    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken(), true);

        var jit = signJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        invalidatedTokenRepository.save(InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build());

        var userbane = signJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(userbane).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)

        );
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();


    }


    //  v2 thêm chức năng xác thức tùy trường hợp refresh hay verify
    private SignedJWT verifyToken(String token, boolean isRefesh) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
// lấy thời gian hết hạn
        Date expityTime = isRefesh
//                khi nó là refesh
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);

        if (!(verified && expityTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);


        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;

    }


    // để dùng authorization thì trong token phải có roles (key = score, các thuộc tính cách nhau bằng dấu " ", đây là lần chỉnh sửa 2
    private String generateToken(User user) {
        // quy định thuật toán cho nó.
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        // build payload, data trong body gọi là Claim

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                // giống như nguồn của token
                .issuer("Huy Vũ from devteria channel")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("customeClaim", "Custome by Huy, this is custome claim")
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        // tạo payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        // Ki token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }


    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });
        }
        return stringJoiner.toString();
    }
}


