package com.first_project.shopapp.services;

import com.first_project.shopapp.components.JwtTokenUtil;
import com.first_project.shopapp.dtos.UserDTO;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.exceptions.InvalidParamException;
import com.first_project.shopapp.models.Role;
import com.first_project.shopapp.models.User;
import com.first_project.shopapp.repositories.RoleRepository;
import com.first_project.shopapp.repositories.UserRepository;
import com.first_project.shopapp.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserService implements IUserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        if(userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())){
            throw new InvalidParamException("Phone number already exists");
        }
        Role role=roleRepository.findById(userDTO.getRoleId()).orElseThrow(
                ()->new DataNotFoundException("Cannot find role with id= "+userDTO.getRoleId())
        );
        User user=new User();
        user.setFullName(userDTO.getFullName());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setActive(true);
        user.setFacebookAccountId(userDTO.getFacebookAccountId());
        user.setGoogleAccountId(userDTO.getGoogleAccountId());
        user.setRole(role);
        user.setPassword(userDTO.getPassword());
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
        }
        return userRepository.save(user);
    }

    @Override
    public String login(String phoneNumber, String password,Long roleId) throws Exception {
        Optional<User> user=userRepository.findByPhoneNumber(phoneNumber);
        if(user.isEmpty()){
            throw new InvalidParamException("Invalid phoneNumber or password");
        }
        User userExisting=user.get();
        if(userExisting.getFacebookAccountId()==0
                &&userExisting.getGoogleAccountId()==0){
            if(!roleId.equals(userExisting.getRole().getId()))
                    throw new BadCredentialsException("Invalid phoneNumber, password or role");
            if(!passwordEncoder.matches(password,userExisting.getPassword())){
                throw new BadCredentialsException("Invalid phoneNumber or password");
            }
        }
        //authenticate with spring security
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(
                phoneNumber,password,userExisting.getAuthorities()
        );
        authenticationManager.authenticate(token);
        return jwtTokenUtil.generateToken(userExisting);
    }

    @Override
    public UserResponse getUserDetails(String token) throws Exception {
        if(jwtTokenUtil.checkExpiredToken(token)){
            throw new Exception("Token is expired");
        }
        String phoneNumber=jwtTokenUtil.extraPhoneNumber(token);
        Optional<User> user=userRepository.findByPhoneNumber(phoneNumber);
        if(user.isEmpty()){
            throw new Exception ("Cannot find user");
        }
        return UserResponse.fromUser(user.get());
    }
}
