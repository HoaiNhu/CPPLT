// package com.example.movieticketsystem.controller;

// import com.example.movieticketsystem.dao.MovieDAO;
// import com.example.movieticketsystem.model.MovieModel;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// @Controller
// @RequestMapping("/movies")
// public class MovieController {
//     private final MovieDAO movieDAO;

//     @Autowired
//     public MovieController(MovieDAO movieDAO) {
//         this.movieDAO = movieDAO;
//     }

//     // Hiển thị danh sách phim
//     @GetMapping
//     public String getAllMovies(Model model) {
//         model.addAttribute("movies", movieDAO.getAllMovies());
//         return "movie_list";
//     }

//     // Hiển thị form thêm phim
//     @GetMapping("/add")
//     public String showAddMovieForm(Model model) {
//         model.addAttribute("movie", new MovieModel());
//         return "movie_form";
//     }

//     // Xử lý thêm phim
//     @PostMapping("/add")
//     public String addMovie(@ModelAttribute MovieModel movie, RedirectAttributes redirectAttributes) {
//         if (movieDAO.addMovie(movie)) {
//             redirectAttributes.addFlashAttribute("message", "Thêm phim thành công!");
//         } else {
//             redirectAttributes.addFlashAttribute("message", "Thêm phim thất bại!");
//         }
//         return "redirect:/movies";
//     }

//     // Hiển thị form sửa phim
//     @GetMapping("/edit/{id}")
//     public String showEditMovieForm(@PathVariable int id, Model model) {
//         MovieModel movie = movieDAO.getMovieById(id);
//         model.addAttribute("movie", movie);
//         return "movie_form";
//     }

//     // Xử lý sửa phim
//     @PostMapping("/edit/{id}")
//     public String updateMovie(@PathVariable int id, @ModelAttribute MovieModel movie, RedirectAttributes redirectAttributes) {
//         if (movieDAO.updateMovie(id, movie)) {
//             redirectAttributes.addFlashAttribute("message", "Cập nhật phim thành công!");
//         } else {
//             redirectAttributes.addFlashAttribute("message", "Cập nhật phim thất bại!");
//         }
//         return "redirect:/movies";
//     }

//     // Xóa phim
//     @PostMapping("/delete/{id}")
//     public String deleteMovie(@PathVariable int id, RedirectAttributes redirectAttributes) {
//         if (movieDAO.deleteMovie(id)) {
//             redirectAttributes.addFlashAttribute("message", "Xóa phim thành công!");
//         } else {
//             redirectAttributes.addFlashAttribute("message", "Xóa phim thất bại!");
//         }
//         return "redirect:/movies";
//     }
// }

package com.example.movieticketsystem.controller;

import com.example.movieticketsystem.dao.MovieDAO;
import com.example.movieticketsystem.model.MovieModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.logging.Logger;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private static final Logger logger = Logger.getLogger(MovieController.class.getName());
    private final MovieDAO movieDAO;

    @Autowired
    public MovieController(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    // Hiển thị danh sách phim
    @GetMapping
    public String getAllMovies(Model model) {
        model.addAttribute("movies", movieDAO.getAllMovies());
        return "movie_list";
    }

    // Hiển thị form thêm phim
    @GetMapping("/add")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new MovieModel());
        return "movie_form";
    }

    // Xử lý thêm phim
    @PostMapping("/add")
    public String addMovie(@ModelAttribute MovieModel movie, RedirectAttributes redirectAttributes) {
        try {
            // Ghi log để debug
            logger.info("Đang thử thêm phim: " + movie.getTitle() + ", " + movie.getGenre() + ", " + movie.getTicketPrice());
            
            // Kiểm tra dữ liệu đầu vào
            if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Thêm phim thất bại! Tên phim không được để trống.");
                return "redirect:/movies";
            }
            
            if (movie.getGenre() == null || movie.getGenre().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Thêm phim thất bại! Thể loại không được để trống.");
                return "redirect:/movies";
            }
            
            if (movie.getTicketPrice() <= 0) {
                redirectAttributes.addFlashAttribute("message", "Thêm phim thất bại! Giá vé phải lớn hơn 0.");
                return "redirect:/movies";
            }
            
            if (movieDAO.addMovie(movie)) {
                redirectAttributes.addFlashAttribute("message", "Thêm phim thành công!");
                logger.info("Thêm phim thành công: ID = " + movie.getId());
            } else {
                redirectAttributes.addFlashAttribute("message", "Thêm phim thất bại! Vui lòng kiểm tra dữ liệu nhập vào.");
                logger.warning("Thêm phim thất bại, không có lỗi exception nhưng trả về false");
            }
        } catch (Exception e) {
            logger.severe("Lỗi khi thêm phim: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Thêm phim thất bại! Lỗi: " + e.getMessage());
        }
        return "redirect:/movies";
    }

    // Hiển thị form sửa phim
    @GetMapping("/edit/{id}")
    public String showEditMovieForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Đang tìm phim với ID: " + id);
            MovieModel movie = movieDAO.getMovieById(id);
            if (movie != null) {
                model.addAttribute("movie", movie);
                return "movie_form";
            } else {
                redirectAttributes.addFlashAttribute("message", "Không tìm thấy phim với ID: " + id);
                return "redirect:/movies";
            }
        } catch (Exception e) {
            logger.severe("Lỗi khi lấy thông tin phim: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Lỗi khi lấy thông tin phim: " + e.getMessage());
            return "redirect:/movies";
        }
    }

    // Xử lý sửa phim
    @PostMapping("/edit/{id}")
    public String updateMovie(@PathVariable int id, @ModelAttribute MovieModel movie, RedirectAttributes redirectAttributes) {
        try {
            // Ghi log để debug
            logger.info("Đang cập nhật phim ID " + id + ": " + movie.getTitle() + ", " + movie.getGenre() + ", " + movie.getTicketPrice());
            
            // Kiểm tra dữ liệu đầu vào
            if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật phim thất bại! Tên phim không được để trống.");
                return "redirect:/movies";
            }
            
            if (movie.getGenre() == null || movie.getGenre().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật phim thất bại! Thể loại không được để trống.");
                return "redirect:/movies";
            }
            
            if (movie.getTicketPrice() <= 0) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật phim thất bại! Giá vé phải lớn hơn 0.");
                return "redirect:/movies";
            }
            
            // Đảm bảo ID được set đúng
            movie.setId(id);
            
            if (movieDAO.updateMovie(id, movie)) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật phim thành công!");
                logger.info("Cập nhật phim thành công: ID = " + id);
            } else {
                redirectAttributes.addFlashAttribute("message", "Cập nhật phim thất bại! Không tìm thấy phim hoặc dữ liệu không hợp lệ.");
                logger.warning("Cập nhật phim thất bại, không có lỗi exception nhưng trả về false cho ID: " + id);
            }
        } catch (Exception e) {
            logger.severe("Lỗi khi cập nhật phim: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Cập nhật phim thất bại! Lỗi: " + e.getMessage());
        }
        return "redirect:/movies";
    }

    // Xóa phim
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Đang xóa phim với ID: " + id);
            if (movieDAO.deleteMovie(id)) {
                redirectAttributes.addFlashAttribute("message", "Xóa phim thành công!");
                logger.info("Xóa phim thành công: ID = " + id);
            } else {
                redirectAttributes.addFlashAttribute("message", "Xóa phim thất bại! Không tìm thấy phim.");
                logger.warning("Xóa phim thất bại, không có lỗi exception nhưng trả về false cho ID: " + id);
            }
        } catch (Exception e) {
            logger.severe("Lỗi khi xóa phim: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Xóa phim thất bại! Lỗi: " + e.getMessage());
        }
        return "redirect:/movies";
    }
}