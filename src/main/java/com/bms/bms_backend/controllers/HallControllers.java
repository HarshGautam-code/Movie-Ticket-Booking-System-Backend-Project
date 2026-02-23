package com.bms.bms_backend.controllers;


import com.bms.bms_backend.dto.RegisterHallDto;
import com.bms.bms_backend.dto.RegisterHallRowMappingDto;
import com.bms.bms_backend.exceptions.TheaterNotFoundException;
import com.bms.bms_backend.exceptions.UnAuthorizedException;
import com.bms.bms_backend.exceptions.UserNotFoundException;
import com.bms.bms_backend.models.Hall;
import com.bms.bms_backend.models.HallRowMapping;
import com.bms.bms_backend.services.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hall")
public class HallControllers {

    HallService hallService;

    @Autowired
    public HallControllers(HallService hallService) {
        this.hallService = hallService;
    }

    @PostMapping("/register")
    public ResponseEntity createHall(
            @RequestBody RegisterHallDto registerHallDto,
            @RequestParam UUID userId,
            @RequestParam  UUID theaterId) {


           try {
               Hall hall = hallService.createHall(registerHallDto, userId, theaterId);
               return new ResponseEntity(hall, HttpStatus.CREATED);
           }
           catch (UserNotFoundException e){
               Map<String, String> response = new HashMap<>();
               response.put("message", e.getMessage());
               return new ResponseEntity(response, HttpStatus.BAD_REQUEST); // 4XX // 401
           }
           catch (UnAuthorizedException e){
               Map<String, String> response = new HashMap<>();
               response.put("message", e.getMessage());
               return new ResponseEntity(response, HttpStatus.UNAUTHORIZED); // 401
           }
           catch (TheaterNotFoundException e){
               Map<String, String> response = new HashMap<>();
               response.put("message", e.getMessage());
               return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
           }
           catch (Exception e){
               Map<String, String> response = new HashMap<>();
               response.put("message", e.getMessage());
               return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
           }


    }



    @PostMapping("/create-mappings")
    public ResponseEntity createHallRowMapping(
            @RequestBody List<RegisterHallRowMappingDto> mappingsDto,
            @RequestParam UUID userId
    ){
        try{
            List<HallRowMapping> mappings = hallService.createHallRowMappings(mappingsDto, userId);
            return new ResponseEntity(mappings, HttpStatus.CREATED);
        }
        catch (UserNotFoundException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST); // 4XX
        }
        catch (IllegalArgumentException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST); // 4XX
        }
        catch (UnAuthorizedException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED); // 401
        }
        catch (Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
