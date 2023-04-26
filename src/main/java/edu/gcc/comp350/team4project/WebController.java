package edu.gcc.comp350.team4project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;


@Controller
public class WebController {
    /**
     * This class holds together form data for a User
     */
    private class UserFormData{
        private String username;
        private String password;
        private String year;

        public UserFormData(){
            this.username = "";
            this.password = "";
            this.year = "";
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    /**
     * This calss holds together form data for the login form
     */
    private class LoginFormData{
        private String username;
        private String password;

        public LoginFormData(){
            this.username = "";
            this.password = "";
        }
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    //Session Data
    private static User currentUser;
    private static SearchController searchBox;
    private static Schedule tempSchedule;
    private static ArrayList<Course> totalCourses;

    public static void initialize(){
        //Importing Courses
        totalCourses = new ArrayList<>();
        String longCSV = "large_courses.csv"; //pulls from csv of all courses
        importCoursesFromCSV(longCSV); //imports information as data we can use

    }
    public static void importCoursesFromCSV(String ext) {//handles importing a course from csv. Takes all csv values and converts to data types. Only takes in good data
        String csvFile =  "src/main/java/edu/gcc/comp350/team4project/" + ext;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String courseName, departmentName, courseLevel, professorName;
                StringBuilder description = new StringBuilder();
                char courseSection = ' ';
                int refNum = 0, numCredits = 0;
                Semester semester = null;
                ArrayList<DayOfWeek> days = new ArrayList<>();
                LocalTime startTime = null;
                LocalTime endTime = null;

                try {
                    if (Integer.parseInt(data[0]) == 10)
                        semester = Semester.FALL;
                    else if (Integer.parseInt(data[0]) == 30)
                        semester = Semester.SPRING;
                } catch (Exception ignored) {continue;}

                departmentName = data[1];
                courseLevel = data[2];

                try {
                    courseSection = data[3].charAt(0);
                } catch (Exception ignored) {}

                courseName = data[4];

                try {
                    numCredits = Integer.parseInt(data[5]);
                } catch (Exception ignored) {continue;}

                if (data[6].equals("M"))
                    days.add(DayOfWeek.MONDAY);

                if (data[7].equals("T"))
                    days.add(DayOfWeek.TUESDAY);

                if(data[8].equals("W"))
                    days.add(DayOfWeek.WEDNESDAY);

                if(data[9].equals("R"))
                    days.add(DayOfWeek.THURSDAY);

                if(data[10].equals("F"))
                    days.add(DayOfWeek.FRIDAY);

                try {
                    String[] tempTime = data[11].split(":");
                    int min;
                    int hour = Integer.parseInt(tempTime[0]);
                    if (tempTime[2].charAt(3)== 'A') {//these convert the time to 24 hour time
                        if(hour == 12){
                            hour = 0;
                        }
                    } else if (tempTime[2].charAt(3) == 'P') {
                        if (hour != 12) {
                            hour += 12;
                        }
                    }
                    min = Integer.parseInt(tempTime[1]);
                    startTime = LocalTime.of(hour, min);
                } catch(Exception ignore) {
                    continue;
                }

                try {
                    String[] tempTime = data[12].split(":");
                    int hour = Integer.parseInt(tempTime[0]);
                    int min = 0;

                    if (tempTime[2].charAt(3) == 'A') { //these convert the time to 24 hour time
                        if(hour == 12)
                            hour = 0;
                    }
                    else if (tempTime[2].charAt(3) == 'P') {
                        if(hour!=12)
                            hour += 12;
                    }
                    min = Integer.parseInt(tempTime[1]);
                    endTime = LocalTime.of(hour, min);
                } catch(Exception ignore) {
                    continue;
                }

                professorName = data[13];

                try {
                    refNum = Integer.parseInt(data[14]);
                } catch(Exception ignore) {continue;}

                for(int i = 15; i < data.length; i++){
                    description.append(data[i]).append(" ");
                }
                totalCourses.add(new Course(refNum,departmentName,semester,courseLevel,courseSection,courseName,numCredits,professorName, description.toString(),days,startTime,endTime));
            }
        } catch (IOException ignored) {
            System.out.println("CSV_FILE not found!");
        }
    }

    @GetMapping("/")
    public String home(Model model) {
        // TODO: add code to display home page
        model.addAttribute("user", currentUser);
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        // TODO: add code to display login page
        model.addAttribute("formData", new LoginFormData());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute LoginFormData formData) {
        // TODO: add code to handle login form submission
        if(DatabaseController.authenticateUser(formData.getUsername(), formData.getPassword())){
            currentUser = DatabaseController.pullUser(formData.getUsername());
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @GetMapping("/schedules")
    public String schedules(Model model) {
        // TODO: add code to display schedules page
        return "schedules";
    }

    @GetMapping("/editschedule/{scheduleName}")
    public String editSchedule(@PathVariable String scheduleName, Model model) {
        // TODO: add code to display edit schedule page for a specific schedule
        model.addAttribute("schedule", tempSchedule);
        return "edit-schedule";
    }

    @PostMapping("/editschedule/{scheduleName}")
    public String doEditSchedule(@PathVariable String scheduleName, @ModelAttribute Schedule schedule) {
        // TODO: add code to handle form submission for editing a schedule
        return "redirect:/schedules";
    }

    @GetMapping("/register")
    public String register(Model model) {
        // TODO: add code to display registration page
        model.addAttribute("formData", new UserFormData());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute UserFormData formData) {
        // TODO: add code to handle registration form submission
        User newUser = new User(formData.username, formData.year, formData.password, false);
        DatabaseController.insert(newUser);
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
