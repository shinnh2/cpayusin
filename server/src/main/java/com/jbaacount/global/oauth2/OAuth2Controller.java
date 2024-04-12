package com.jbaacount.global.oauth2;

import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
@RestController
public class OAuth2Controller
{
    private final AuthenticationService authenticationService;

    @GetMapping("/code/{registrationId}")
    public ResponseEntity getGoogleUser(@RequestParam("code") String code,
                                        @PathVariable("registrationId") String registrationId)
    {
        OAuth2Response response = authenticationService.oauth2Login(code, registrationId);

        return new ResponseEntity(new GlobalResponse<>(response), HttpStatus.OK);
    }
}
