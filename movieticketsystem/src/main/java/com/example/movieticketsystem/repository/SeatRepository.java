package com.example.movieticketsystem.repository;

import com.example.movieticketsystem.model.SeatModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SeatRepository {
    private final ConcurrentHashMap<Integer, SeatModel> seatMap = new ConcurrentHashMap<>();

    public void loadSeats(List<SeatModel> seats) {
        seatMap.clear();
        for (SeatModel seat : seats) {
            seatMap.put(seat.getId(), seat);
        }
    }

    public SeatModel getSeat(int seatId) {
        return seatMap.get(seatId);
    }

    public List<SeatModel> getAllSeats() {
        return List.copyOf(seatMap.values());
    }
}