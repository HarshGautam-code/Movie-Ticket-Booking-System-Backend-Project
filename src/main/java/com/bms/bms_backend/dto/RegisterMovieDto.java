package com.bms.bms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMovieDto {

    private String movieName;
    private UUID movieOwnerUserId;
    private LocalDate launchDate;
    private Double movieDurationInHours;
    private String genre;
    private List<String> artists;
    private String director;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public UUID getMovieOwnerUserId() {
        return movieOwnerUserId;
    }

    public void setMovieOwnerUserId(UUID movieOwnerUserId) {
        this.movieOwnerUserId = movieOwnerUserId;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public Double getMovieDurationInHours() {
        return movieDurationInHours;
    }

    public void setMovieDurationInHours(Double movieDurationInHours) {
        this.movieDurationInHours = movieDurationInHours;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
