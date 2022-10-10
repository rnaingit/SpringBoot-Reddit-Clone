package com.rohit.reddit.springredditclonebackend.service;

import com.rohit.reddit.springredditclonebackend.dto.AuthenticationResponse;
import com.rohit.reddit.springredditclonebackend.dto.LoginRequest;
import com.rohit.reddit.springredditclonebackend.dto.RegisterRequest;
import com.rohit.reddit.springredditclonebackend.exceptions.SpringRedditException;
import com.rohit.reddit.springredditclonebackend.model.NotificationEmail;
import com.rohit.reddit.springredditclonebackend.model.User;
import com.rohit.reddit.springredditclonebackend.model.VerificationToken;
import com.rohit.reddit.springredditclonebackend.repository.UserRepository;
import com.rohit.reddit.springredditclonebackend.repository.VerificationTokenRepository;
import com.rohit.reddit.springredditclonebackend.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    //making it final and adding ALlArgsConstructor achieves constructor injection
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    //spring recommends to use constructor injection whenever required.
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;



    //as we are interacting with the database use transactional annotation
    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user= new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());

        //password is encoded by the spring security
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);
        System.out.println(userRepository.findByUsername("TestUser1"));

        //JWT token is for securing the web application
        //for managing authorization
        //JWT stands for JSON web token
        //HTTP is a stateless protocol no info is stored
        String token = generateVerificationToken(user);


        mailService.sendMail(new NotificationEmail(
                "Please activate your account",
                user.getEmail(),
                "Thank you for signing up to Spring Reddit, "+
                        "please click on the below link to activate your account "+
                        "http://localhost:8080/api/auth/accountVerification/" +token
        ));
    }

    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken= verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("User not found with name: "+username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername()
        ,loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token,loginRequest.getUsername());

    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        System.out.println("Inside getCurrent User");
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
//        Jwt principal = (Jwt) SecurityContextHolder.
//                getContext().getAuthentication().getPrincipal();
        System.out.println("before return");
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " +username));
    }
}
