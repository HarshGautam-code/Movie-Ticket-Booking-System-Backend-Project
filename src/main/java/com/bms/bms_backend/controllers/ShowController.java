package com.bms.bms_backend.controllers;

import com.bms.bms_backend.dto.*;
import com.bms.bms_backend.exceptions.UnAuthorizedException;
import com.bms.bms_backend.exceptions.UserNotFoundException;
import com.bms.bms_backend.models.Bill;
import com.bms.bms_backend.models.Show;
import com.bms.bms_backend.models.ShowPriceMapping;
import com.bms.bms_backend.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ser.jdk.NumberSerializers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/show")
public class ShowController {

  ShowService showService;

   @Autowired
    public ShowController(ShowService showService) {
        this.showService = showService;
    }


    @PostMapping("/register")
    public ResponseEntity createShow(
            @RequestBody RegisterShowDto registerShowDto,
            @RequestParam UUID movieId,
            @RequestParam UUID hallId,
            @RequestParam UUID userId
    ){
        try {
            Show show = showService.createShow(movieId, hallId, userId, registerShowDto);
            return new ResponseEntity<>(show, HttpStatus.CREATED);
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


    // create Show Price mapping
    @PostMapping("/create-price-mapping")
    public ResponseEntity createShowPriceMapping(
            @RequestBody List<RegisterShowPriceMappingDto> mappingDtos,
            @RequestParam UUID userId
    ){
        try{
            List<ShowPriceMapping> priceMappings = showService.createPriceMappings(userId, mappingDtos);
            return new ResponseEntity(priceMappings, HttpStatus.CREATED);
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



    //  Showing Seat Status

    @GetMapping("/show-seat-status/{showId}")
    public List<SeatStatusDto> fetchShowSeatStatus(
            @PathVariable UUID showId
    ){
        return showService.fetchAllSeatStatus(showId);
    }



    //  Booking Seat and Getting bill

    @PostMapping("/book-seat/{userId}/{showId}")
    public ResponseEntity createSeatBooking(
            @PathVariable UUID userId,
            @PathVariable UUID showId,
            @RequestBody RegisterSeatBookingDto seatBookingDto
    ){

       try {
           Bill bill =  showService.bookSeatForShow(showId, userId, seatBookingDto);
           return new ResponseEntity<>(bill, HttpStatus.CREATED);
       }
       catch (IllegalArgumentException e) {
           Map<String, String> response = new HashMap<>();
           response.put("message", e.getMessage());
           return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
       }
       catch (Exception e){
           Map<String, String> response = new HashMap<>();
           response.put("message", e.getMessage());
           return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }


}
