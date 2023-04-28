package edu.gcc.comp350.team4project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {
    public static void main(String[] args) {
        //Creating Database
        DatabaseController.createNewDatabase("team4_project.db");
        DatabaseController.drop();
        DatabaseController.createNewTable();
        User jonah = new User("Jonah_Dybas", "Junior", "Password", false);
        Schedule schedule = new Schedule("test_sched", Semester.SPRING);
        jonah.saveScheduleToUser(schedule);
        DatabaseController.insert(jonah);
        //Running Server
        SpringApplication.run(WebController.class, args);
    }

}
