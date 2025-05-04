// package com.example.movieticketsystem.dao;

// import com.example.movieticketsystem.model.MovieModel;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.stereotype.Repository;

// import java.util.List;

// @Repository
// public class MovieDAO {
//     private final JdbcTemplate jdbcTemplate;

//     public MovieDAO(JdbcTemplate jdbcTemplate) {
//         this.jdbcTemplate = jdbcTemplate;
//     }

//     public List<MovieModel> getAllMovies() {
//         String sql = "SELECT * FROM movies";
//         return jdbcTemplate.query(sql, (rs, rowNum) -> {
//             MovieModel movie = new MovieModel(
//                     rs.getString("title"),
//                     rs.getString("genre"),
//                     rs.getDouble("ticket_price")
//             );
//             movie.setId(rs.getInt("id")); // Thêm id vào MovieModel
//             return movie;
//         });
//     }

//     public boolean addMovie(MovieModel movie) {
//         String sql = "INSERT INTO movies (title, genre, ticket_price) VALUES (?, ?, ?)";
//         int rowsAffected = jdbcTemplate.update(sql, movie.getTitle(), movie.getGenre(), movie.getTicketPrice());
//         return rowsAffected > 0;
//     }

//     public boolean updateMovie(int id, MovieModel movie) {
//         String sql = "UPDATE movies SET title = ?, genre = ?, ticket_price = ? WHERE id = ?";
//         int rowsAffected = jdbcTemplate.update(sql, movie.getTitle(), movie.getGenre(), movie.getTicketPrice(), id);
//         return rowsAffected > 0;
//     }

//     public boolean deleteMovie(int id) {
//         String sql = "DELETE FROM movies WHERE id = ?";
//         int rowsAffected = jdbcTemplate.update(sql, id);
//         return rowsAffected > 0;
//     }

//     public MovieModel getMovieById(int id) {
//         String sql = "SELECT * FROM movies WHERE id = ?";
//         return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
//             MovieModel movie = new MovieModel(
//                     rs.getString("title"),
//                     rs.getString("genre"),
//                     rs.getDouble("ticket_price")
//             );
//             movie.setId(rs.getInt("id")); // Thêm id vào MovieModel
//             return movie;
//         });
//     }
// }

package com.example.movieticketsystem.dao;

import com.example.movieticketsystem.model.MovieModel;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class MovieDAO {
    private final JdbcTemplate jdbcTemplate;

    public MovieDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MovieModel> getAllMovies() {
        String sql = "SELECT * FROM movies";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MovieModel movie = new MovieModel(
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getDouble("ticket_price")
            );
            movie.setId(rs.getInt("id"));
            return movie;
        });
    }

    public boolean addMovie(MovieModel movie) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = "INSERT INTO movies (title, genre, ticket_price) VALUES (?, ?, ?)";
            
            int rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, movie.getTitle());
                ps.setString(2, movie.getGenre());
                ps.setDouble(3, movie.getTicketPrice());
                return ps;
            }, keyHolder);
            
            if (rowsAffected > 0 && keyHolder.getKey() != null) {
                movie.setId(keyHolder.getKey().intValue());
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            System.err.println("Lỗi khi thêm phim: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMovie(int id, MovieModel movie) {
        try {
            // Kiểm tra xem phim có tồn tại không
            MovieModel existingMovie = getMovieById(id);
            if (existingMovie == null) {
                System.err.println("Không tìm thấy phim với ID: " + id);
                return false;
            }
            
            String sql = "UPDATE movies SET title = ?, genre = ?, ticket_price = ? WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, 
                movie.getTitle(), 
                movie.getGenre(), 
                movie.getTicketPrice(), 
                id
            );
            return rowsAffected > 0;
        } catch (DataAccessException e) {
            System.err.println("Lỗi khi cập nhật phim: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMovie(int id) {
        try {
            String sql = "DELETE FROM movies WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);
            return rowsAffected > 0;
        } catch (DataAccessException e) {
            System.err.println("Lỗi khi xóa phim: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public MovieModel getMovieById(int id) {
        try {
            String sql = "SELECT * FROM movies WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                MovieModel movie = new MovieModel(
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getDouble("ticket_price")
                );
                movie.setId(rs.getInt("id"));
                return movie;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            // Phim không tồn tại với ID đã cho
            System.err.println("Không tìm thấy phim với ID: " + id);
            return null;
        } catch (DataAccessException e) {
            System.err.println("Lỗi khi tìm phim theo ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}