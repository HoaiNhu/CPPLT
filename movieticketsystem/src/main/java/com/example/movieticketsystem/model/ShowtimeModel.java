package com.example.movieticketsystem.model;

import java.time.LocalDateTime;

public class ShowtimeModel {
    private int id;
    private MovieModel movie;
    private TheaterModel theater;
    private LocalDateTime showtime;
    private int availableSeats;

    public ShowtimeModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MovieModel getMovie() {
        return movie;
    }

    public void setMovie(MovieModel movie) {
        this.movie = movie;
    }

    public TheaterModel getTheater() {
        return theater;
    }

    public void setTheater(TheaterModel theater) {
        this.theater = theater;
    }

    public LocalDateTime getShowtime() {
        return showtime;
    }

    public void setShowtime(LocalDateTime showtime) {
        this.showtime = showtime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}