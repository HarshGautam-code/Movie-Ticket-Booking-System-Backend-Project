package com.bms.bms_backend.services;

import com.bms.bms_backend.dto.RegisterMovieDto;
import com.bms.bms_backend.exceptions.UnAuthorizedException;
import com.bms.bms_backend.exceptions.UserNotFoundException;
import com.bms.bms_backend.models.Artist;
import com.bms.bms_backend.models.Movie;
import com.bms.bms_backend.models.User;
import com.bms.bms_backend.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    UserService userService;
    ArtistService artistService;
    MovieRepository movieRepository;

    @Autowired
    public MovieService(UserService userService, ArtistService artistService, MovieRepository movieRepository) {
        this.userService = userService;
        this.artistService = artistService;
        this.movieRepository = movieRepository;
    }

    public Movie registerMovie(RegisterMovieDto registerMovieDto) {

       UUID movieOwnerId = registerMovieDto.getMovieOwnerUserId();

        // We need to validate is this user id actual user id or not ?
        // we should call userservice
        User movieOwner = userService.isUserIdExist(movieOwnerId);

        if(movieOwner == null){
            throw new UserNotFoundException("Movie Owner id is invalid");
        }

        if(!movieOwner.getUserType().equals("MOVIE_OWNER")){
            throw new UnAuthorizedException("User does not have access to register movie");
        }


        // Now we want to register the movie -> So, to register the movie inside movie table
        // we require movie class object
        // Now to create class object -> we are already getting the data in the dto


        Movie movie = new Movie();
        movie.setMovieName(registerMovieDto.getMovieName());
        movie.setMovieOwner(movieOwner);
        movie.setGenre(registerMovieDto.getGenre());
        movie.setLaunchDate(registerMovieDto.getLaunchDate());
        movie.setRating(0.0);
        movie.setMovieDurationInHours(registerMovieDto.getMovieDurationInHours());
        movie.setTotalIncome(0);
        movie.setTotalTicketSold(0);

        // movie.setArtists(); -> List<Artists> , List<String>
        // movie.setDirector(); -> Artist -> To set director we need to set Artist object inside this
        List<Artist> artists = artistService.fetchAllArtists(registerMovieDto.getArtists());
        movie.setArtists(artists);

        Artist director = artistService.fetchArtistByName(registerMovieDto.getDirector());
        movie.setDirector(director);


        return movieRepository.save(movie);

    }


    public Movie isMovieIdValid(UUID movieId){
        Optional<Movie> movie  = movieRepository.findById(movieId);
        return movie.orElse(null);
    }
}
