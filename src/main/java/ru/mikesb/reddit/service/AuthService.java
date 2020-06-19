package ru.mikesb.reddit.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mikesb.reddit.dto.AuthenticationResponse;
import ru.mikesb.reddit.dto.LoginRequest;
import ru.mikesb.reddit.dto.RegisterRequest;
import ru.mikesb.reddit.exceptions.SpringRedditException;
import ru.mikesb.reddit.model.NotificationEmail;
import ru.mikesb.reddit.model.User;
import ru.mikesb.reddit.model.VerificationToken;
import ru.mikesb.reddit.repository.UserRepository;
import ru.mikesb.reddit.repository.VerificationTokenRepository;
import ru.mikesb.reddit.security.JwtProvider;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor

public class AuthService {

    private  final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private  final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider provider;

    @Transactional
    public void signup(RegisterRequest registerRequest) throws SpringRedditException {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

     String token =  generateVerificationToken(user);

     Logger logger = LoggerFactory.getLogger(AuthService.class);

        try {
         mailService.sendEmail(new NotificationEmail("Please activate your acc", user.getEmail(), "Thank u for singing up to Spring Reddit, " +
                 "please click on the url below to activate your account: " +
                 "http://localhost:8080/api/auth/accountVerification/" + token));
     }catch (Exception e) {
            logger.error("Exception occur while send mail :");
        }
     }

    private String generateVerificationToken(User user)
    {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) throws SpringRedditException {
       Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
       verificationToken.orElseThrow(()-> new SpringRedditException("Invalid token"));
       fetchUserAndEnable(verificationToken.get());
    }

    private void  fetchUserAndEnable(VerificationToken token) throws SpringRedditException {

       @NotBlank(message = "Username is required") String username = token.getUser().getUsername();
       User user = userRepository.findByUsername(username).orElseThrow(()->new SpringRedditException("User "+ username+ " not found" ));
        user.setEnabled(true);
        userRepository.save(user);
    }


    public AuthenticationResponse login(LoginRequest loginRequest) throws SpringRedditException {
     Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = provider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
