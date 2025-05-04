package com.example.movieticketsystem.controller;

import com.example.movieticketsystem.dao.SeatDAO;
import com.example.movieticketsystem.dao.ShowtimeDAO;
import com.example.movieticketsystem.model.SeatModel;
import com.example.movieticketsystem.model.ShowtimeModel;
import com.example.movieticketsystem.model.TicketModel;
import com.example.movieticketsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tickets")
public class TicketCancellationController {
    
    private final SeatDAO seatDAO;
    private final ShowtimeDAO showtimeDAO;
    private final TicketService ticketService;
    
    @Autowired
    public TicketCancellationController(SeatDAO seatDAO, ShowtimeDAO showtimeDAO, TicketService ticketService) {
        this.seatDAO = seatDAO;
        this.showtimeDAO = showtimeDAO;
        this.ticketService = ticketService;
    }
    
    @GetMapping("/cancel")
    public String showCancellationForm() {
        return "cancel-ticket";
    }
    
    @PostMapping("/find")
    public String findTicketsByEmail(@RequestParam String email, Model model) {
        List<SeatModel> bookedSeats = seatDAO.getAllSeats().stream()
                .filter(seat -> !seat.getAvailable().get() && email.equals(seat.getEmail()))
                .collect(Collectors.toList());
        
        List<TicketModel> tickets = new ArrayList<>();
        for (SeatModel seat : bookedSeats) {
            // Tìm suất chiếu cho ghế (giả định rằng một ghế chỉ thuộc về một suất chiếu tại một thời điểm)
            List<ShowtimeModel> showtimes = showtimeDAO.getAllShowtimes().stream()
                    .filter(s -> s.getTheater().getId() == seat.getTheaterId())
                    .collect(Collectors.toList());
            
            if (!showtimes.isEmpty()) {
                // Lấy suất chiếu đầu tiên tìm thấy
                ShowtimeModel showtime = showtimes.get(0);
                TicketModel ticket = new TicketModel(showtime.getMovie(), seat);
                // Thêm showtime vào ticket model (cần mở rộng TicketModel)
                ticket.setShowtime(showtime);
                tickets.add(ticket);
            }
        }
        
        model.addAttribute("tickets", tickets);
        model.addAttribute("email", email);
        return "cancel-ticket";
    }
    
    @PostMapping("/cancel")
    public String cancelTicket(@RequestParam int seatId, 
                              @RequestParam String email, 
                              @RequestParam(required = false) Integer showtimeId,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        // Kiểm tra email trước khi hủy vé
        SeatModel seat = seatDAO.getAllSeats().stream()
                .filter(s -> s.getId() == seatId)
                .findFirst()
                .orElse(null);
        
        if (seat == null) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy ghế này!");
            redirectAttributes.addFlashAttribute("success", false);
            return "redirect:/tickets/cancel";
        }
        
        if (!email.equals(seat.getEmail())) {
            redirectAttributes.addFlashAttribute("message", "Email không khớp với thông tin đặt vé!");
            redirectAttributes.addFlashAttribute("success", false);
            return "redirect:/tickets/cancel";
        }
        
        boolean success = ticketService.cancelSeat(seatId);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Hủy vé thành công!");
            redirectAttributes.addFlashAttribute("success", true);
        } else {
            redirectAttributes.addFlashAttribute("message", "Không thể hủy vé. Vui lòng thử lại sau!");
            redirectAttributes.addFlashAttribute("success", false);
        }
        
        return "redirect:/tickets/cancel";
    }
}