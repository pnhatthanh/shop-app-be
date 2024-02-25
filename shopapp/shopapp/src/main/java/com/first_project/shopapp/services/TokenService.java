package com.first_project.shopapp.services;

import com.first_project.shopapp.components.JwtTokenUtil;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.Token;
import com.first_project.shopapp.repositories.TokenRepository;
import com.first_project.shopapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@Service
public class TokenService implements ITokenService{
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Override
    public Token createToken(String username) {
        Token token= Token.builder()
                .token(UUID.randomUUID().toString())
                .expirationDate(LocalDateTime.now().plusMinutes(10))
                .user(userRepository.findByPhoneNumber(username).get())
                .build();
        return tokenRepository.save(token);
    }

    @Override
    public String refreshToken(String token) throws Exception {
        Optional<Token> tokenFound=tokenRepository.findByToken(token);
        if(tokenFound.isEmpty()){
            throw new DataNotFoundException("Cannot find token");
        }
        if(tokenFound.get().getExpirationDate().compareTo(LocalDateTime.now())<0) {
            tokenRepository.delete(tokenFound.get());
            throw new Exception("Token is expired");
        }
        return jwtTokenUtil.generateToken(tokenFound.get().getUser());
    }
}
