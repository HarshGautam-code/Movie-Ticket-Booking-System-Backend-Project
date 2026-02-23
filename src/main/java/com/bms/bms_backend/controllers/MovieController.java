package com.bms.bms_backend.controllers;

import com.bms.bms_backend.dto.RegisterMovieDto;
import com.bms.bms_backend.exceptions.UnAuthorizedException;
import com.bms.bms_backend.exceptions.UserNotFoundException;
import com.bms.bms_backend.models.Movie;
import com.bms.bms_backend.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }


    @PostMapping("/register")
    public ResponseEntity registerMovie(
            @RequestBody RegisterMovieDto registerMovieDto
    ){

        // We will be calling movie service -> to register movie inside movie table
        try{
            Movie movie = movieService.registerMovie(registerMovieDto);
            return new ResponseEntity<>(movie, HttpStatus.CREATED);
        }
        catch (UserNotFoundException e){
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
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }


}
