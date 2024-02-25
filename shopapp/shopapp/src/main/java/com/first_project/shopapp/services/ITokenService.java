package com.first_project.shopapp.services;

import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.Token;
import com.first_project.shopapp.models.User;

public interface ITokenService {

    Token createToken(String username);

    String refreshToken(String token) throws Exception;
}
