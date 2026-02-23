package com.bms.bms_backend.services;

import com.bms.bms_backend.dto.RegisterUserDto;
import com.bms.bms_backend.models.User;
import com.bms.bms_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;


@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(RegisterUserDto registerUserDto) {

        User user = new User();

        user.setUserName(registerUserDto.getUserName());
        user.setPassword(registerUserDto.getPassword());
        user.setEmail(registerUserDto.getEmail());
        user.setPhoneNumber(registerUserDto.getPhoneNumber());
        user.setAddress(registerUserDto.getAddress());
        user.setPincode(registerUserDto.getPincode());
        user.setCity(registerUserDto.getCity());
        user.setCountry(registerUserDto.getCountry());
        user.setState(registerUserDto.getState());
        user.setUserType(registerUserDto.getUserType());

        // We need to take this user object and save it inside the user table.
        return userRepository.save(user);
    }

    public User isUserIdExist(UUID userId) {

       return userRepository.findById(userId).orElse(null);
    }
}
