package com.bms.bms_backend.controllers;

import com.bms.bms_backend.dto.RegisterTheaterDto;
import com.bms.bms_backend.exceptions.UnAuthorizedException;
import com.bms.bms_backend.exceptions.UserNotFoundException;
import com.bms.bms_backend.models.Theater;
import com.bms.bms_backend.services.TheaterService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@RestController
@RequestMapping("/api/v1/theater")
public class TheaterController {

    TheaterService theaterService;

    @Autowired
    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @PostMapping("/register")
    public ResponseEntity registerTheater(@RequestBody RegisterTheaterDto registerTheaterDto, @RequestParam UUID userId){

        try {
            Theater theater = theaterService.registerTheater(registerTheaterDto, userId);
            ResponseEntity response = new ResponseEntity(theater, HttpStatus.CREATED); // 201
            return response;
        }
        catch (UserNotFoundException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST); // 4XX // 400
        }
        catch (UnAuthorizedException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED); // 401
        }
        catch (Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }
}

