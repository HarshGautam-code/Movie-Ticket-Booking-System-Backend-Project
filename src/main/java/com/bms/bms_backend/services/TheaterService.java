package com.bms.bms_backend.services;

import com.bms.bms_backend.exceptions.UnAuthorizedException;
import com.bms.bms_backend.exceptions.UserNotFoundException;
import com.bms.bms_backend.dto.RegisterTheaterDto;
import com.bms.bms_backend.models.Theater;
import com.bms.bms_backend.models.User;
import com.bms.bms_backend.repositories.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.UUID;

@Service
public class TheaterService {

    TheaterRepository theaterRepository;
    UserService userService;


    @Autowired
    public TheaterService(TheaterRepository theaterRepository,  UserService userServise) {
        this.theaterRepository = theaterRepository;
        this.userService = userServise;
    }

    public Theater registerTheater(RegisterTheaterDto registerTheaterDto, UUID userId) {

        // 1. We want to validate userId exists in the user table or not.
        // 2. TheaterService -> UserService -> isUserIdExist()

        User theaterOwner = userService.isUserIdExist(userId);

        if(theaterOwner == null){

            // If theaterOwner is null that means we got invaild id
            // We should throw the exception that user does not exist
            throw new UserNotFoundException("User does not exist");
        }
        if(!theaterOwner.getUserType().equals("THEATER_OWNER")){

            throw new UnAuthorizedException("User does not have permission to create theater");
        }

        Theater theater = new Theater();
        theater.setTheaterName(registerTheaterDto.getTheaterName());
        theater.setCity(registerTheaterDto.getCity());
        theater.setAddress(registerTheaterDto.getAddress());
        theater.setPincode(registerTheaterDto.getPincode());
        theater.setState(registerTheaterDto.getState());
        theater.setCountry(registerTheaterDto.getCountry());
        theater.setTheaterOwner(theaterOwner);


        return theaterRepository.save(theater);
    }

    public Theater isTheaterExists(UUID theaterId){
        return theaterRepository.findById(theaterId).orElse(null);
    }
}
