package com.example.movieticketsystem.service;

import com.example.movieticketsystem.dao.SeatDAO;
import com.example.movieticketsystem.model.SeatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class TicketService {
    private final SeatDAO seatDAO;
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    public TicketService(SeatDAO seatDAO) {
        this.seatDAO = seatDAO;
    }

    public boolean reserveSeat(int seatId, String customerName, String phone, String email) {
        lock.lock();
        try {
            return seatDAO.bookSeat(seatId, customerName, phone, email);
        } finally {
            lock.unlock();
        }
    }

    public boolean cancelSeat(int seatId) {
        lock.lock();
        try {
            return seatDAO.cancelSeat(seatId);
        } finally {
            lock.unlock();
        }
    }
    
    public boolean checkEmailMatch(int seatId, String email) {
        SeatModel seat = seatDAO.getAllSeats().stream()
                .filter(s -> s.getId() == seatId)
                .findFirst()
                .orElse(null);
        
        if (seat == null) {
            return false;
        }
        
        return email.equals(seat.getEmail());
    }
}