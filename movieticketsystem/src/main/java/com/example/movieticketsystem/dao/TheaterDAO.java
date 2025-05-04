package com.example.movieticketsystem.dao;

import com.example.movieticketsystem.model.TheaterModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TheaterDAO {
    private final JdbcTemplate jdbcTemplate;

    public TheaterDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TheaterModel> getAllTheaters() {
        String sql = "SELECT * FROM theaters";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TheaterModel theater = new TheaterModel(
                    rs.getString("name"),
                    rs.getInt("total_seats")
            );
            theater.setId(rs.getInt("id"));
            return theater;
        });
    }

    public boolean addTheater(TheaterModel theater) {
        String sql = "INSERT INTO theaters (name, total_seats) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, theater.getName(), theater.getTotalSeats());
        return rowsAffected > 0;
    }

    public boolean updateTheater(int id, TheaterModel theater) {
        String sql = "UPDATE theaters SET name = ?, total_seats = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, theater.getName(), theater.getTotalSeats(), id);
        return rowsAffected > 0;
    }

    public boolean deleteTheater(int id) {
        String sql = "DELETE FROM theaters WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public TheaterModel getTheaterById(int id) {
        String sql = "SELECT * FROM theaters WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            TheaterModel theater = new TheaterModel(
                    rs.getString("name"),
                    rs.getInt("total_seats")
            );
            theater.setId(rs.getInt("id"));
            return theater;
        });
    }
} 