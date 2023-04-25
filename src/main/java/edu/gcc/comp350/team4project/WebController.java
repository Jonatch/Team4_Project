package edu.gcc.comp350.team4project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        // TODO: add code to display home page
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        // TODO: add code to display login page
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password) {
        // TODO: add code to handle login form submission
        return "redirect:/schedules";
    }

    @GetMapping("/schedules")
    public String schedules(Model model) {
        // TODO: add code to display schedules page
        return "schedules";
    }

    @GetMapping("/editschedule/{scheduleName}")
    public String editSchedule(@PathVariable String scheduleName, Model model) {
        // TODO: add code to display edit schedule page for a specific schedule
        return "edit-schedule";
    }

    @PostMapping("/editschedule/{scheduleName}")
    public String doEditSchedule(@PathVariable String scheduleName, @ModelAttribute Schedule schedule) {
        // TODO: add code to handle form submission for editing a schedule
        return "redirect:/schedules";
    }

    @GetMapping("/register")
    public String register() {
        // TODO: add code to display registration page
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute User user) {
        // TODO: add code to handle registration form submission
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String doLogout() {
        // TODO: add code to handle logout
        return "redirect:/login";
    }

    @PostMapping("/deleteschedule/{scheduleName}")
    public String doDeleteSchedule(@PathVariable String scheduleName) {
        // TODO: add code to handle deleting a specific schedule
        return "redirect:/schedules";
    }
}
