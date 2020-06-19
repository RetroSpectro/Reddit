package ru.mikesb.reddit.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mikesb.reddit.dto.AuthenticationResponse;
import ru.mikesb.reddit.dto.LoginRequest;
import ru.mikesb.reddit.dto.RegisterRequest;
import ru.mikesb.reddit.exceptions.SpringRedditException;
import ru.mikesb.reddit.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) throws SpringRedditException {
    authService.signup(registerRequest);
    return new ResponseEntity<>("User registration Successful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) throws SpringRedditException {
        authService.verifyAccount(token);
    return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) throws SpringRedditException {

       return authService.login(loginRequest);
    }
}
