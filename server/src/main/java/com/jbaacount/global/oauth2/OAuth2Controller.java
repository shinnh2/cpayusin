package com.jbaacount.global.oauth2;

import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.service.AuthenticationService;
import com.jbaacount.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
@RestController
public class OAuth2Controller
{
    private final OAuth2Service oAuth2Service;

    @GetMapping("/code/{registrationId}")
    public ResponseEntity getGoogleUser(@RequestParam("code") String code,
                                        @PathVariable("registrationId") String registrationId)
    {
        OAuth2Response response = oAuth2Service.oauth2Login(code, registrationId);

        return new ResponseEntity(new GlobalResponse<>(response), HttpStatus.OK);
    }
}
