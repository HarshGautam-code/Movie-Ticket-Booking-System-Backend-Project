package com.bms.bms_backend.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie")
public class Movie {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String movieName;

    @ManyToOne
    private User movieOwner;

    private double rating;
    private double totalIncome;
    private int totalTicketSold;
    private LocalDate launchDate;
    private Double movieDurationInHours;
    private String genre;

    @ManyToMany
    private List<Artist> artists;

    @ManyToOne
    private Artist director;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public User getMovieOwner() {
        return movieOwner;
    }

    public void setMovieOwner(User movieOwner) {
        this.movieOwner = movieOwner;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public int getTotalTicketSold() {
        return totalTicketSold;
    }

    public void setTotalTicketSold(int totalTicketSold) {
        this.totalTicketSold = totalTicketSold;
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

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Artist getDirector() {
        return director;
    }

    public void setDirector(Artist director) {
        this.director = director;
    }
}
