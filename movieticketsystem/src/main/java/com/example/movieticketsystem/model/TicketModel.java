package com.example.movieticketsystem.model;

public class TicketModel {
    private MovieModel movie;
    private SeatModel seat;
    private ShowtimeModel showtime;  // Added showtime reference

    public TicketModel(MovieModel movie, SeatModel seat) {
        this.movie = movie;
        this.seat = seat;
    }

    public MovieModel getMovie() {
        return movie;
    }

    public SeatModel getSeat() {
        return seat;
    }
    
    public ShowtimeModel getShowtime() {
        return showtime;
    }
    
    public void setShowtime(ShowtimeModel showtime) {
        this.showtime = showtime;
    }
}