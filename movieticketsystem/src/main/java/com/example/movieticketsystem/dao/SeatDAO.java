package com.example.movieticketsystem.dao;

import com.example.movieticketsystem.model.SeatModel;
import com.example.movieticketsystem.repository.SeatRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class SeatDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SeatRepository seatRepository;
    private final ReentrantLock lock = new ReentrantLock();

    public SeatDAO(JdbcTemplate jdbcTemplate, SeatRepository seatRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.seatRepository = seatRepository;
        loadSeatsToMemory();
    }

    private void loadSeatsToMemory() {
        List<SeatModel> seats = jdbcTemplate.query("SELECT * FROM seats", (rs, rowNum) -> {
            SeatModel seat = new SeatModel(
                    rs.getInt("id"),
                    rs.getString("seat_row"),
                    rs.getInt("seat_col"),
                    rs.getString("seat_status"),
                    rs.getInt("theater_id")
            );
            seat.setCustomerName(rs.getString("customer_name"));
            seat.setPhone(rs.getString("phone"));
            seat.setEmail(rs.getString("email"));
            return seat;
        });
        seatRepository.loadSeats(seats);
    }

    public List<SeatModel> getAllSeats() {
        return seatRepository.getAllSeats();
    }

    public boolean bookSeat(int seatId, String customerName, String phone, String email) {
        String sql = "UPDATE seats SET seat_status = 'booked', customer_name = ?, phone = ?, email = ? " +
                    "WHERE id = ? AND seat_status = 'available'";
        int rowsAffected = jdbcTemplate.update(sql, customerName, phone, email, seatId);
        
        if (rowsAffected > 0) {
            // Cập nhật cache
            SeatModel seat = seatRepository.getSeat(seatId);
            if (seat != null) {
                seat.getAvailable().set(false);
                seat.setCustomerName(customerName);
                seat.setPhone(phone);
                seat.setEmail(email);
            }
            return true;
        }
        return false;
    }

    public boolean cancelSeat(int seatId) {
        String sql = "UPDATE seats SET seat_status = 'available', customer_name = NULL, phone = NULL, email = NULL " +
                    "WHERE id = ? AND seat_status = 'booked'";
        int rowsAffected = jdbcTemplate.update(sql, seatId);
        
        if (rowsAffected > 0) {
            // Cập nhật cache
            SeatModel seat = seatRepository.getSeat(seatId);
            if (seat != null) {
                seat.getAvailable().set(true);
                seat.setCustomerName(null);
                seat.setPhone(null);
                seat.setEmail(null);
            }
            return true;
        }
        return false;
    }

    public boolean updateSeatStatus(int seatId, String status) {
        String sql = "UPDATE seats SET seat_status = ? WHERE id = ? AND " +
                    "(seat_status = 'available' OR (seat_status = 'selected' AND ? = 'available'))";
        int rowsAffected = jdbcTemplate.update(sql, status, seatId, status);
        
        if (rowsAffected > 0) {
            // Cập nhật cache
            SeatModel seat = seatRepository.getSeat(seatId);
            if (seat != null) {
                if ("selected".equals(status)) {
                    seat.getAvailable().set(false);
                } else if ("available".equals(status)) {
                    seat.getAvailable().set(true);
                }
            }
            return true;
        }
        return false;
    }

    public List<SeatModel> getSeatsByTheater(int theaterId) {
        return seatRepository.getAllSeats().stream()
            .filter(seat -> seat.getTheaterId() == theaterId)
            .toList();
    }
}