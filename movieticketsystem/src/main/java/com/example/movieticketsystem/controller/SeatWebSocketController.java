package com.example.movieticketsystem.controller;

import com.example.movieticketsystem.dao.SeatDAO;
import com.example.movieticketsystem.model.SeatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SeatWebSocketController {

    private final SeatDAO seatDAO;

    @Autowired
    public SeatWebSocketController(SeatDAO seatDAO) {
        this.seatDAO = seatDAO;
    }

    @MessageMapping("/select-seat")
    @SendTo("/topic/seats")
    public SeatModel selectSeat(SeatModel seat) {
        // KHÔNG cập nhật trạng thái ghế trong database ở đây!
        // Chỉ trả về thông tin ghế để các client khác biết ghế này đang được chọn tạm thời
        return seat;
    }

    @MessageMapping("/deselect-seat")
    @SendTo("/topic/seats")
    public SeatModel deselectSeat(SeatModel seat) {
        // KHÔNG cập nhật trạng thái ghế trong database ở đây!
        // Chỉ trả về thông tin ghế để các client khác biết ghế này được bỏ chọn
        return seat;
    }
} 