package huyvu.identityservice.controller;


import com.nimbusds.jose.JOSEException;
import huyvu.identityservice.dto.request.ApiResponse;
import huyvu.identityservice.dto.request.AuthenticationRequest;
import huyvu.identityservice.dto.request.IntrospectRequest;
import huyvu.identityservice.dto.request.LogoutRequest;
import huyvu.identityservice.dto.response.AuthenticationResponse;
import huyvu.identityservice.dto.response.IntrospectResponse;
import huyvu.identityservice.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> logIn(@RequestBody AuthenticationRequest request) {

        AuthenticationResponse result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder().
                result(result)
                .build();

    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> logIn(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {

        var result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder().
                result(result)
                .build();
}

    @PostMapping("/logout")
    ApiResponse<Void> logOut(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {

        authenticationService.logout(logoutRequest);


        return ApiResponse.<Void>builder()
                .build();
    }
}
