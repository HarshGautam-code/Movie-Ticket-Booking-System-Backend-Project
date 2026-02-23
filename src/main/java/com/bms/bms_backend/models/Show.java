package com.bms.bms_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Hall hall;

    private String showName;
    private long startTimeInSec;
    private long endTimeInSec;

    @ManyToOne
    private Movie movie;

    private LocalDateTime displayStartTime;
    private LocalDateTime displayEndTime;
    private int totalTicketSold;
    private Double totalRevenue;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public long getStartTimeInSec() {
        return startTimeInSec;
    }

    public void setStartTimeInSec(long startTimeInSec) {
        this.startTimeInSec = startTimeInSec;
    }

    public long getEndTimeInSec() {
        return endTimeInSec;
    }

    public void setEndTimeInSec(long endTimeInSec) {
        this.endTimeInSec = endTimeInSec;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getDisplayStartTime() {
        return displayStartTime;
    }

    public void setDisplayStartTime(LocalDateTime displayStartTime) {
        this.displayStartTime = displayStartTime;
    }

    public LocalDateTime getDisplayEndTime() {
        return displayEndTime;
    }

    public void setDisplayEndTime(LocalDateTime displayEndTime) {
        this.displayEndTime = displayEndTime;
    }

    public int getTotalTicketSold() {
        return totalTicketSold;
    }

    public void setTotalTicketSold(int totalTicketSold) {
        this.totalTicketSold = totalTicketSold;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
