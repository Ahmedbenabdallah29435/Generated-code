package dhiabensaada.dhia.rest;

import dhiabensaada.dhia.model.AuthenticationRequest;
import dhiabensaada.dhia.model.AuthenticationResponse;
import dhiabensaada.dhia.model.DhiaUserDetails;
import dhiabensaada.dhia.service.DhiaTokenService;
import dhiabensaada.dhia.service.DhiaUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "http://localhost:4200") // Allows all endpoints in this controller

@RestController
public class AuthenticationResource {

    private final AuthenticationManager authenticationManager;
    private final DhiaUserDetailsService dhiaUserDetailsService;
    private final DhiaTokenService dhiaTokenService;

    public AuthenticationResource(final AuthenticationManager authenticationManager,
            final DhiaUserDetailsService dhiaUserDetailsService,
            final DhiaTokenService dhiaTokenService) {
        this.authenticationManager = authenticationManager;
        this.dhiaUserDetailsService = dhiaUserDetailsService;
        this.dhiaTokenService = dhiaTokenService;
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @RequestBody @Valid final AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final DhiaUserDetails userDetails = dhiaUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(dhiaTokenService.generateToken(userDetails));
        return authenticationResponse;
    }

}
