package edu.gcc.comp350.team4project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Session {
    private static User currentUser;
    private static Search searchBox;
    private static Schedule tempSchedule;
    private static ArrayList<Course> totalCourses;
    private static Scanner input;


    public static void main(String[] args) throws Exception {
        createSession();
        //TODO: after, figure out where to close Scanner
    }

    public static void createSession() throws Exception {
        totalCourses = new ArrayList<>();

        String longCSV = "large_courses.csv";
        String shortCSV = "small_courses.csv";
        Scanner tempS = new Scanner(System.in);
        while(true){
            System.out.println("Type 1 to run with small list of courses");
            System.out.println(shortCSV);
            System.out.println("Type 2 to run with total list of courses");
            System.out.println(longCSV);
            String input = tempS.nextLine();
            if(input.equals("1")){
                importCoursesFromCSV(shortCSV);
                break;
            }
            else if(input.equals("2")){
                importCoursesFromCSV(longCSV);
                break;
            }
            else{
                System.out.println("bad input try again");
            }
        }


        while(true){
            System.out.println("Type 1 to run with existing database.");
            DatabaseController.printAllUsers();
            System.out.println("Type 2 to run with a cleared database. WARNING: WILL DELETE ALL STORED USERS");
            String input = tempS.nextLine();
            if(input.equals("1")){
                System.out.println("Running with existing db");
                break;
            }
            else if(input.equals("2")){
                System.out.println("Running with new db");
                DatabaseController.createNewDatabase("team4_project.db");
                DatabaseController.drop();
                DatabaseController.createNewTable();
                break;
            }
            else{
                System.out.println("bad input try again");
            }
        }
        menuLoop();
    }

    private static void menuLoop() throws Exception {
        input = new Scanner(System.in);
        boolean isLoggedIn=false, isScheduling;
        mainMenu();
        String command = input.nextLine().toLowerCase();
        while (!command.equals("exit")) {
            if (command.equals("u")) {
                if(logInUser()) isLoggedIn = true;
            }
            else if (command.equals("n")) {
                if (createNewUser()) isLoggedIn = true;//make new account
            }
            else if(command.equals("g")){
                currentUser = new User("","","",true);
                System.out.println("Logged in as a guest");
                isLoggedIn = true;
            }
            else if (command.equals("v")){
                DatabaseController.printAllUsers();
            }
            else{
                invalidArgument();
            }

            while (isLoggedIn) {
                loggedInMenu();
                command = input.nextLine().toLowerCase();
                switch (command) {
                    case "ns" -> {
                        if(currentUser.getSchedules().size()<5){
                            String name = "";
                            Semester sem;
                            do {
                                System.out.println("Enter schedule name (5-20 characters): ");
                                name = input.nextLine();
                            } while (name.length() > 20 || name.length() < 5);
                            while (true) {
                                System.out.println("Enter 'f' for fall or 's' for spring");
                                String semester = input.nextLine().toLowerCase();
                                if (semester.equals("f")) {
                                    sem = Semester.FALL;
                                    break;
                                }
                                if (semester.equals("s")) {
                                    sem = Semester.SPRING;
                                    break;
                                }
                            }
                            tempSchedule = new Schedule(name, sem);
                            System.out.println("Creating schedule " + tempSchedule.getScheduleName() + " (" + tempSchedule.getSemester().toString().toLowerCase() + " semester)");
                            isScheduling = true;
                            while (isScheduling) {
                                isScheduling = scheduleMenu();
                            }

                        }
                        else{
                            System.out.println("Maximum of " + currentUser.getNumMaxSchedules() + " schedules allowed. Try deleting one");

                        }


                    }
                    case "ds" -> {
                        if(currentUser.getSchedules().size()>0){
                            System.out.println("Pick a schedule to delete: ");
                            for(int i = 0; i<currentUser.getSchedules().size();i++){
                                System.out.println("" + (i+1) + ": " + currentUser.getSchedules().get(i));
                            }
                            String numberToDelete = input.nextLine();
                            switch (numberToDelete) {
                                case "1" -> {
                                    currentUser.removeSchedule(0);
                                    System.out.println("Schedule deleted!");
                                }
                                case "2" -> {
                                    try {
                                        currentUser.removeSchedule(1);
                                        System.out.println("Schedule deleted!");
                                    } catch (Exception ignored) {
                                        invalidArgument();
                                    }
                                }
                                case "3" -> {
                                    try {
                                        currentUser.removeSchedule(2);
                                        System.out.println("Schedule deleted!");
                                    } catch (Exception ignored) {
                                        invalidArgument();
                                    }
                                }
                                case "4" -> {
                                    try {
                                        currentUser.removeSchedule(3);
                                        System.out.println("Schedule deleted!");
                                    } catch (Exception ignored) {
                                        invalidArgument();
                                    }
                                }
                                case "5" -> {
                                    try {
                                        currentUser.removeSchedule(4);
                                        System.out.println("Schedule deleted!");
                                    } catch (Exception ignored) {
                                        invalidArgument();
                                    }
                                }
                                default -> invalidArgument();
                            }
                        }
                        else{
                            System.out.println("No saved schedules to delete!");
                        }
                    }
                    case "vs" -> {
                        if(currentUser.getSchedules().size()>0){
                            System.out.println("Pick a schedule to edit: ");
                            for(int i = 0; i<currentUser.getSchedules().size();i++){
                                System.out.println("" + (i+1) + ": " + currentUser.getSchedules().get(i));
                            }
                            System.out.println("\nType 'b' to go back");
                            String numberToEdit = input.nextLine();
                            switch (numberToEdit) {
                                case "1" -> {
                                    System.out.println("Editing schedule 1");
                                    tempSchedule = currentUser.getSchedules().get(0);
                                    currentUser.removeSchedule(0);
                                    isScheduling = true;
                                    while (isScheduling) {
                                        isScheduling = scheduleMenu();
                                    }
                                }
                                case "2" -> {
                                    try {
                                        System.out.println("Editing schedule 2");
                                        tempSchedule = currentUser.getSchedules().get(1);
                                        currentUser.removeSchedule(1);
                                        isScheduling = true;
                                        while (isScheduling) {
                                            isScheduling = scheduleMenu();
                                        }
                                    } catch (Exception ignored) {
                                        invalidArgument();
                                    }
                                }
                                case "3" -> {
                                    try {
                                        System.out.println("Editing schedule 3");
                                        tempSchedule = currentUser.getSchedules().get(2);
                                        currentUser.removeSchedule(2);
                                        isScheduling = true;
                                        while (isScheduling) {
                                            isScheduling = scheduleMenu();
                                        }
                                    } catch (Exception ignored) {
                                        invalidArgument();
                                    }
                                }
                                case "4" -> {
                                    try {
                                        System.out.println("Editing schedule 4");
                                        tempSchedule = currentUser.getSchedules().get(3);
                                        currentUser.removeSchedule(3);
                                        isScheduling = true;
                                        while (isScheduling) {
                                            isScheduling = scheduleMenu();
                                        }
                                    } catch (Exception ignored) {
                                        invalidArgument();
                                    }
                                }
                                case "5" -> {
                                    try {
                                        System.out.println("Editing schedule 5");
                                        tempSchedule = currentUser.getSchedules().get(4);
                                        currentUser.removeSchedule(4);
                                        isScheduling = true;
                                        while (isScheduling) {
                                            isScheduling = scheduleMenu();
                                        }
                                    } catch (Exception ignored) {
                                        invalidArgument();
                                    }
                                }
                                case "b" -> {
                                }
                                default -> invalidArgument();
                            }
                        }
                        else{
                            System.out.println("No saved schedules to edit!");
                        }
                    }
                    case "b" -> {
                        logOutUser();
                        isLoggedIn = false;
                    }
                    case "exit" -> {
                        isLoggedIn = false;
                        endSession();
                    } //exit the program
                    default -> invalidArgument();
                }
            }
            mainMenu();
            command = input.nextLine().toLowerCase();
        }
        endSession();
    }

    private static void filter() {
        String filterType;
        boolean isFiltering = true;

        while (isFiltering) {
            if (searchBox.getCurrentFilters().size() > 0) {
                System.out.println("CURRENT APPLIED FILTERS: ");
                for (Filter f: searchBox.getCurrentFilters()) System.out.println(f.getType() + ": " + f.getValue());
            }
            else System.out.println("CURRENT APPLIED FILTERS: -none-");

            System.out.println("""
                Type 'dept' to filter by department
                Type 'time' to filter by time
                Type 'days' to filter by days
                Type 'lvl' to filter by level
                Type 'r' to remove a specific filter
                Type 'ra' to filter by remove all filters
                Type 'b' to go back
                """);
            filterType = input.nextLine().toLowerCase();
            switch (filterType) {
                case "dept" -> filterDept();
                case "time" -> filterTime();
                case "days" -> filterDays();
                case "lvl" -> filterLevel();
                case "r" -> removeSpecificFilter();
                case "ra" -> searchBox.refreshFilteredCourses(); //removes all filters
                case "b" -> isFiltering = false;
                case "exit" -> endSession();
                default -> invalidArgument();
            }
        }
    }

    private static void removeSpecificFilter() {
        if(searchBox.getCurrentFilters().size()>0){
            while(true){
                System.out.println("Enter the filter number to remove it or 'b' to go back");
                System.out.println("CURRENT FILTERS:");
                for(int i = 0;i<searchBox.getCurrentFilters().size();i++){
                    System.out.println("[" + (i+1) + "] TYPE: " + searchBox.getCurrentFilters().get(i).getType() + " VALUE: " + searchBox.getCurrentFilters().get(i).getValue());
                }

                String command = input.nextLine();
                if(command.equals("b")){
                    break;
                }
                try{
                    int temp = Integer.parseInt(command);
                    if(temp >0 && temp <= searchBox.getCurrentFilters().size()){
                        String filter = searchBox.getCurrentFilters().get(temp-1).getType();
                        searchBox.removeSpecificFilter(filter);
                        break;
                    }
                    else{
                        System.out.println("Enter a valid number to remove");
                    }
                }catch(Exception ignored){
                    invalidArgument();
                }
            }
        }
        else{
            System.out.println("No current filters applied");
        }
    }

    private static void filterDept() {
        HashSet<String> departments = new HashSet<>();
        boolean isFiltering = true;
        String command;

        for (Course c: searchBox.getFilteredCourses())
            departments.add(c.getDepartmentInfo().department()); //TODO: TEST, IT SHOULD WORK THO

        while (isFiltering) {
            System.out.println("FILTERABLE DEPARTMENTS: ");
            for (String s: departments) System.out.println(s);
            System.out.println("""
                Type <department> to filter by that department
                Type 'b' to go back
                Type 'exit' to terminate the program
                """);
            command = input.nextLine().toLowerCase();
            switch (command) {
//                case "s" -> {
//                    System.out.println("Here is a list of all departments: ");
//                    for (String s: departments) System.out.println(s);
//                }
                case "b" -> isFiltering = false;
                case "exit" -> endSession();
                default -> {
                    if (departments.contains(command.toUpperCase())) {
                        searchBox.filterByDept(command.toUpperCase());
                        isFiltering = false;
                    }
                    else invalidArgument();
                }
            }
        }
    }

    private static void filterTime() {
        //TODO: figure this out
    }

    private static void filterDays() {
        HashSet<DayOfWeek> setOfDays = new HashSet<>();
        boolean isFiltering = true;
        String days;
        while (isFiltering) {
            System.out.println("""
                Enter the day you would like to filter by
                'm' - Monday
                't' - Tuesday
                'w' - Wednesday
                'r' - Thursday
                'f' - Friday
                Type 'd' if you are done adding days to filter by
                Type 'b' to go back
                Type 'exit' to terminate the program
                """);
            days = input.nextLine().toLowerCase();
            switch (days) {
                case "m" -> setOfDays.add(DayOfWeek.MONDAY);
                case "t" -> setOfDays.add(DayOfWeek.TUESDAY);
                case "w" -> setOfDays.add(DayOfWeek.WEDNESDAY);
                case "r" -> setOfDays.add(DayOfWeek.THURSDAY);
                case "f" -> setOfDays.add(DayOfWeek.FRIDAY);
                case "d" -> {
                    isFiltering = false;
                    searchBox.filterByDays(new ArrayList<>(setOfDays));
                }
                case "b" -> isFiltering = false;
                case "exit" -> endSession();
                default -> invalidArgument();
            }
        }
    }

    private static void filterLevel() {
        String level;
        boolean isFiltering = true;

        while (isFiltering) {
            System.out.println("""
                    Please enter the level you would like to filter by:
                    '1' - 100 level classes
                    '2' - 200 level classes
                    '3' - 300 level classes
                    '4' - 400 level classes
                    Type 'b' to go back
                    Type 'exit' to terminate the program
                    """);
            level = input.nextLine();
            switch (level) {
                //TODO: PROBABLY CHANGE TO STRING SO THAT THERE ISNT ACCIDENTAL INPUTS
                case "1" -> searchBox.filterByLevel("1");
                case "2" -> searchBox.filterByLevel("2");
                case "3" -> searchBox.filterByLevel("3");
                case "4" -> searchBox.filterByLevel("4");
                case "b" -> isFiltering = false;
                case "exit" -> endSession();
                default -> invalidArgument();
            }
        }
    }

    private static void search() {
        String command;
        boolean isSearching = true;

        while (isSearching) {
            System.out.println("CURRENT SEARCH QUERY: " + searchBox.getCurrentSearchPhrase());
            System.out.println("""
                    Enter a new search query (will clear any previous query)
                    Type 'c' to clear current search query
                    Type 'b' to go back
                    """);
            command = input.nextLine().toLowerCase();

            if(command.equals("c")){
                searchBox.removeSpecificFilter("phrase");
                System.out.println("Clearing search phrase");isSearching = false;
            }
            else if (command.equals("b")){
                isSearching = false;
            }
            else{
                if(command.length()>30){
                    System.out.println("Too long of a search term");
                }
                else{
                    searchBox.removeSpecificFilter("phrase");
                    searchBox.filterByPhrase(command);
                    isSearching = false;
                }
            }
        }
    }


    private static boolean logInUser() {
        while(true) {
            input = new Scanner(System.in);
            System.out.println("Enter username or type 'b' to go back:");
            String name = input.nextLine().toLowerCase();
            if(name.equals("b")){
                return false;
            }
            System.out.println("Enter password");
            String password = input.nextLine().toLowerCase();
            if (DatabaseController.authenticateUser(name,password)) {
                System.out.println("Log in successful!");
                currentUser = DatabaseController.pullUser(name);
                return true;
            }
            System.out.println("Log in details do not match any stored users");

        }
    }
    private static void logOutUser() {
        if(currentUser.isGuest()) {
            System.out.println("Ending guest session");

        }
        else {
            System.out.println("Logging out!");
        }
        DatabaseController.updateUser(currentUser);
        currentUser = null;
    }

    private static boolean createNewUser() {
        System.out.println("Creating new user!");
        String name, password, year = "";

        while (true) {
            System.out.println("""
                    Max characters allowed: 20
                    Min characters allowed: 5
                    No whitespace allowed
                    Type 'b' to cancel new account creation
                    Type 'exit' to exit
                    Enter a username:
                    """);
            name = input.nextLine().toLowerCase();
            if (name.equals("b")) return false;
            else if (name.equals("exit")) endSession();
            else if (name.length() > 20) System.out.println("Username is too long, must be less then 20 characters!");
            else if (name.length() < 5) System.out.println("Username is too short, 5 characters is the minimum!");
            else if (name.matches(".*\\s+.*")) System.out.println("Username cannot contain whitespace");
            else if (DatabaseController.checkIfUserInDB(name)) System.out.println("Username already exists. Try again");
            else break;
        }
        while (true) {
            System.out.println("""
                    Max characters allowed: 20
                    No whitespace allowed
                    Type 'b' to cancel new account creation
                    Type 'exit' to exit
                    Enter a password:
                    """);
            password = input.nextLine().toLowerCase();
            if (password.equals("b")) return false;
            else if (password.equals("exit")) endSession();
            else if (password.matches(".*\\s+.*")) System.out.println("Password cannot contain whitespace");
            else if (password.length() > 20) System.out.println("Password is too long");
            else break;
        }

        while (!year.equals("freshman") && !year.equals("sophomore") && !year.equals("junior") && !year.equals("senior")) {
            System.out.println("Enter class year you are scheduling for:");
            System.out.println("""
                    'f' for freshman
                    's' for sophomore
                    'j' for junior
                    'e' for senior
                    Type 'b' to cancel new account creation
                    Type 'exit' to quit the program
                    """);
            String command = input.nextLine().toLowerCase();

            switch (command) {
                case "b" -> {
                    return false;
                }
                case "exit" -> endSession();
                case "f" -> year = "freshman";
                case "s" -> year = "sophomore";
                case "j" -> year = "junior";
                case "e" -> year = "senior";
                default -> invalidArgument();
            }
        }
        currentUser = new User(name, year, password, false);
        DatabaseController.insert(currentUser);
        System.out.println("Account info: \nUsername: " + name + "\nPassowrd: " + password + "\nYear: " + year + "\n");
        return true;
    }


    private static void mainMenu() {
        System.out.println("""
                Type 'u' to login as a user
                Type 'g' to login as a guest
                Type 'n' to create a new account
                Type 'exit' to terminate the program
                """);
    }

    private static void loggedInMenu() {
        System.out.println("""
                        Type 'ns' to create a new schedule
                        Type 'ds' to delete a schedule
                        Type 'vs' to view saved schedule or edit
                        Type 'b' to go logout
                        Type 'exit' to terminate the program
                        """);
    }

    private static boolean scheduleMenu() throws Exception {
        System.out.println("""
                            Type 'a' to add courses
                            Type 'r' to remove a course
                            Type 'vc' to see your schedule as a calendar
                            Type 'vt' to see your schedule as a table
                            Type 'sb' to save and go back
                            Type 'exit' to terminate the program
                            """);


        String command = input.nextLine().toLowerCase();
        switch (command) {
            case "a" -> addCourseMenu();
            case "r" -> removeCourseFromSchedule();
            case "vc" -> System.out.println(tempSchedule.toCalenderView());
            case "vt" -> System.out.println(tempSchedule.toTableView());
            case "sb" -> {
                try {
                    currentUser.saveScheduleToUser(tempSchedule);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
                return false;
            }
            case "exit" -> endSession();
            default -> invalidArgument();
        }
        return true;
    }


    private static void removeCourseFromSchedule(){

        if(tempSchedule.getCourses().size()>0) {
            while(true){
                for (Course c : tempSchedule.getCourses()) System.out.println(c);
                System.out.println("""
                        Enter the course REFNUM to remove
                        Type 'ra' to remove all
                        Type 'b' to go back
                        """);
                String command = input.nextLine().toLowerCase();
                if (command.equals("ra")){
                    tempSchedule.removeAllCourses();
                    System.out.println("Removed all courses!");
                    break;
                }
                else if (command.equals("b")) break;
                else {
                    int commandInt = -100;
                    try {
                        commandInt = Integer.parseInt(command);
                        boolean flag = false;
                        Course tempCourse = null;
                        for (Course c : tempSchedule.getCourses()) {
                            if (c.getRefNum() == commandInt) {
                                flag = true;
                                tempCourse = c;
                                break;
                            }
                        }
                        if (flag) {
                            try{
                                tempSchedule.removeCourse(tempCourse);
                            }catch(Exception e){
                                System.out.println(e.getMessage());
                            }
                        } else {
                            System.out.println("Not a valid refnum!");
                        }
                    } catch (Exception ignored) {
                        invalidArgument();
                    }
                }
            }
        }
    }


    private static void addCourseMenu() throws Exception {
        searchBox = new Search(totalCourses, tempSchedule.getSemester());
        while (true) {
            System.out.println("COURSES (WITH ANY SEARCH OR FILTERS APPLIED):");
            for (Course c: searchBox.getFilteredCourses()) System.out.println(c);
            System.out.println("""
                Type <REFNUM> to add the course with that reference number
                Type 's' to apply or remove a search query
                Type 'f' to apply or remove a filter
                Type 'b' to go back
                """);
            String command = input.nextLine().toLowerCase();
            //if (command.equals("r")) addByReference();
            if (command.equals("f")) filter();
            else if (command.equals("s")) search();
            else if (command.equals("b")) break;
            else {
                int commandInt = -100;
                try{
                    commandInt = Integer.parseInt(command);
                    boolean flag = false;
                    for(Course c : searchBox.getFilteredCourses()){
                        if(c.getRefNum() == commandInt){
                            flag = true;
                            break;
                        }
                    }
                    if(flag){
                        Course c = searchBox.searchForRefNum(commandInt);
                        if (c != null) {
                            try {
                                tempSchedule.addCourse(c);
                                System.out.println("Course: " + c.getName() + " added!");
                                break;
                            }catch(Exception e){
                                System.out.println(e.getMessage());
                                break;
                            }
                        }
                    }
                    else{
                        System.out.println("Not a valid refnum!");
                        break;
                    }
                }catch(Exception ignored){
                    invalidArgument();
                }
            }
        }
    }

    private static void invalidArgument() { System.out.println("Invalid argument!"); }


    private static void endSession() {
        System.out.println("terminating program");
        System.exit(0);
    }


    public static void importCoursesFromCSV(String ext) throws IOException {
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
                } catch (Exception ignored) {

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

                }

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
                    //
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
                    //
                }

                professorName = data[13];

                try {
                    refNum = Integer.parseInt(data[14]);
                } catch(Exception ignore) {
                    //
                }

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