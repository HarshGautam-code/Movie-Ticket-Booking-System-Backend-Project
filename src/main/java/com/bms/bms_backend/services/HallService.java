package com.bms.bms_backend.services;

import com.bms.bms_backend.dto.RegisterHallDto;
import com.bms.bms_backend.dto.RegisterHallRowMappingDto;
import com.bms.bms_backend.exceptions.TheaterNotFoundException;
import com.bms.bms_backend.exceptions.UnAuthorizedException;
import com.bms.bms_backend.exceptions.UserNotFoundException;
import com.bms.bms_backend.models.Hall;
import com.bms.bms_backend.models.HallRowMapping;
import com.bms.bms_backend.models.Theater;
import com.bms.bms_backend.models.User;
import com.bms.bms_backend.repositories.HallRepository;
import com.bms.bms_backend.repositories.HallRowMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class HallService {

    HallRepository hallRepository;
    UserService userService;
    TheaterService theaterService;
    HallRowMappingRepository hallRowMappingRepository;

    @Autowired
    public HallService(HallRepository hallRepository, UserService userService, TheaterService theaterService, HallRowMappingRepository hallRowMappingRepository) {
        this.hallRepository = hallRepository;
        this.userService = userService;
        this.theaterService = theaterService;
        this.hallRowMappingRepository = hallRowMappingRepository;
    }

    public Hall createHall(RegisterHallDto registerHallDto, UUID userId, UUID theaterId) {

        User theaterOwner = userService.isUserIdExist(userId);

        if(theaterOwner == null){
            // If theaterOwner is null that means we got invaild id
            // We should throw the exception that user does not exist
            throw new UserNotFoundException("User does not exist");
        }
        if(!theaterOwner.getUserType().equals("THEATER_OWNER")){ // MOVIE_OWNER
            throw new UnAuthorizedException("User does not have permission to create theater");
        }


        Theater theater = theaterService.isTheaterExists(theaterId);

        if(theater == null){
            throw  new TheaterNotFoundException("Theater does not exists");
        }


        // here we are checking the user/theaterowner is own the  theater or not .
        if(!theater.getTheaterOwner().getId().equals(theaterOwner.getId())){
            throw new UnAuthorizedException("User doesn't own this theater");
        }


        Hall hall = new Hall();

        hall.setHallName(registerHallDto.getHallName());
        hall.setCapacity(registerHallDto.getCapacity());
        hall.setTheater(theater);

        return hallRepository.save(hall);
    }

    public Hall isHallIdValid(UUID hallId){
       Optional<Hall> hall = hallRepository.findById(hallId);
       return hall.orElse(null);
    }



   // creating Hall Row Mappings

    public List<HallRowMapping> createHallRowMappings(List<RegisterHallRowMappingDto> mappingsDto, UUID userId) {

        User theaterOwner = userService.isUserIdExist(userId);

        if(theaterOwner == null){
            // If theaterOwner is null that means we got invaild id
            // We should throw the exception that user does not exist
            throw new UserNotFoundException("User does not exist");
        }
        if(!theaterOwner.getUserType().equals("THEATER_OWNER")){ // MOVIE_OWNER
            throw new UnAuthorizedException("User does not have permission to create theater");
        }

        List<HallRowMapping> mappings = new ArrayList<>();

        for(RegisterHallRowMappingDto rowMappingDto : mappingsDto){
            UUID hallId = rowMappingDto.getHallId();
            Hall hall =  this.isHallIdValid(hallId);

            if(hall == null){
                throw new IllegalArgumentException("Invalid hall Id");
            }

            if(!hall.getTheater().getTheaterOwner().getId().equals(theaterOwner.getId())){
                throw new UnAuthorizedException("User does not own the hall");
            }

            HallRowMapping hallRowMapping = new HallRowMapping();
            hallRowMapping.setHall(hall);
            hallRowMapping.setRowRange(rowMappingDto.getRowRange());
            hallRowMapping.setSeatCount(rowMappingDto.getSeatCount());
            hallRowMapping.setRowType(rowMappingDto.getRowType());

            hallRowMapping = hallRowMappingRepository.save(hallRowMapping);

            mappings.add(hallRowMapping);
        }

        return mappings;
    }

    public HallRowMapping getHallRowMappingById(UUID hallRowMappingId) {
        return hallRowMappingRepository.findById(hallRowMappingId).orElse(null);
    }



    public List<HallRowMapping> getHallRowMappingByHall(Hall hall) {
        return hallRowMappingRepository.findByHall(hall);
    }
}
