package edu.gcc.comp350.team4project;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Scanner;

@SpringBootApplication
public class Server {
    public static void main(String[] args) {
        //Creating Database
        Scanner tempS = new Scanner(System.in);

        //boot menu that handles database for testing purposes.
        while(true) {
            System.out.println("Type 1 to run with existing database.");//Normally, the software would always run option '1'
            DatabaseController.printAllUsers();
            System.out.println("Type 2 to run with a cleared database. WARNING: WILL DELETE ALL STORED USERS");//option '2' clears all db files which also creates new one for first time user
            String input = tempS.nextLine();
            if (input.equals("1")) {
                System.out.println("Running with existing db");
                break;
            } else if (input.equals("2")) {
                System.out.println("Running with new db");
                DatabaseController.createNewDatabase("team4_project.db");
                DatabaseController.drop();
                DatabaseController.createNewTable();
                break;
            }
        }
//        User jonah = new User("jonah_dybas", "Junior", "Password", false);
//        Schedule schedule = new Schedule("test_sched", Semester.SPRING);
//        Schedule schedule2 = new Schedule("test_sched2", Semester.SPRING);
//        ArrayList<DayOfWeek> days1 = new ArrayList<>();
//        days1.add(DayOfWeek.MONDAY);
//        days1.add(DayOfWeek.WEDNESDAY);
//        days1.add(DayOfWeek.FRIDAY);
//
//        ArrayList<DayOfWeek> days2 = new ArrayList<>();
//        days2.add(DayOfWeek.TUESDAY);
//        days2.add(DayOfWeek.THURSDAY);
//
//        Sche course1 = new Course(12345, "CS", Semester.SPRING, "300", 'A', "Algorithms", 4, "Dr. Smith", "This course covers the design and analysis of algorithms.", days1, LocalTime.of(9, 0), LocalTime.of(9, 50));
//        Course course2 = new Course(67890, "MATH", Semester.SPRING, "200", 'B', "Calculus II", 3, "Dr. Johnson", "This course covers advanced topics in calculus.", days2, LocalTime.of(13, 0), LocalTime.of(14, 15));
//
//        try {
//            schedule.addCourse(course1);
//            schedule2.addCourse(course1);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            schedule.addCourse(course2);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        jonah.saveScheduleToUser(schedule);
//        jonah.saveScheduleToUser(schedule2);
//
//        DatabaseController.insert(jonah);
        //Running Server
        SpringApplication.run(WebController.class, args);
    }

}
