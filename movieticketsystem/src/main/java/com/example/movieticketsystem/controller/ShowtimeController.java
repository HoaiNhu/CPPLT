// package com.example.movieticketsystem.controller;

// import com.example.movieticketsystem.dao.MovieDAO;
// import com.example.movieticketsystem.dao.ShowtimeDAO;
// import com.example.movieticketsystem.model.MovieModel;
// import com.example.movieticketsystem.model.ShowtimeModel;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// @Controller
// @RequestMapping("/showtimes")
// public class ShowtimeController {
//     private final ShowtimeDAO showtimeDAO;
//     private final MovieDAO movieDAO;

//     @Autowired
//     public ShowtimeController(ShowtimeDAO showtimeDAO, MovieDAO movieDAO) {
//         this.showtimeDAO = showtimeDAO;
//         this.movieDAO = movieDAO;
//     }

//     @GetMapping
//     public String getAllShowtimes(Model model) {
//         model.addAttribute("showtimes", showtimeDAO.getAllShowtimes());
//         return "showtime_list";
//     }

//     @GetMapping("/add")
//     public String showAddShowtimeForm(Model model) {
//         model.addAttribute("showtime", new ShowtimeModel());
//         model.addAttribute("movies", movieDAO.getAllMovies());
//         return "showtime_form";
//     }

//     @PostMapping("/add")
//     public String addShowtime(@ModelAttribute ShowtimeModel showtime, 
//                               @RequestParam("movieId") int movieId, 
//                               RedirectAttributes redirectAttributes) {
//         MovieModel movie = movieDAO.getMovieById(movieId);
//         if (movie == null) {
//             redirectAttributes.addFlashAttribute("message", "Phim không tồn tại!");
//             return "redirect:/showtimes";
//         }
//         showtime.setMovie(movie);
//         if (showtimeDAO.addShowtime(showtime)) {
//             redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thành công!");
//         } else {
//             redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thất bại!");
//         }
//         return "redirect:/showtimes";
//     }

//     @GetMapping("/edit/{id}")
//     public String showEditShowtimeForm(@PathVariable int id, Model model) {
//         ShowtimeModel showtime = showtimeDAO.getShowtimeById(id);
//         model.addAttribute("showtime", showtime);
//         model.addAttribute("movies", movieDAO.getAllMovies());
//         return "showtime_form";
//     }

//     @PostMapping("/edit/{id}")
//     public String updateShowtime(@PathVariable int id, 
//                                  @ModelAttribute ShowtimeModel showtime, 
//                                  @RequestParam("movieId") int movieId, 
//                                  RedirectAttributes redirectAttributes) {
//         MovieModel movie = movieDAO.getMovieById(movieId);
//         if (movie == null) {
//             redirectAttributes.addFlashAttribute("message", "Phim không tồn tại!");
//             return "redirect:/showtimes";
//         }
//         showtime.setMovie(movie);
//         if (showtimeDAO.updateShowtime(id, showtime)) {
//             redirectAttributes.addFlashAttribute("message", "Cập nhật suất chiếu thành công!");
//         } else {
//             redirectAttributes.addFlashAttribute("message", "Cập nhật suất chiếu thất bại!");
//         }
//         return "redirect:/showtimes";
//     }

//     @PostMapping("/delete/{id}")
//     public String deleteShowtime(@PathVariable int id, RedirectAttributes redirectAttributes) {
//         if (showtimeDAO.deleteShowtime(id)) {
//             redirectAttributes.addFlashAttribute("message", "Xóa suất chiếu thành công!");
//         } else {
//             redirectAttributes.addFlashAttribute("message", "Xóa suất chiếu thất bại!");
//         }
//         return "redirect:/showtimes";
//     }
// }

package com.example.movieticketsystem.controller;

import com.example.movieticketsystem.dao.MovieDAO;
import com.example.movieticketsystem.dao.ShowtimeDAO;
import com.example.movieticketsystem.dao.TheaterDAO;
import com.example.movieticketsystem.model.MovieModel;
import com.example.movieticketsystem.model.ShowtimeModel;
import com.example.movieticketsystem.model.TheaterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/showtimes")
public class ShowtimeController {
    private final ShowtimeDAO showtimeDAO;
    private final MovieDAO movieDAO;
    private final TheaterDAO theaterDAO;

    @Autowired
    public ShowtimeController(ShowtimeDAO showtimeDAO, MovieDAO movieDAO, TheaterDAO theaterDAO) {
        this.showtimeDAO = showtimeDAO;
        this.movieDAO = movieDAO;
        this.theaterDAO = theaterDAO;
    }

    @GetMapping
    public String getAllShowtimes(Model model) {
        try {
            model.addAttribute("showtimes", showtimeDAO.getAllShowtimes());
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách suất chiếu: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("message", "Có lỗi xảy ra khi tải dữ liệu.");
        }
        return "showtime_list";
    }

    @GetMapping("/add")
    public String showAddShowtimeForm(Model model) {
        try {
            // Ensure we create a new instance with id=0 to avoid null ID issues
            ShowtimeModel showtime = new ShowtimeModel();
            showtime.setId(0); // Set ID explicitly to 0 for new records
            
            model.addAttribute("showtime", showtime);
            model.addAttribute("movies", movieDAO.getAllMovies());
            model.addAttribute("theaters", theaterDAO.getAllTheaters());
        } catch (Exception e) {
            System.out.println("Lỗi khi chuẩn bị form thêm suất chiếu: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("message", "Có lỗi xảy ra khi tải dữ liệu.");
        }
        return "showtime_form";
    }

    @PostMapping("/add")
    public String addShowtime(@ModelAttribute ShowtimeModel showtime, 
                              @RequestParam("movieId") int movieId,
                              @RequestParam("theaterId") int theaterId, 
                              RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Đang thêm suất chiếu - movieId: " + movieId + ", theaterId: " + theaterId);
            
            // Set ID to 0 to ensure we're adding a new record
            showtime.setId(0);
            
            MovieModel movie = movieDAO.getMovieById(movieId);
            if (movie == null) {
                System.out.println("Không tìm thấy phim với ID: " + movieId);
                redirectAttributes.addFlashAttribute("message", "Phim không tồn tại!");
                return "redirect:/showtimes";
            }
            
            TheaterModel theater = theaterDAO.getTheaterById(theaterId);
            if (theater == null) {
                System.out.println("Không tìm thấy rạp với ID: " + theaterId);
                redirectAttributes.addFlashAttribute("message", "Rạp không tồn tại!");
                return "redirect:/showtimes";
            }
            
            // Set giá trị mặc định cho available_seats nếu nó bằng 0
            if (showtime.getAvailableSeats() <= 0) {
                showtime.setAvailableSeats(theater.getTotalSeats());
                System.out.println("Đặt số ghế khả dụng thành tổng số ghế của rạp: " + theater.getTotalSeats());
            }
            
            // Gán các đối tượng vào showtime
            showtime.setMovie(movie);
            showtime.setTheater(theater);
            
            // Log ra thông tin suất chiếu
            System.out.println("Thông tin suất chiếu trước khi thêm: "
                + "\nMovie ID: " + showtime.getMovie().getId()
                + "\nTheater ID: " + showtime.getTheater().getId()
                + "\nShowtime: " + (showtime.getShowtime() != null ? showtime.getShowtime() : "NULL")
                + "\nAvailable Seats: " + showtime.getAvailableSeats());
            
            if (showtimeDAO.addShowtime(showtime)) {
                redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thành công!");
            } else {
                redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thất bại!");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm suất chiếu: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thất bại! Lỗi: " + e.getMessage());
        }
        return "redirect:/showtimes";
    }

    @GetMapping("/edit/{id}")
    public String showEditShowtimeForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Verify ID is valid (greater than 0)
            if (id <= 0) {
                redirectAttributes.addFlashAttribute("message", "ID suất chiếu không hợp lệ!");
                return "redirect:/showtimes";
            }
            
            ShowtimeModel showtime = showtimeDAO.getShowtimeById(id);
            if (showtime == null) {
                redirectAttributes.addFlashAttribute("message", "Không tìm thấy suất chiếu!");
                return "redirect:/showtimes";
            }
            
            model.addAttribute("showtime", showtime);
            model.addAttribute("movies", movieDAO.getAllMovies());
            model.addAttribute("theaters", theaterDAO.getAllTheaters());
        } catch (Exception e) {
            System.out.println("Lỗi khi chuẩn bị form chỉnh sửa suất chiếu: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Có lỗi xảy ra khi tải dữ liệu.");
            return "redirect:/showtimes";
        }
        return "showtime_form";
    }

    @PostMapping("/edit/{id}")
    public String updateShowtime(@PathVariable int id, 
                                 @ModelAttribute ShowtimeModel showtime, 
                                 @RequestParam("movieId") int movieId,
                                 @RequestParam("theaterId") int theaterId, 
                                 RedirectAttributes redirectAttributes) {
        try {
            // Verify ID is valid (greater than 0)
            if (id <= 0) {
                redirectAttributes.addFlashAttribute("message", "ID suất chiếu không hợp lệ!");
                return "redirect:/showtimes";
            }
            
            System.out.println("Đang cập nhật suất chiếu ID: " + id 
                + " - movieId: " + movieId 
                + ", theaterId: " + theaterId);
            
            MovieModel movie = movieDAO.getMovieById(movieId);
            if (movie == null) {
                System.out.println("Không tìm thấy phim với ID: " + movieId);
                redirectAttributes.addFlashAttribute("message", "Phim không tồn tại!");
                return "redirect:/showtimes";
            }
            
            TheaterModel theater = theaterDAO.getTheaterById(theaterId);
            if (theater == null) {
                System.out.println("Không tìm thấy rạp với ID: " + theaterId);
                redirectAttributes.addFlashAttribute("message", "Rạp không tồn tại!");
                return "redirect:/showtimes";
            }
            
            // Bảo đảm ID đúng
            showtime.setId(id);
            
            // Gán các đối tượng vào showtime
            showtime.setMovie(movie);
            showtime.setTheater(theater);
            
            // Đảm bảo showtime được thiết lập
            if (showtime.getShowtime() == null) {
                System.out.println("Lỗi: Thời gian chiếu (showtime) là null");
                redirectAttributes.addFlashAttribute("message", "Thời gian chiếu không hợp lệ!");
                return "redirect:/showtimes";
            }
            
            // Log ra thông tin suất chiếu
            System.out.println("Thông tin suất chiếu trước khi cập nhật: "
                + "\nID: " + showtime.getId()
                + "\nMovie ID: " + showtime.getMovie().getId()
                + "\nTheater ID: " + showtime.getTheater().getId()
                + "\nShowtime: " + showtime.getShowtime()
                + "\nAvailable Seats: " + showtime.getAvailableSeats());
            
            if (showtimeDAO.updateShowtime(id, showtime)) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật suất chiếu thành công!");
            } else {
                redirectAttributes.addFlashAttribute("message", "Cập nhật suất chiếu thất bại! Vui lòng kiểm tra log để biết thêm chi tiết.");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật suất chiếu: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Cập nhật suất chiếu thất bại! Lỗi: " + e.getMessage());
        }
        return "redirect:/showtimes";
    }

    @PostMapping("/delete/{id}")
    public String deleteShowtime(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            if (id <= 0) {
                redirectAttributes.addFlashAttribute("message", "ID suất chiếu không hợp lệ!");
                return "redirect:/showtimes";
            }
            
            if (showtimeDAO.deleteShowtime(id)) {
                redirectAttributes.addFlashAttribute("message", "Xóa suất chiếu thành công!");
            } else {
                redirectAttributes.addFlashAttribute("message", "Xóa suất chiếu thất bại!");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa suất chiếu: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Xóa suất chiếu thất bại! Lỗi: " + e.getMessage());
        }
        return "redirect:/showtimes";
    }
}