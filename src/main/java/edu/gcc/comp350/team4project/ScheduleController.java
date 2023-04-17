package edu.gcc.comp350.team4project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ScheduleController {

    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

}
