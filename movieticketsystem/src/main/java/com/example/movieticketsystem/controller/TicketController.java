// package com.example.movieticketsystem.controller;

// import com.example.movieticketsystem.dao.MovieDAO;
// import com.example.movieticketsystem.dao.SeatDAO;
// import com.example.movieticketsystem.dao.ShowtimeDAO;
// import com.example.movieticketsystem.model.MovieModel;
// import com.example.movieticketsystem.model.SeatModel;
// import com.example.movieticketsystem.model.ShowtimeModel;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import java.util.List;
// import java.util.Map;
// import java.util.HashMap;

// @Controller
// public class TicketController {
//     private final SeatDAO seatDAO;
//     private final MovieDAO movieDAO;
//     private final ShowtimeDAO showtimeDAO;

//     @Autowired
//     public TicketController(SeatDAO seatDAO, MovieDAO movieDAO, ShowtimeDAO showtimeDAO) {
//         this.seatDAO = seatDAO;
//         this.movieDAO = movieDAO;
//         this.showtimeDAO = showtimeDAO;
//     }

//     @GetMapping("/")
//     public String index(Model model) {
//         List<MovieModel> movies = movieDAO.getAllMovies();
//         List<ShowtimeModel> showtimes = showtimeDAO.getAllShowtimes();

//         // Tạo map movieId -> List<ShowtimeModel>
//         Map<Integer, List<ShowtimeModel>> showtimesByMovie = new HashMap<>();
//         for (MovieModel movie : movies) {
//             showtimesByMovie.put(
//                 movie.getId(),
//                 showtimes.stream()
//                     .filter(s -> s.getMovie().getId() == movie.getId())
//                     .toList()
//             );
//         }

//         model.addAttribute("movies", movies);
//         model.addAttribute("showtimesByMovie", showtimesByMovie);
//         return "movie_selection";
//     }

//     @GetMapping("/tickets/select-seats")
//     public String showSeats(@RequestParam int showtimeId, Model model) {
//         ShowtimeModel showtime = showtimeDAO.getShowtimeById(showtimeId);
//         if (showtime == null) {
//             return "redirect:/";
//         }
//         List<SeatModel> seats = seatDAO.getSeatsByTheater(showtime.getTheater().getId());
//         model.addAttribute("selectedShowtime", showtime);
//         model.addAttribute("seats", seats);
//         return "index";
//     }

//     @GetMapping("/booking")
//     public String showBookingForm(@RequestParam int showtimeId, 
//                                 @RequestParam List<Integer> seatIds,
//                                 Model model) {
//         ShowtimeModel showtime = showtimeDAO.getShowtimeById(showtimeId);
//         if (showtime == null) {
//             return "redirect:/";
//         }

//         List<SeatModel> selectedSeats = seatDAO.getAllSeats().stream()
//                 .filter(seat -> seatIds.contains(seat.getId()))
//                 .toList();

//         model.addAttribute("selectedShowtime", showtime);
//         model.addAttribute("seats", selectedSeats);
//         return "booking";
//     }
// }

package com.example.movieticketsystem.controller;

import com.example.movieticketsystem.dao.MovieDAO;
import com.example.movieticketsystem.dao.SeatDAO;
import com.example.movieticketsystem.dao.ShowtimeDAO;
import com.example.movieticketsystem.model.MovieModel;
import com.example.movieticketsystem.model.SeatModel;
import com.example.movieticketsystem.model.ShowtimeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
public class TicketController {
    private final SeatDAO seatDAO;
    private final MovieDAO movieDAO;
    private final ShowtimeDAO showtimeDAO;

    @Autowired
    public TicketController(SeatDAO seatDAO, MovieDAO movieDAO, ShowtimeDAO showtimeDAO) {
        this.seatDAO = seatDAO;
        this.movieDAO = movieDAO;
        this.showtimeDAO = showtimeDAO;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<MovieModel> movies = movieDAO.getAllMovies();
        List<ShowtimeModel> showtimes = showtimeDAO.getAllShowtimes();

        // Tạo map movieId -> List<ShowtimeModel>
        Map<Integer, List<ShowtimeModel>> showtimesByMovie = new HashMap<>();
        for (MovieModel movie : movies) {
            showtimesByMovie.put(
                movie.getId(),
                showtimes.stream()
                    .filter(s -> s.getMovie().getId() == movie.getId())
                    .toList()
            );
        }

        model.addAttribute("movies", movies);
        model.addAttribute("showtimesByMovie", showtimesByMovie);
        return "movie_selection";
    }

    @GetMapping("/tickets/select-seats")
    public String showSeats(@RequestParam int showtimeId, Model model) {
        ShowtimeModel showtime = showtimeDAO.getShowtimeById(showtimeId);
        if (showtime == null) {
            return "redirect:/";
        }
        List<SeatModel> seats = seatDAO.getSeatsByTheater(showtime.getTheater().getId())
                .stream()
                .sorted(Comparator.comparing(SeatModel::getSeatRow)
                        .thenComparingInt(SeatModel::getSeatCol))
                .collect(Collectors.toList());
        model.addAttribute("selectedShowtime", showtime);
        model.addAttribute("seats", seats);
        return "index";
    }

    @GetMapping("/booking")
    public String showBookingForm(@RequestParam int showtimeId, 
                                @RequestParam List<Integer> seatIds,
                                Model model) {
        ShowtimeModel showtime = showtimeDAO.getShowtimeById(showtimeId);
        if (showtime == null) {
            return "redirect:/";
        }

        List<SeatModel> selectedSeats = seatDAO.getAllSeats().stream()
                .filter(seat -> seatIds.contains(seat.getId()))
                .sorted(Comparator.comparing(SeatModel::getSeatRow)
                        .thenComparingInt(SeatModel::getSeatCol))
                .collect(Collectors.toList());

        model.addAttribute("selectedShowtime", showtime);
        model.addAttribute("seats", selectedSeats);
        return "booking";
    }
    
    // Thêm link tới trang hủy vé
    // @GetMapping("/tickets/cancel")
    // public String showCancellationPage() {
    //     return "cancel-ticket";
    // }
}