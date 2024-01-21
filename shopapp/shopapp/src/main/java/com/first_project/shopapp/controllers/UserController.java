package com.first_project.shopapp.controllers;

import com.first_project.shopapp.dtos.UserDTO;
import com.first_project.shopapp.dtos.UserLoginDTO;
import com.first_project.shopapp.exceptions.InvalidParamException;
import com.first_project.shopapp.models.User;
import com.first_project.shopapp.responses.LoginResponse;
import com.first_project.shopapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO,
                                        BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream()
                    .map(error->error.getDefaultMessage()).toList();
            return new ResponseEntity<>(messageErrors,HttpStatus.BAD_REQUEST);
        }
        try{
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                throw new InvalidParamException( "The re-entered password does not match");
            }
            return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public  ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                    BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream()
                    .map(error->error.getDefaultMessage()).toList();
            return new ResponseEntity<>(messageErrors,HttpStatus.BAD_REQUEST);
        }
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword(), userLoginDTO.getRoleId());
            return new ResponseEntity<>(LoginResponse.builder()
                                                    .message("Login successfully")
                                                    .token(token)
                                                    .build(),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
}
