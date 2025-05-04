// package com.example.movieticketsystem.dao;

// import com.example.movieticketsystem.model.MovieModel;
// import com.example.movieticketsystem.model.ShowtimeModel;
// import com.example.movieticketsystem.model.TheaterModel;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.stereotype.Repository;

// import java.time.LocalDateTime;
// import java.util.Collections;
// import java.util.List;

// @Repository
// public class ShowtimeDAO {
//     private final JdbcTemplate jdbcTemplate;
//     private final MovieDAO movieDAO;
//     private final TheaterDAO theaterDAO;

//     public ShowtimeDAO(JdbcTemplate jdbcTemplate, MovieDAO movieDAO, TheaterDAO theaterDAO) {
//         this.jdbcTemplate = jdbcTemplate;
//         this.movieDAO = movieDAO;
//         this.theaterDAO = theaterDAO;
//     }

//     public List<ShowtimeModel> getAllShowtimes() {
//         String sql = "SELECT s.id, s.movie_id, s.theater_id, s.showtime, s.available_seats, " +
//                      "m.title, m.genre, m.ticket_price, t.name, t.total_seats " +
//                      "FROM showtimes s " +
//                      "JOIN movies m ON s.movie_id = m.id " +
//                      "JOIN theaters t ON s.theater_id = t.id";
//         try {
//             return jdbcTemplate.query(sql, (rs, rowNum) -> {
//                 MovieModel movie = new MovieModel(rs.getString("title"), rs.getString("genre"), rs.getDouble("ticket_price"));
//                 movie.setId(rs.getInt("movie_id"));

//                 TheaterModel theater = new TheaterModel(rs.getString("name"), rs.getInt("total_seats"));
//                 theater.setId(rs.getInt("theater_id"));

//                 ShowtimeModel showtime = new ShowtimeModel();
//                 showtime.setId(rs.getInt("id"));
//                 showtime.setMovie(movie);
//                 showtime.setTheater(theater);
//                 showtime.setShowtime(rs.getTimestamp("showtime").toLocalDateTime());
//                 showtime.setAvailableSeats(rs.getInt("available_seats"));
//                 return showtime;
//             });
//         } catch (Exception e) {
//             System.out.println("Lỗi khi lấy danh sách suất chiếu: " + e.getMessage());
//             return Collections.emptyList();
//         }
//     }

//     public boolean addShowtime(ShowtimeModel showtime) {
//         String sql = "INSERT INTO showtimes (movie_id, theater_id, showtime, available_seats) VALUES (?, ?, ?, ?)";
//         int rowsAffected = jdbcTemplate.update(sql, 
//             showtime.getMovie().getId(), 
//             showtime.getTheater().getId(),
//             showtime.getShowtime(), 
//             showtime.getAvailableSeats());
//         return rowsAffected > 0;
//     }

//     public boolean updateShowtime(int id, ShowtimeModel showtime) {
//         String sql = "UPDATE showtimes SET movie_id = ?, theater_id = ?, showtime = ?, available_seats = ? WHERE id = ?";
//         int rowsAffected = jdbcTemplate.update(sql, 
//             showtime.getMovie().getId(), 
//             showtime.getTheater().getId(),
//             showtime.getShowtime(), 
//             showtime.getAvailableSeats(),
//             id);
//         return rowsAffected > 0;
//     }

//     public boolean deleteShowtime(int id) {
//         String sql = "DELETE FROM showtimes WHERE id = ?";
//         int rowsAffected = jdbcTemplate.update(sql, id);
//         return rowsAffected > 0;
//     }

//     public ShowtimeModel getShowtimeById(int id) {
//         String sql = "SELECT s.id, s.movie_id, s.theater_id, s.showtime, s.available_seats, " +
//                      "m.title, m.genre, m.ticket_price, t.name, t.total_seats " +
//                      "FROM showtimes s " +
//                      "JOIN movies m ON s.movie_id = m.id " +
//                      "JOIN theaters t ON s.theater_id = t.id " +
//                      "WHERE s.id = ?";
//         return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
//             MovieModel movie = new MovieModel(rs.getString("title"), rs.getString("genre"), rs.getDouble("ticket_price"));
//             movie.setId(rs.getInt("movie_id"));

//             TheaterModel theater = new TheaterModel(rs.getString("name"), rs.getInt("total_seats"));
//             theater.setId(rs.getInt("theater_id"));

//             ShowtimeModel showtime = new ShowtimeModel();
//             showtime.setId(rs.getInt("id"));
//             showtime.setMovie(movie);
//             showtime.setTheater(theater);
//             showtime.setShowtime(rs.getTimestamp("showtime").toLocalDateTime());
//             showtime.setAvailableSeats(rs.getInt("available_seats"));
//             return showtime;
//         });
//     }
// }

package com.example.movieticketsystem.dao;

import com.example.movieticketsystem.model.MovieModel;
import com.example.movieticketsystem.model.ShowtimeModel;
import com.example.movieticketsystem.model.TheaterModel;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class ShowtimeDAO {
    private final JdbcTemplate jdbcTemplate;
    private final MovieDAO movieDAO;
    private final TheaterDAO theaterDAO;

    public ShowtimeDAO(JdbcTemplate jdbcTemplate, MovieDAO movieDAO, TheaterDAO theaterDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.movieDAO = movieDAO;
        this.theaterDAO = theaterDAO;
    }

    public List<ShowtimeModel> getAllShowtimes() {
        String sql = "SELECT s.id, s.movie_id, s.theater_id, s.showtime, s.available_seats, " +
                     "m.title, m.genre, m.ticket_price, t.name, t.total_seats " +
                     "FROM showtimes s " +
                     "JOIN movies m ON s.movie_id = m.id " +
                     "JOIN theaters t ON s.theater_id = t.id";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                MovieModel movie = new MovieModel(rs.getString("title"), rs.getString("genre"), rs.getDouble("ticket_price"));
                movie.setId(rs.getInt("movie_id"));

                TheaterModel theater = new TheaterModel(rs.getString("name"), rs.getInt("total_seats"));
                theater.setId(rs.getInt("theater_id"));

                ShowtimeModel showtime = new ShowtimeModel();
                showtime.setId(rs.getInt("id"));
                showtime.setMovie(movie);
                showtime.setTheater(theater);
                showtime.setShowtime(rs.getTimestamp("showtime").toLocalDateTime());
                showtime.setAvailableSeats(rs.getInt("available_seats"));
                return showtime;
            });
        } catch (DataAccessException e) {
            System.out.println("Lỗi khi lấy danh sách suất chiếu: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean addShowtime(ShowtimeModel showtime) {
        String sql = "INSERT INTO showtimes (movie_id, theater_id, showtime, available_seats) VALUES (?, ?, ?, ?)";
        try {
            // Hiển thị thông tin chi tiết trước khi thêm
            System.out.println("Thông tin trước khi thêm vào database:");
            System.out.println("- Movie ID: " + showtime.getMovie().getId());
            System.out.println("- Theater ID: " + showtime.getTheater().getId());
            System.out.println("- Showtime: " + showtime.getShowtime());
            System.out.println("- Available seats: " + showtime.getAvailableSeats());
            
            if (showtime.getShowtime() == null) {
                System.out.println("Lỗi: Thời gian chiếu là null");
                return false;
            }
            
            Timestamp timestamp = Timestamp.valueOf(showtime.getShowtime());
            System.out.println("- Timestamp đã chuyển đổi: " + timestamp);
            
            int rowsAffected = jdbcTemplate.update(sql, 
                showtime.getMovie().getId(), 
                showtime.getTheater().getId(),
                timestamp, 
                showtime.getAvailableSeats());
                
            System.out.println("Số dòng được thêm: " + rowsAffected);
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm suất chiếu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateShowtime(int id, ShowtimeModel showtime) {
        String sql = "UPDATE showtimes SET movie_id = ?, theater_id = ?, showtime = ?, available_seats = ? WHERE id = ?";
        try {
            // Hiển thị thông tin chi tiết trước khi cập nhật
            System.out.println("Thông tin trước khi cập nhật database:");
            System.out.println("- ID: " + id);
            System.out.println("- Movie ID: " + showtime.getMovie().getId());
            System.out.println("- Theater ID: " + showtime.getTheater().getId());
            System.out.println("- Showtime: " + showtime.getShowtime());
            System.out.println("- Available seats: " + showtime.getAvailableSeats());
            
            if (showtime.getShowtime() == null) {
                System.out.println("Lỗi: Thời gian chiếu là null");
                return false;
            }
            
            Timestamp timestamp = Timestamp.valueOf(showtime.getShowtime());
            System.out.println("- Timestamp đã chuyển đổi: " + timestamp);
            
            // Kiểm tra xem showtime có tồn tại không trước khi cập nhật
            String checkSql = "SELECT COUNT(*) FROM showtimes WHERE id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
            if (count == null || count == 0) {
                System.out.println("Lỗi: Không tìm thấy suất chiếu với ID: " + id);
                return false;
            }
            
            // Chỉ cập nhật nếu ID > 0 và showtime đã tồn tại
            if (id > 0) {
                int rowsAffected = jdbcTemplate.update(sql, 
                    showtime.getMovie().getId(), 
                    showtime.getTheater().getId(),
                    timestamp, 
                    showtime.getAvailableSeats(),
                    id);
                    
                System.out.println("Số dòng được cập nhật: " + rowsAffected);
                return rowsAffected > 0;
            } else {
                System.out.println("Lỗi: ID không hợp lệ: " + id);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật suất chiếu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteShowtime(int id) {
        // Kiểm tra ID hợp lệ
        if (id <= 0) {
            System.out.println("Lỗi: ID không hợp lệ: " + id);
            return false;
        }
        
        String sql = "DELETE FROM showtimes WHERE id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, id);
            System.out.println("Số dòng đã xóa: " + rowsAffected);
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa suất chiếu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ShowtimeModel getShowtimeById(int id) {
        // Kiểm tra ID hợp lệ
        if (id <= 0) {
            System.out.println("Lỗi: ID không hợp lệ khi lấy thông tin: " + id);
            return null;
        }
        
        String sql = "SELECT s.id, s.movie_id, s.theater_id, s.showtime, s.available_seats, " +
                     "m.title, m.genre, m.ticket_price, t.name, t.total_seats " +
                     "FROM showtimes s " +
                     "JOIN movies m ON s.movie_id = m.id " +
                     "JOIN theaters t ON s.theater_id = t.id " +
                     "WHERE s.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                MovieModel movie = new MovieModel(rs.getString("title"), rs.getString("genre"), rs.getDouble("ticket_price"));
                movie.setId(rs.getInt("movie_id"));

                TheaterModel theater = new TheaterModel(rs.getString("name"), rs.getInt("total_seats"));
                theater.setId(rs.getInt("theater_id"));

                ShowtimeModel showtime = new ShowtimeModel();
                showtime.setId(rs.getInt("id"));
                showtime.setMovie(movie);
                showtime.setTheater(theater);
                showtime.setShowtime(rs.getTimestamp("showtime").toLocalDateTime());
                showtime.setAvailableSeats(rs.getInt("available_seats"));
                return showtime;
            });
        } catch (DataAccessException e) {
            System.out.println("Lỗi khi lấy thông tin suất chiếu với ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}