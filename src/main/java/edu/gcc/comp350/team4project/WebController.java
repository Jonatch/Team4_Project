package edu.gcc.comp350.team4project;

import edu.gcc.comp350.team4project.forms.*;

import org.apache.tomcat.jni.Local;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.config.Task;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static edu.gcc.comp350.team4project.DatabaseController.updateUser;

@SpringBootApplication
@Controller
public class WebController {

    /**
     * This class holds together form data for a User
     */
    private class RegisterFormData {
        @NotEmpty(message = "Username cannot be empty")
        @Size(min = 5, max = 250)
        private String username;

        @NotEmpty(message = "Password cannot be empty")
        @Size(min = 8)
        private String password;

        @NotEmpty(message = "Confirm password cannot be empty")
        @Size(min = 8)
        private String confirm_password;

        private String year;

        public RegisterFormData() {
            this.username = "";
            this.password = "";
            this.confirm_password = "";
            this.year = "";
        }

        public String getConfirm_password() {
            return confirm_password;
        }

        public void setConfirm_password(String confirm_password) {
            this.confirm_password = confirm_password;
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
    private class LoginFormData {
        @NotEmpty(message = "Username cannot be empty")
        @Size(min = 5, max = 250)
        private String username;

        @NotEmpty(message = "Password cannot be empty")
        @Size(min = 8)
        private String password;

        public LoginFormData() {
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

    private class ScheduleFormData {
        private String name;
        private String semester;

        public ScheduleFormData() {
            this.name = "";
            this.semester = "";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSemester() {
            return semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }
    }

    // Session Data
    private static User currentUser;
    private static SearchController searchBox;
    private static Schedule tempSchedule;
    private static ArrayList<Course> totalCourses;

    @RequestMapping("/")
    public String home(Model model) {
        // If there are is no current user then redirect to the login form
        if (currentUser == null) {
            return "redirect:/login";
        }
        // If we have a user then return the home template for that user and the user's
        // schedules
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("schedules", currentUser.getSchedules()); // Replace with your implementation to get the
                                                                     // schedules
        return "home";
    }

    @PostMapping("/create-schedule")
    public String createSchedule(@ModelAttribute ScheduleFormData scheduleFormData) {
        Semester semester = null;
        if (scheduleFormData.getSemester().equalsIgnoreCase("spring")) {
            semester = Semester.SPRING;
        } else if (scheduleFormData.getSemester().equalsIgnoreCase("fall")) {
            semester = Semester.FALL;
        }

        Schedule newSchedule = new Schedule(scheduleFormData.getName(), semester);
        tempSchedule = newSchedule;
        currentUser.saveScheduleToUser(tempSchedule);

        return "redirect:/editschedule/" + newSchedule.getScheduleName();
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

    /**
     * Helper method to load courses into the totalCourses array
     */
    public static void initialize() {
        totalCourses = new ArrayList<>();
        String longCSV = "large_courses.csv"; // pulls from csv of all courses
        importCoursesFromCSV(longCSV); // imports information as data we can use
    }

    /**
     * Helper method to import courses from csv file
     * 
     * @param ext: name of file (has to be in:
     *             src/main/java/edu/gcc/comp350/team4project/)
     */
    public static void importCoursesFromCSV(String ext) {// handles importing a course from csv. Takes all csv values
                                                         // and converts to data types. Only takes in good data
        String csvFile = "src/main/java/edu/gcc/comp350/team4project/" + ext;
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
                } catch (Exception ignored) {
                    continue;
                }

                departmentName = data[1];
                courseLevel = data[2];

                try {
                    courseSection = data[3].charAt(0);
                } catch (Exception ignored) {
                }

                courseName = data[4];

                try {
                    numCredits = Integer.parseInt(data[5]);
                } catch (Exception ignored) {
                    continue;
                }

                if (data[6].equals("M"))
                    days.add(DayOfWeek.MONDAY);

                if (data[7].equals("T"))
                    days.add(DayOfWeek.TUESDAY);

                if (data[8].equals("W"))
                    days.add(DayOfWeek.WEDNESDAY);

                if (data[9].equals("R"))
                    days.add(DayOfWeek.THURSDAY);

                if (data[10].equals("F"))
                    days.add(DayOfWeek.FRIDAY);

                try {
                    String[] tempTime = data[11].split(":");
                    int min;
                    int hour = Integer.parseInt(tempTime[0]);
                    if (tempTime[2].charAt(3) == 'A') {// these convert the time to 24 hour time
                        if (hour == 12) {
                            hour = 0;
                        }
                    } else if (tempTime[2].charAt(3) == 'P') {
                        if (hour != 12) {
                            hour += 12;
                        }
                    }
                    min = Integer.parseInt(tempTime[1]);
                    startTime = LocalTime.of(hour, min);
                } catch (Exception ignore) {
                    continue;
                }

                try {
                    String[] tempTime = data[12].split(":");
                    int hour = Integer.parseInt(tempTime[0]);
                    int min = 0;

                    if (tempTime[2].charAt(3) == 'A') { // these convert the time to 24 hour time
                        if (hour == 12)
                            hour = 0;
                    } else if (tempTime[2].charAt(3) == 'P') {
                        if (hour != 12)
                            hour += 12;
                    }
                    min = Integer.parseInt(tempTime[1]);
                    endTime = LocalTime.of(hour, min);
                } catch (Exception ignore) {
                    continue;
                }

                professorName = data[13];

                try {
                    refNum = Integer.parseInt(data[14]);
                } catch (Exception ignore) {
                    continue;
                }

                for (int i = 15; i < data.length; i++) {
                    description.append(data[i]).append(" ");
                }
                totalCourses.add(new Course(refNum, departmentName, semester, courseLevel, courseSection, courseName,
                        numCredits, professorName, description.toString(), days, startTime, endTime));
            }
        } catch (IOException ignored) {
            System.out.println("CSV_FILE not found!");
        }
    }

    @GetMapping("/login")
    public String login(Model model, RedirectAttributes redirectAttributes) {
        // TODO: add code to display login page
        if (redirectAttributes.containsAttribute("errors")) {
            model.addAttribute("errors", redirectAttributes.getFlashAttributes());
        }
        model.addAttribute("formData", new LoginFormData());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute @Valid LoginFormData formData, BindingResult result,
            RedirectAttributes redirectAttributes) {
        // TODO: add code to handle login form submission
        if (!DatabaseController.authenticateUser(formData.getUsername(), formData.getPassword())) {
            result.rejectValue("username", "username.invalid", "The username and password do not match");
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            System.out.println(result.getAllErrors().toString());
            return "redirect:/login";
        }
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

    @GetMapping("/editschedule/{scheduleName}")
    public String editSchedule(@PathVariable String scheduleName, Model model) {
        // TODO: add code to display edit schedule page for a specific schedule
        model.addAttribute("schedule", tempSchedule);
        model.addAttribute("courses", totalCourses);
        return "edit-schedule";
    }

    @PostMapping("/editschedule/{scheduleName}")
    public String doEditSchedule(@PathVariable String scheduleName, @ModelAttribute Schedule schedule) {
        // TODO: add code to handle form submission for editing a schedule
        return "redirect:/schedules";
    }

    // This map stores the schedules by name
    private final Map<String, Schedule> schedules = new HashMap<>();

    // This method adds a Schedule to the map
    public void addSchedule(String name, Schedule schedule) {
        schedules.put(name, schedule);
    }

    @GetMapping("/viewschedule/{scheduleName}")
    public String viewSchedule(@PathVariable String scheduleName, Model model) {
        for (Schedule s : currentUser.getSchedules()) {
            if (s.getScheduleName().equals(scheduleName)) {
                tempSchedule = s;
            }
        }
        String[][] array = new String[80][6];
        String[] header = { "Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };

        LocalTime beginning = LocalTime.of(4, 0);
        LocalTime ending = LocalTime.MIDNIGHT;
        int j = 0;
        while (beginning != ending) {
            array[j][0] = beginning.toString();
            beginning = beginning.plusMinutes(15);
            j = j + 1;
        }
        List<String> course_info = new ArrayList<>();
        for (Course c : tempSchedule.getCourses()) {
            List<DayOfWeek> course_days = c.getDays();
            LocalTime course_start_time = c.getStartTime();
            LocalTime course_end_time = c.getEndTime();
            int start_row = (course_start_time.getHour() - 4) * 4 + course_start_time.getMinute() / 15;
            int end_row = (course_end_time.getHour() - 4) * 4 + course_end_time.getMinute() / 15;
            int[] columns = new int[3];
            int col_idx = 0;
            for (int i = 0; i < course_days.size(); i++) {
                switch (course_days.get(i)) {
                    case MONDAY:
                        columns[col_idx] = 1;
                        col_idx++;
                        break;
                    case WEDNESDAY:
                        columns[col_idx] = 3;
                        col_idx++;
                        break;
                    case FRIDAY:
                        columns[col_idx] = 5;
                        col_idx++;
                        break;
                    default:
                        break;
                }
            }
            for (int i = start_row; i <= end_row; i++) {
                for (int k = 0; k < columns.length; k++) {
                    if (columns[k] == 0 || columns[k] % 6 == 0) {
                        continue;
                    }
                    array[i][columns[k]] = c.getName();
                }
            }

            int[] tr_columns = new int[2];
            int tr_col_idx = 0;
            for (int i = 0; i < course_days.size(); i++) {
                switch (course_days.get(i)) {
                    case TUESDAY:
                        tr_columns[tr_col_idx] = 2;
                        tr_col_idx++;
                        break;
                    case THURSDAY:
                        tr_columns[tr_col_idx] = 4;
                        tr_col_idx++;
                        break;
                    default:
                        break;
                }
            }

            for (int i = start_row; i <= end_row; i++) {
                for (int k = 0; k < tr_columns.length; k++) {
                    if (tr_columns[k] == 0 || tr_columns[k] % 6 == 0) {
                        continue;
                    }
                    array[i][tr_columns[k]] = c.getName();
                }
            }
        }
        model.addAttribute("header", header);
        model.addAttribute("times", array);

        return "schedule";
    }

    @GetMapping("/register")
    public String register(Model model, RedirectAttributes redirectAttributes) {
        // TODO: add code to display registration page
        if (redirectAttributes.containsAttribute("errors")) {
            model.addAttribute("errors", redirectAttributes.getFlashAttributes());
        }
        model.addAttribute("formData", new RegisterFormData());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute @Valid RegisterFormData formData, BindingResult result,
            RedirectAttributes redirectAttributes) {
        // TODO: add code to handle registration form submission
        if (!formData.getPassword().equals(formData.getConfirm_password())) {
            result.rejectValue("confirm_password", "confirm_password.invalid", "The passwords do not match");
        }
        if (DatabaseController.authenticateUser(formData.getUsername(), formData.getPassword())) {
            result.rejectValue("username", "username.invalid", "This account already exist");
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            System.out.println(result.getAllErrors().toString());
            return "redirect:/register";
        }
        User newUser = new User(formData.username, formData.year, formData.password, false);
        DatabaseController.insert(newUser);
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String doLogout() {
        // TODO: add code to handle logout
        currentUser = null;
        return "redirect:/login";
    }

    @PostMapping("/deleteschedule/{scheduleName}")
    public String doDeleteSchedule(@PathVariable String scheduleName) {
        // Find the index of the schedule to be removed
        int index = -1;
        List<Schedule> schedules = currentUser.getSchedules();
        for (int i = 0; i < schedules.size(); i++) {
            if (schedules.get(i).getScheduleName().equals(scheduleName)) {
                index = i;
                break;
            }
        }

        // Remove the schedule if it was found
        if (index != -1) {
            currentUser.removeSchedule(index);
            updateUser(currentUser);
        }

        return "redirect:/";
    }

}
