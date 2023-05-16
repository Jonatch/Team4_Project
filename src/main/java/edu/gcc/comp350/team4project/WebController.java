package edu.gcc.comp350.team4project;

import edu.gcc.comp350.team4project.forms.*;

import org.apache.tomcat.jni.Local;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sound.midi.Soundbank;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
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
    // Session Data
    private static User currentUser;
    private Semester semester;
    private static SearchController searchBox;
    private static Schedule tempSchedule;
    private static ArrayList<Course> totalCourses;
    private static ArrayList<String> unCheckedItems;

    private String major;

    @RequestMapping("/")
    public String home(Model model) {
        //Getting all the courses loaded into totalCourses\
        initializeCSVCourses();
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

    @PostMapping("/create-event")
    @ResponseBody
    public ResponseEntity<String> addEvent(@RequestBody Map<String, Object> eventData) {
        // Process the received data
        String eventName = (String) eventData.get("name");
        if(eventName.isEmpty() || eventName.length()>20){
            return ResponseEntity.badRequest().body("Name must be 1-20 characters");
        }


        ArrayList<String> tempDays = (ArrayList<String>) eventData.get("days");
        if(tempDays.size()==0){
            return ResponseEntity.badRequest().body("Must select a day");
        }
        ArrayList<DayOfWeek> days = new ArrayList<>();
        for(String s : tempDays){
            days.add(DayOfWeek.valueOf(s.toUpperCase()));
        }

        String tempStartTime;
        String tempEndTime;
        try {
            tempStartTime = (String) eventData.get("startTime");
            tempEndTime = (String) eventData.get("endTime");
        }catch(Exception ignored){
            return ResponseEntity.badRequest().body("Must input times fully");
        }

        if(tempEndTime.isEmpty() || tempEndTime.isEmpty()){
            return ResponseEntity.badRequest().body("Must select start and end time");
        }

        LocalTime startTime = LocalTime.parse(tempStartTime);
        LocalTime endTime = LocalTime.parse(tempEndTime);

        if(startTime.isBefore(LocalTime.of(8,0))){
            return ResponseEntity.badRequest().body("Events must start after 7:59 AM");
        }

        if(endTime.isAfter(LocalTime.of(21,0))){
            return ResponseEntity.badRequest().body("Event must end before 9 PM");
        }

        if(endTime.isBefore(startTime) || startTime.isAfter(endTime)){
            return ResponseEntity.badRequest().body("Time input must make sense");
        }

//
//
//        System.out.println("Event Name: " + eventName);
//        System.out.println("Days: " + days);
//        System.out.println("Start Time: " + startTime);
//        System.out.println("End Time: " + endTime);

        ScheduleElement temp = new Event(eventName, days,startTime,endTime, "");
        if(addEvent(temp)){
            return ResponseEntity.ok("Event added successfully");
        }else{
            return ResponseEntity.badRequest().body("Event conflicts with schedule!");
        }
    }
    @PostMapping("/remove-popup")
    public String getRemoveCourses(Model model) {
        ArrayList<ScheduleElement> elements = tempSchedule.getEvents();
        model.addAttribute("coursesRemove", elements);
        return "fragments/remove-courses-popup :: remove-popup-content"; // Return the updated content of the popup
    }

    @PostMapping("/search-box")
    public String search(@RequestParam("query") String query, Model model) {
        searchBox.removeSpecificFilter(FilterTypes.PHRASE);
        searchBox.filterByPhrase(query);
        model.addAttribute("courses", searchBox);
        return "fragments/search :: search-results";
    }

    @PostMapping("/remove-course")
    @ResponseBody
    public String doRemoveCourses(@RequestBody Map<String, String[]> requestBody) {
        String[] selectedCourses = requestBody.get("selectedCourses");
        for(String s : selectedCourses){
            int id = Integer.parseInt(s);
            tempSchedule.removeEventByID(id);
        }
        return "Course removed!"; // Return the updated content of the popup
    }


    @PostMapping("/add-course")
    @ResponseBody
    public String handleButtonClick(@RequestParam("parameter") int parameter, Model model) {
        Course c = searchBox.searchForRefNum(parameter);

        System.out.println(c);
        if (addEvent(c)) {
            printCalendarView(tempSchedule,model);
            return "true";

        }
        else {
            return "false";
        }
    }


    @PostMapping("/create-schedule")
    public String createSchedule(@ModelAttribute ScheduleFormData scheduleFormData) {
        boolean selectCourses;
        if (scheduleFormData.getSemester().equalsIgnoreCase("spring")) {
            semester = Semester.SPRING;
        } else if (scheduleFormData.getSemester().equalsIgnoreCase("fall")) {
            semester = Semester.FALL;
        }
        major = scheduleFormData.getMajor();
        selectCourses = scheduleFormData.getSuggestCourses();

        Schedule newSchedule = new Schedule(scheduleFormData.getName(), semester);
        tempSchedule = newSchedule;
        searchBox = new SearchController(totalCourses, tempSchedule.getSemester());
        currentUser.saveScheduleToUser(tempSchedule);

        if (selectCourses && !major.equals("Other")) {
            return "redirect:/select-courses-completed/";
        }
        return "redirect:/edit-schedule/" + newSchedule.getScheduleName();
    }

    @GetMapping("/select-courses-completed")
    public String showForm(Model model) {
        ClassListRead c = new ClassListRead();
        c.ReadTextFile(major);
        ArrayList<String> classes = c.classes;
        model.addAttribute("options", classes);
        return "select-courses";
    }

    @PostMapping("/select-courses-completed")
    public String processForm(@RequestParam(value = "selected", required = false) ArrayList<String> selectedStrings, Model model) {
        unCheckedItems = new ArrayList<>();
        ClassListRead c = new ClassListRead();
        c.ReadTextFile(major);
        ArrayList<String> classes = c.classes;
        for (String s : classes) {
            if (selectedStrings != null) {
                if (!selectedStrings.contains(s)) {
                    unCheckedItems.add(s);
                }
            }
            else {
                unCheckedItems = classes;
            }
        }
        c.ClassesSuggest(semester);
        model.addAttribute("uncheckedItems", unCheckedItems);

        //addEvent
        //loop through and add all possible
        return "redirect:/edit-schedule/" + tempSchedule.getScheduleName();
    }

    @GetMapping("/login")
    public String login(Model model, RedirectAttributes redirectAttributes) {
        // If there are is a current user then redirect to the main page
        if (currentUser != null) {
            return "redirect:/";
        }
        if (redirectAttributes.containsAttribute("errors")) {
            model.addAttribute("errors", redirectAttributes.getFlashAttributes());
        }
        model.addAttribute("formData", new LoginFormData());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute @Valid LoginFormData formData, BindingResult result, RedirectAttributes redirectAttributes) {
        // Checks to see if any fields are empty before authenticating to avoid sending extra errors
        if(!(formData.getUsername().isEmpty() || formData.getPassword().isEmpty())){
            if (!DatabaseController.authenticateUser(formData.getUsername(), formData.getPassword())) {
                result.rejectValue("username", "username.invalid", "Username and password combination does not exist");
            }
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            System.out.println(result.getAllErrors().toString());
            return "redirect:/login";
        }
        currentUser = DatabaseController.pullUser(formData.getUsername());
        return "redirect:/";
    }

    @GetMapping("/create-schedule")
    public String createSchedule(Model model) {
        if(tempSchedule!=null){
            return "redirect:/";
        }
        model.addAttribute("scheduleFormData", new ScheduleFormData());
        return "create-schedule";
    }

    @GetMapping("/edit-schedule/{scheduleName}")
    public String editSchedule(@PathVariable String scheduleName, Model model) {



        //Getting all the courses loaded into totalCourses\
        initializeCSVCourses();
        //set temp Schedule of current user
        for (Schedule s : currentUser.getSchedules()){
            if(s.getScheduleName().equals(scheduleName)){
                tempSchedule = s;
            }
        }
        printCalendarView(tempSchedule,model);
        //Creating the search object
        searchBox = new SearchController(totalCourses, tempSchedule.getSemester());

        //Adding all courses to the model (or filtered ones)
        model.addAttribute("courses", searchBox);
        //adding previous value of each filter
        for(Filter f: searchBox.getCurrentFilters()){
            model.addAttribute(f.getType().toString(), f);
            System.out.println(f.getType().toString());
        }
        //Adding the current schedule being modified
        model.addAttribute("schedule", tempSchedule);
        //Filtering Form
        model.addAttribute("filterForm", new FilterFormData());
        //Adding all departments for filtering form
        model.addAttribute("departments", getAllDepartments(totalCourses));
        return "edit-schedule";
    }

    @PostMapping("/edit-schedule/{scheduleName}")
    public String doEditSchedule(@PathVariable String scheduleName, @ModelAttribute FilterFormData filterForm) {
        gettingFilters(filterForm);
        return "redirect:/edit-schedule/" + scheduleName + "";
    }

    @PostMapping("/update-schedule")
    public String saveAndExit(){
        updateUser(currentUser);
        tempSchedule=null;
        return"redirect:/";
    }

    public static ArrayList<String> getAllDepartments(ArrayList<Course> totalCourses){
        ArrayList<String> allDepartments = new ArrayList<>();
        for (Course c : totalCourses){
            if (!allDepartments.contains(c.getDepartmentInfo().department())){
                allDepartments.add(c.getDepartmentInfo().department());
            }
        }
        return allDepartments;
    }

    @PostMapping("/more-info")
    public String doLoadMoreInfo(@RequestParam("parameter") int parameter, Model model){
        System.out.println(parameter);
        Course c = searchBox.searchForRefNum(parameter);

        model.addAttribute("moreInfoCourse", c);

        System.out.println(c.getNameLabel());

        return "fragments/more-info-popup :: info-popup-content";
    }

    @PostMapping("/filter")
    public String doFilterCourses(
            @RequestParam(value="department", required = false) String department,
            @RequestParam(value="startTime", required = false) String startTime,
            @RequestParam(value="endTime", required = false) String endTime,
            @RequestParam(value = "days", required = false) String[] days,
            @RequestParam(value="credits", required = false) String credits,
            @RequestParam(value = "level", required = false) String level,
            Model model
    ) {
        if(department != null){
            System.out.println("Department: " + department);
            if(!department.equals("")){
                searchBox.removeSpecificFilter(FilterTypes.DEPT);
                searchBox.filterByDept(department);
            }else{
                searchBox.removeSpecificFilter(FilterTypes.DEPT);
            }
        }
        if(startTime != null && endTime != null){
            System.out.println("Start time: " + startTime);
            System.out.println("End time: " + endTime);
            if(!startTime.equals("") && !endTime.equals("")){
                searchBox.removeSpecificFilter(FilterTypes.TIME);
                searchBox.removeSpecificFilter(FilterTypes.TIME);
                String[] startTokens = startTime.split(":");
                LocalTime start = LocalTime.of(Integer.parseInt(startTokens[0]), Integer.parseInt(startTokens[0]), Integer.parseInt(startTokens[0]));
                String[] endTokens = endTime.split(":");
                LocalTime end = LocalTime.of(Integer.parseInt(endTokens[0]), Integer.parseInt(endTokens[0]), Integer.parseInt(endTokens[0]));
                searchBox.filterByTime(new ArrayList<>(List.of(start,end)));
            }else{
                searchBox.removeSpecificFilter(FilterTypes.TIME);
            }
        }
        if(days != null) {
            System.out.println("Days: " + Arrays.toString(days));
            searchBox.removeSpecificFilter(FilterTypes.DAYS);
            ArrayList<DayOfWeek> setOfDays = new ArrayList<>();
            for(String s: days){
                if(s.equals("M")){
                    setOfDays.add(DayOfWeek.MONDAY);
                }
                if(s.equals("T")){
                    setOfDays.add(DayOfWeek.TUESDAY);
                }
                if(s.equals("W")){
                    setOfDays.add(DayOfWeek.WEDNESDAY);
                }
                if(s.equals("R")){
                    setOfDays.add(DayOfWeek.THURSDAY);
                }
                if(s.equals("F")){
                    setOfDays.add(DayOfWeek.FRIDAY);
                }
            }
            if(!setOfDays.isEmpty()){
                searchBox.filterByDays(setOfDays);
            }
        }
        else{
            searchBox.removeSpecificFilter(FilterTypes.DAYS);
        }
        if(credits != null){
            System.out.println("Credits: " + credits);
            if(!credits.equals("")){
                searchBox.removeSpecificFilter(FilterTypes.CRED);
                searchBox.filterByCredits(credits);
            }else{
                searchBox.removeSpecificFilter(FilterTypes.CRED);
            }
        }
        if(level != null) {
            System.out.println("level: " + level);
            if (!level.equals("")) {
                searchBox.removeSpecificFilter(FilterTypes.LVL);
                searchBox.filterByLevel(level);
            } else {
                searchBox.removeSpecificFilter(FilterTypes.LVL);
            }
        }
        model.addAttribute("courses", searchBox);
        System.out.println("Refreshing course list ...");
        return "fragments/search :: search-results";
    }

    public static void gettingFilters(FilterFormData form){
        if(!form.getDepartment().equals("")){
            searchBox.removeSpecificFilter(FilterTypes.DEPT);
            searchBox.filterByDept(form.getDepartment());
            System.out.println(searchBox.getFilteredCourses());
        }
        else{
            searchBox.removeSpecificFilter(FilterTypes.DEPT);
        }
        if(!form.getStartTime().equals("") && !form.getEndTime().equals("")){
            searchBox.removeSpecificFilter(FilterTypes.TIME);
            searchBox.removeSpecificFilter(FilterTypes.TIME);
            String[] startTokens = form.getStartTime().split(":");
            LocalTime start = LocalTime.of(Integer.parseInt(startTokens[0]), Integer.parseInt(startTokens[0]), Integer.parseInt(startTokens[0]));
            String[] endTokens = form.getEndTime().split(":");
            LocalTime end = LocalTime.of(Integer.parseInt(endTokens[0]), Integer.parseInt(endTokens[0]), Integer.parseInt(endTokens[0]));
            searchBox.filterByTime(new ArrayList<>(List.of(start,end)));
        }
        else{
            searchBox.removeSpecificFilter(FilterTypes.TIME);
        }
        if(!form.getDays().isEmpty()) {
            searchBox.removeSpecificFilter(FilterTypes.DAYS);
            searchBox.removeSpecificFilter(FilterTypes.DAYS);
            HashSet<DayOfWeek> setOfDays = new HashSet<>();
            for(String s: form.getDays()){
                if(s.equals("M")){
                    setOfDays.add(DayOfWeek.MONDAY);
                }
                if(s.equals("T")){
                    setOfDays.add(DayOfWeek.TUESDAY);
                }
                if(s.equals("W")){
                    setOfDays.add(DayOfWeek.WEDNESDAY);
                }
                if(s.equals("R")){
                    setOfDays.add(DayOfWeek.THURSDAY);
                }
                if(s.equals("F")){
                    setOfDays.add(DayOfWeek.FRIDAY);
                }
            }
        }
        else{
            searchBox.removeSpecificFilter(FilterTypes.DAYS);
        }
        if(!form.getCredits().equals("")){
            searchBox.removeSpecificFilter(FilterTypes.CRED);
            searchBox.filterByCredits(form.getCredits());
        }
        else{
            searchBox.removeSpecificFilter(FilterTypes.CRED);
        }
        if(!form.getLevel().equals("")){
            searchBox.removeSpecificFilter(FilterTypes.LVL);
            searchBox.filterByLevel(form.getLevel());
        }
        else{
            searchBox.removeSpecificFilter(FilterTypes.LVL);
        }
    }

    // This map stores the schedules by name
    private final Map<String, Schedule> schedules = new HashMap<>();

    // This method adds a Schedule to the map
    public void addSchedule(String name, Schedule schedule) {
        schedules.put(name, schedule);
    }

    @GetMapping("/view-schedule/{scheduleName}")
    public String viewSchedule(@PathVariable String scheduleName, Model model) {

        Schedule viewSchedule = null;
        for (Schedule s : currentUser.getSchedules()){
            if(s.getScheduleName().equals(scheduleName)){
                viewSchedule = s;
            }
        }
        printCalendarView(viewSchedule,model);

        for(ScheduleElement e : viewSchedule.getEvents()){
            if (e.isAnEvent()){
                try {
                    viewSchedule.removeEvent(e);
                }catch(Exception ignored){}
            }
        }
        model.addAttribute("schedule",viewSchedule);
        return "view-schedule";
    }

    @GetMapping("/register")
    public String register(Model model, RedirectAttributes redirectAttributes) {
        // If there are is a current user then redirect to the main page
        if (currentUser != null) {
            return "redirect:/";
        }
        if (redirectAttributes.containsAttribute("errors")) {
            model.addAttribute("errors", redirectAttributes.getFlashAttributes());
        }
        model.addAttribute("formData", new RegisterFormData());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute @Valid RegisterFormData formData, BindingResult result, RedirectAttributes redirectAttributes) {
        //If any data fields in registering are empty, form throws error and do not bother authenticating
        if(!(formData.getUsername().isEmpty() || formData.getPassword().isEmpty() || formData.getConfirm_password().isEmpty() || formData.getYear().isEmpty())){
            if (!formData.getPassword().equals(formData.getConfirm_password())) {
                result.rejectValue("confirm_password", "confirm_password.invalid", "The passwords do not match");
            }
            if (DatabaseController.authenticateUser(formData.getUsername(), formData.getPassword())) {
                result.rejectValue("username", "username.invalid", "This account already exist");
            }
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            System.out.println(result.getAllErrors().toString());
            return "redirect:/register";
        }
        User newUser = new User(formData.getUsername(), formData.getYear(), formData.getPassword(), false);
        DatabaseController.insert(newUser);
        return "redirect:/login";
    }

    @GetMapping("/guest-user")
    public String guestUser(Model model, RedirectAttributes redirectAttributes) {
        if (redirectAttributes.containsAttribute("errors")) {
            model.addAttribute("errors", redirectAttributes.getFlashAttributes());
        }
        //Only sets user to guest if no one is logged in
        if(currentUser==null){
            currentUser = new User("Guest","","",true);
        }
        return "redirect:/";
    }
    @PostMapping("/logout")
    public String doLogout() {
        currentUser = null;
        return "redirect:/login";
    }

    @PostMapping("/delete-schedule/{scheduleName}")
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

    /**
     * Helper method to load courses into the totalCourses array
     */
    public static void initializeCSVCourses() {
        totalCourses = new ArrayList<>();
        String longCSV = "updated_courses_2324.csv"; // pulls from csv of all courses
        importCoursesFromCSV(longCSV); // imports information as data we can use
    }

    public static void printCalendarView(Schedule calendar, Model model){
        String[][] array = new String[53][6];
        String[] header = { "Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");


        LocalTime beginning = LocalTime.of(8, 0);
        LocalTime ending = LocalTime.of(21, 0);
        int j = 0;
        while (beginning.isBefore(ending.plusMinutes(15))) {
            String timeString = beginning.format(formatter);
            array[j][0] = timeString;
            beginning = beginning.plusMinutes(15);
            j = j + 1;
        }
        List<String> course_info = new ArrayList<>();
        for (ScheduleElement c : calendar.getEvents()) {
            List<DayOfWeek> course_days = c.getDays();
            LocalTime course_start_time = c.getStartTime();
            LocalTime course_end_time = c.getEndTime();
            int start_row = (course_start_time.getHour() - 8) * 4 + course_start_time.getMinute() / 15;
            int end_row = (course_end_time.getHour() - 8) * 4 + course_end_time.getMinute() / 15;
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
            //Print TR classes
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

            //Print MWF classes
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

    }
    /**
     * Helper method to import courses from csv file
     *
     * @param ext: name of file (has to be in:
     *             src/main/java/edu/gcc/comp350/team4project/)
     */
    public static void importCoursesFromCSV(String ext) {
        // handles importing a course from csv. Takes all csv values
        // and converts to data types. Only takes in good data
        String csvFile = "src/main/java/edu/gcc/comp350/team4project/" + ext;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String courseName, departmentName, courseLevel, professorName;
                String description;
                char courseSection = ' ';
                int refNum = 0, numCredits = 0;
                Semester semester = null;
                ArrayList<DayOfWeek> days = new ArrayList<>();
                LocalTime startTime = null;
                LocalTime endTime = null;


                try {
                    refNum = Integer.parseInt(data[0]);
                } catch (Exception ignore) {
                    continue;
                }

                try {
                    if (data[1].equals("F"))
                        semester = Semester.FALL;
                    else if (data[1].equals("S"))
                        semester = Semester.SPRING;
                } catch (Exception ignored) {
                    continue;
                }

                String[] departmentInfo = data[2].split(" ");
                departmentName = departmentInfo[0];
                courseLevel = departmentInfo[1];
                try {
                    courseSection = departmentInfo[2].charAt(0);
                } catch (Exception ignored) {
                }

                courseName = data[3].toUpperCase();

                //Only if there is time data
                if(data[4].length()>0){
                    try {
                        Scanner parser = new Scanner(data[4]);
                        char[] dayChars = parser.next().toCharArray();
                        for(char d : dayChars){
                            if(d=='M'){
                                days.add(DayOfWeek.MONDAY);
                            }
                            else if(d=='T'){
                                days.add(DayOfWeek.TUESDAY);
                            }
                            else if(d=='W'){
                                days.add(DayOfWeek.WEDNESDAY);
                            }
                            else if(d=='R'){
                                days.add(DayOfWeek.THURSDAY);
                            }
                            else if(d=='F'){
                                days.add(DayOfWeek.FRIDAY);
                            }
                        }
                        // 1:00 PM-1:50 PM
                        String sTimeTemp = parser.next();
                        String[] sTimeTempSplit = sTimeTemp.split(":");
                        int sTimeHour = Integer.parseInt(sTimeTempSplit[0]);
                        int sTimeMin = Integer.parseInt(sTimeTempSplit[1]);
                        String midTimeTemp = parser.next();
                        String eTimeTemp = midTimeTemp.substring(3);
                        String[] eTimeTempSplit = eTimeTemp.split(":");
                        int eTimeHour = Integer.parseInt(eTimeTempSplit[0]);
                        int eTimeMin = Integer.parseInt(eTimeTempSplit[1]);
                        String eTimeAMPM = parser.next();

                        if(midTimeTemp.charAt(0)=='A'){
                            if(sTimeHour==12){
                                startTime = LocalTime.of(0,sTimeMin);
                            }
                            else{
                                startTime = LocalTime.of(sTimeHour,sTimeMin);
                            }
                        }
                        if(midTimeTemp.charAt(0)=='P'){
                            if(sTimeHour<12){
                                startTime = LocalTime.of(sTimeHour+12,sTimeMin);
                            }
                            else{
                                startTime = LocalTime.of(12,sTimeMin);
                            }
                        }

                        if(eTimeAMPM.charAt(0)=='A'){
                            if(eTimeHour==12){
                                endTime = LocalTime.of(0,eTimeMin);
                            }
                            else{
                                endTime = LocalTime.of(eTimeHour,eTimeMin);
                            }
                        }
                        if(eTimeAMPM.charAt(0)=='P'){
                            if(eTimeHour<12){
                                endTime = LocalTime.of(eTimeHour+12,eTimeMin);
                            }
                            else{
                                endTime = LocalTime.of(12,eTimeMin);
                            }
                        }
                    }catch(Exception ignored){}
                }else{ //This else statement gets rid of online courses or courses without time info
                    continue;
                }
                try {
                    numCredits = Integer.parseInt(data[5]);
                } catch (Exception ignored) {
                    continue;
                }

                professorName = data[6];

                try {
                    description = data[7];
                } catch (Exception ignored) {
                    continue;
                }

                totalCourses.add(new Course(refNum, departmentName, semester, courseLevel, courseSection, courseName,
                        numCredits, professorName, description.toString(), days, startTime, endTime));
            }
        } catch (IOException ignored) {
            System.out.println("CSV_FILE not found!");
        }
    }
    public ArrayList<String> getArrayList() {
        return unCheckedItems;
    }

    private ScheduleElement newEvent;
    public boolean addConflictingEvent() {
        if (newEvent.isAnEvent()) return false;
        Course conflictingCourse = (Course) newEvent; //cast to Course because it has a getSection() method
        SearchController sb = new SearchController(totalCourses, semester);

        HashSet<Course> potentialCourses = getAllOtherSections(conflictingCourse, sb.getFilteredCourses());
        ArrayList<Course> suggestions = suggestOtherCourses(potentialCourses, tempSchedule.getEvents());
        if (suggestions.size() == 0) return false;

        Course course = chooseSuggestions(suggestions);
        tempSchedule.getEvents().add(course);
        tempSchedule.setTotalCredits(tempSchedule.getTotalCredits() + course.getCredits());
        return true;
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSuggestions(@RequestParam String query) {
        SearchSuggestions searchSuggestions = new SearchSuggestions(totalCourses);
        List<String> suggestions = searchSuggestions.getSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }

    public boolean addEvent(ScheduleElement newEvent) {
        this.newEvent = newEvent;
        for (ScheduleElement event : tempSchedule.getEvents()) {
            if (event.equals(newEvent)) { //event is already added, do not add
                System.out.println("THIS EVENT IS ALREADY ADDED");
                return false;
            }
            if (event.doesCourseConflict(newEvent)) { //
                System.out.println("THIS EVENT TIMESLOT IS OCCUPIED");
                return false;
            }
        }
        tempSchedule.getEvents().add(newEvent);
        int value1 = newEvent.getCredits();
        int value2 = tempSchedule.getTotalCredits();
        int valueTotal = value1 + value2;
        tempSchedule.setTotalCredits(valueTotal);
        return true;
    }

    private ArrayList<Course> suggestOtherCourses(HashSet<Course> potentialCourses, ArrayList<ScheduleElement> events) {
        ArrayList<Course> suggestions = new ArrayList<>();

        for (Course course : potentialCourses) {
            boolean conflict = false;
            for (ScheduleElement event : events) {
                if (course.doesCourseConflict(event)) {
                    conflict = true;
                    break;
                }
            }
            if (!conflict) suggestions.add(course);
        }
        return suggestions;
    }

    private HashSet<Course> getAllOtherSections(Course course, ArrayList<Course> courses) {
        HashSet<Course> set = new HashSet<>();

        for (Course c: courses) {
            if (c.getRefNum() == course.getRefNum() && c.getSection() != course.getSection()) set.add(c);
        }

        return set;
    }

    private Course chooseSuggestions(ArrayList<Course> suggestions) {
        Scanner input = new Scanner(System.in);
        for (int i = 0; i < suggestions.size(); i++) {
            System.out.println(i + ": " + suggestions.get(i));
        }
        System.out.println("Input the number corresponding to the course you wish to add: ");
        int i = input.nextInt();

        return suggestions.get(i);
    }
    public String getMajor() {
        return major;
    }
}
