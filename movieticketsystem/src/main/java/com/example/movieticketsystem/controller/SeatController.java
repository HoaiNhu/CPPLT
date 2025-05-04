// package com.example.movieticketsystem.controller;

// import com.example.movieticketsystem.dao.SeatDAO;
// import com.example.movieticketsystem.model.MovieModel;
// import com.example.movieticketsystem.model.SeatModel;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import java.util.List;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.concurrent.Future;

// @Controller
// @RequestMapping("/seats")
// public class SeatController {
//     private final SeatDAO seatDAO;
//     private final ExecutorService executorService = Executors.newFixedThreadPool(5);
//     private final MovieModel movie = new MovieModel("Avengers", "Action", 100);

//     @Autowired
//     public SeatController(SeatDAO seatDAO) {
//         this.seatDAO = seatDAO;
//     }

//     @GetMapping
//     public String getAllSeats(Model model) {
//         List<SeatModel> seats = seatDAO.getAllSeats();
//         model.addAttribute("seats", seats);
//         model.addAttribute("movie", movie);
//         return "index";
//     }

//     @GetMapping("/book")
//     public String showBookingForm(@RequestParam int seatId, Model model, RedirectAttributes redirectAttributes) {
//         SeatModel seat = seatDAO.getAllSeats().stream()
//                 .filter(s -> s.getId() == seatId)
//                 .findFirst()
//                 .orElse(null);
//         if (seat == null || !seat.getAvailable().get()) {
//             redirectAttributes.addFlashAttribute("message", "Ghế không khả dụng!");
//             return "redirect:/seats";
//         }
//         model.addAttribute("seat", seat);
//         return "booking";
//     }

//     @PostMapping("/book")
//     public String bookSeat(@RequestParam int seatId,
//                            @RequestParam String customerName,
//                            @RequestParam String phone,
//                            @RequestParam String email,
//                            RedirectAttributes redirectAttributes) throws Exception {
//         // Dùng ExecutorService và Future để chờ kết quả
//         Future<Boolean> future = executorService.submit(() -> seatDAO.bookSeat(seatId, customerName, phone, email));
//         boolean success = future.get(); // Chờ kết quả

//         if (success) {
//             redirectAttributes.addFlashAttribute("message", "Đặt ghế thành công!");
//         } else {
//             redirectAttributes.addFlashAttribute("message", "Ghế đã được đặt bởi người khác!");
//         }
//         return "redirect:/seats";
//     }

//     @PostMapping("/cancel")
//     public String cancelSeat(@RequestParam int seatId, RedirectAttributes redirectAttributes) throws Exception {
//         // Dùng ExecutorService và Future để chờ kết quả
//         Future<Boolean> future = executorService.submit(() -> seatDAO.cancelSeat(seatId));
//         boolean success = future.get(); // Chờ kết quả

//         if (success) {
//             redirectAttributes.addFlashAttribute("message", "Hủy ghế thành công!");
//         } else {
//             redirectAttributes.addFlashAttribute("message", "Ghế không thể hủy!");
//         }
//         return "redirect:/seats";
//     }
// }

package com.example.movieticketsystem.controller;

import com.example.movieticketsystem.dao.SeatDAO;
import com.example.movieticketsystem.dao.ShowtimeDAO;
import com.example.movieticketsystem.model.SeatModel;
import com.example.movieticketsystem.model.ShowtimeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/seats")
public class SeatController {
    private final SeatDAO seatDAO;
    private final ShowtimeDAO showtimeDAO;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    public SeatController(SeatDAO seatDAO, ShowtimeDAO showtimeDAO) {
        this.seatDAO = seatDAO;
        this.showtimeDAO = showtimeDAO;
    }

    @GetMapping
    public String getAllSeats(Model model, @RequestParam(value = "selectShowtimeId", required = false) Integer showtimeId) {
        if (showtimeId != null) {
            ShowtimeModel showtime = showtimeDAO.getShowtimeById(showtimeId);
            List<SeatModel> seats = seatDAO.getAllSeats();
            model.addAttribute("selectedShowtime", showtime);
            model.addAttribute("seats", seats);
        }
        return "index";
    }

    @PostMapping("/book")
    public String bookSeats(@RequestParam List<Integer> seatIds,
                            @RequestParam String customerName,
                            @RequestParam String phone,
                            @RequestParam String email,
                            @RequestParam int showtimeId,
                            RedirectAttributes redirectAttributes) throws Exception {
        boolean allSuccess = true;
        for (Integer seatId : seatIds) {
            Future<Boolean> future = executorService.submit(() -> seatDAO.bookSeat(seatId, customerName, phone, email));
            boolean success = future.get();
            if (!success) {
                allSuccess = false;
            }
        }

        if (allSuccess) {
            redirectAttributes.addFlashAttribute("message", "Đặt ghế thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Một số ghế đã được đặt bởi người khác!");
        }
        return "redirect:/tickets/select-seats?showtimeId=" + showtimeId;
    }

    @PostMapping("/cancel")
    public String cancelSeat(@RequestParam int seatId, @RequestParam(required = false) Integer showtimeId, RedirectAttributes redirectAttributes) throws Exception {
        Future<Boolean> future = executorService.submit(() -> seatDAO.cancelSeat(seatId));
        boolean success = future.get();

        if (success) {
            redirectAttributes.addFlashAttribute("message", "Hủy ghế thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Ghế không thể hủy!");
        }
        return showtimeId != null ? "redirect:/seats?selectShowtimeId=" + showtimeId : "redirect:/";
    }
}