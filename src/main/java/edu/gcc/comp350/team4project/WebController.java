package edu.gcc.comp350.team4project;
import edu.gcc.comp350.team4project.forms.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Controller
public class WebController {
    //Session Data
    private static User currentUser;
    private static SearchController searchBox;
    private static Schedule tempSchedule;
    private static ArrayList<Course> totalCourses;

    @RequestMapping("/")
    public String home(Model model) {
        //If there are is no current user then redirect to the login form
        if (currentUser == null){
            return "redirect:/login";
        }
        //If we have a user then return the home template for that user and the user's schedules
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("schedules", currentUser.getSchedules()); // Replace with your implementation to get the schedules
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, RedirectAttributes redirectAttributes) {
        //If we have some errors to show, add them to the model to be showed
        if (redirectAttributes.containsAttribute("errors")) {
            model.addAttribute("errors", redirectAttributes.getFlashAttributes());
        }
        //send Fields data to be filled (LoginFormData() object holds together)
        model.addAttribute("formData", new LoginFormData());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute @Valid LoginFormData formData, BindingResult result, RedirectAttributes redirectAttributes) {
        //Checks if the username and password provided are in the database. Flash an error message
        if(!DatabaseController.authenticateUser(formData.getUsername(), formData.getPassword())){
            result.rejectValue("username", "username.invalid", "The username and password do not match");
        }
        //If there is any other error (e.g. password is too short) flash them and return the login form again
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            System.out.println(result.getAllErrors().toString());
            return "redirect:/login";
        }
        //If there are no errors then load the user data. Redirect to Home page
        currentUser = DatabaseController.pullUser(formData.getUsername());
        return "redirect:/";
    }

    @GetMapping("/schedules")
    public String schedules(Model model) {
        // TODO: add code to display schedules page
        return "schedules";
    }

    @GetMapping("/create-schedule")
    public String createSchedule(Model model) {
        model.addAttribute("scheduleFormData", new ScheduleFormData());
        return "create-schedule";
    }

    @PostMapping("/create-schedule")
    public String createSchedule(@ModelAttribute ScheduleFormData scheduleFormData) {
        Semester semester = null;
        if(scheduleFormData.getSemester().equalsIgnoreCase("spring")){
            semester = Semester.SPRING;
        }else if(scheduleFormData.getSemester().equalsIgnoreCase("fall")){
            semester = Semester.FALL;
        }

        Schedule newSchedule = new Schedule(scheduleFormData.getName(), semester);
        tempSchedule = newSchedule;
        currentUser.saveScheduleToUser(tempSchedule);

        return "redirect:/editschedule/" + newSchedule.getScheduleName();
    }



    @GetMapping("/editschedule/{scheduleName}")
    public String editSchedule(@PathVariable String scheduleName, Model model) {
        //Getting all the courses loaded into totalCourses\
        initialize();
        //Adding all courses to the model
        model.addAttribute("courses", totalCourses);
        //Adding the current schedule being modified
        model.addAttribute("schedule", currentUser.getSchedule(scheduleName));
        return "edit-schedule";
    }

    @PostMapping("/editschedule/{scheduleName}")
    public String doEditSchedule(@PathVariable String scheduleName, @ModelAttribute Schedule schedule) {
        // TODO: add code to handle form submission for editing a schedule
        return "redirect:/schedules";
    }

    @GetMapping("/viewschedule/{scheduleName}")
    public String viewSchedule(@PathVariable String scheduleName, Model model) {
        // TODO: add code to display edit schedule page for a specific schedule
        ArrayList<Schedule> schedules = currentUser.getSchedules();
        for (Schedule schedule : schedules) {
            if (schedule.getScheduleName().equals(scheduleName)) {
                tempSchedule = schedule;
                break;
            }
        }
        model.addAttribute("schedule", tempSchedule);
        return "schedule";
    }

    @GetMapping("/register")
    public String register(Model model, RedirectAttributes redirectAttributes) {
        //If there are any errors from previous submission of form flash them
        if (redirectAttributes.containsAttribute("errors")){
            model.addAttribute("errors", redirectAttributes.getFlashAttributes());
        }
        //Else send the Form holding the fields to be filled and return form
        model.addAttribute("formData", new RegisterFormData());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute @Valid RegisterFormData formData, BindingResult result, RedirectAttributes redirectAttributes) {
        //If the 'password' field is not the same as the 'Confirm password' field, flash and error
        if (!formData.getPassword().equals(formData.getConfirm_password())){
            result.rejectValue("confirm_password", "confirm_password.invalid", "The passwords do not match");
        }
        //If the user already exist, flash that user already exist
        if(DatabaseController.authenticateUser(formData.getUsername(), formData.getPassword())){
            result.rejectValue("username", "username.invalid", "This account already exist");
        }
        //If there are any other errors (e.g. password is too short), flash them
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            System.out.println(result.getAllErrors().toString());
            return "redirect:/register";
        }
        //If everything is gucci then create a new user and add it to the database. Redirect to login form for user to login!
        User newUser = new User(formData.getUsername(), formData.getYear(), formData.getPassword(), false);
        DatabaseController.insert(newUser);
        return "redirect:/login";
    }

    @GetMapping("/select-courses-completed")
    public String showForm(Model model) {
        ClassListRead c = new ClassListRead();
        c.ReadTextFile("Chemistry");
        ArrayList<String> classes = c.classes;
        model.addAttribute("options", classes);
        return "select-courses";
    }

    @PostMapping("/select-courses-completed")
    public String processForm(@ModelAttribute("selectedOptions") List<String> selectedOptions) {
        // Handle the form submission
        return "result";
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

    /**
     * Helper method to load courses into the totalCourses array
     */
    public static void initialize(){
        totalCourses = new ArrayList<>();
        String longCSV = "large_courses.csv"; //pulls from csv of all courses
        importCoursesFromCSV(longCSV); //imports information as data we can use
    }

    /**
     * Helper method to import courses from csv file
     * @param ext: name of file (has to be in: src/main/java/edu/gcc/comp350/team4project/)
     */
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

}
