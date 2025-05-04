package com.example.movieticketsystem.controller;

import com.example.movieticketsystem.dao.TheaterDAO;
import com.example.movieticketsystem.model.TheaterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/theaters")
public class TheaterController {
    private final TheaterDAO theaterDAO;

    @Autowired
    public TheaterController(TheaterDAO theaterDAO) {
        this.theaterDAO = theaterDAO;
    }

    @GetMapping
    public String getAllTheaters(Model model) {
        model.addAttribute("theaters", theaterDAO.getAllTheaters());
        return "theater_list";
    }

    @GetMapping("/add")
    public String showAddTheaterForm(Model model) {
        model.addAttribute("theater", new TheaterModel());
        return "theater_form";
    }

    @PostMapping("/add")
    public String addTheater(@ModelAttribute TheaterModel theater, RedirectAttributes redirectAttributes) {
        if (theaterDAO.addTheater(theater)) {
            redirectAttributes.addFlashAttribute("message", "Thêm rạp thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm rạp thất bại!");
        }
        return "redirect:/theaters";
    }

    @GetMapping("/edit/{id}")
    public String showEditTheaterForm(@PathVariable int id, Model model) {
        TheaterModel theater = theaterDAO.getTheaterById(id);
        model.addAttribute("theater", theater);
        return "theater_form";
    }

    @PostMapping("/edit/{id}")
    public String updateTheater(@PathVariable int id, @ModelAttribute TheaterModel theater, RedirectAttributes redirectAttributes) {
        if (theaterDAO.updateTheater(id, theater)) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật rạp thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật rạp thất bại!");
        }
        return "redirect:/theaters";
    }

    @PostMapping("/delete/{id}")
    public String deleteTheater(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (theaterDAO.deleteTheater(id)) {
            redirectAttributes.addFlashAttribute("message", "Xóa rạp thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Xóa rạp thất bại!");
        }
        return "redirect:/theaters";
    }
} 