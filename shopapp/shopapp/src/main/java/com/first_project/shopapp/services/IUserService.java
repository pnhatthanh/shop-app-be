package com.first_project.shopapp.services;

import com.first_project.shopapp.dtos.UserDTO;
import com.first_project.shopapp.exceptions.InvalidParamException;
import com.first_project.shopapp.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password) throws Exception;

}
