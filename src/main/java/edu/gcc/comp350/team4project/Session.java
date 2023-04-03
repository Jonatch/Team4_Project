package edu.gcc.comp350.team4project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

public class Session {
    private static User currentUser;
    private static Search searchBox;
    private static Schedule tempSchedule;
    private static ArrayList<Course> totalCourses;
    private static Scanner input;


    public static void main(String[] args) throws Exception {
        createSession();
        //input.close(); //TODO: test to see if this breaks anything
    }

    public static void createSession() throws Exception {
        totalCourses = new ArrayList<>();

        String longCSV = "large_courses.csv";
        importCoursesFromCSV(longCSV);
        Scanner tempS = new Scanner(System.in);


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
        mainMenu(); // helper method that contains the menu print lines
        String command = input.nextLine().toLowerCase();
        while (!command.equals("exit")) {
            if (command.equals("u")) { // login as a user
                if(logInUser()) isLoggedIn = true;
            }
            else if (command.equals("n")) { // make a new account
                if (createNewUser()) isLoggedIn = true;
            }
            else if(command.equals("g")){ // guest login
                currentUser = new User("","","",true);
                System.out.println("Logged in as a guest");
                isLoggedIn = true;
            }
            else if (command.equals("v")) DatabaseController.printAllUsers(); // hidden command, for testing
            else invalidArgument(); // invalid argument

            while (isLoggedIn) {
                loggedInMenu(); // helper method that contains logged in menu print lines
                command = input.nextLine().toLowerCase();
                switch (command) {
                    case "ns" -> { // making a new schedule
                        if(currentUser.getSchedules().size()<5){
                            String name = "";
                            Semester sem;
                            do {
                                System.out.println("Enter schedule name (5-20 characters): ");
                                name = input.nextLine();
                            } while (name.length() > 20 || name.length() < 5);
                            while (true) { // choose if you want it to be for fall or spring
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
                            tempSchedule = new Schedule(name, sem); // makes a new schedule
                            System.out.println("Creating schedule " + tempSchedule.getScheduleName() + " (" + tempSchedule.getSemester().toString().toLowerCase() + " semester)");
                            isScheduling = true;
                            while (isScheduling) {
                                isScheduling = scheduleMenu(); // print scheduling menu
                            }
                        }
                        else System.out.println("Maximum of " + currentUser.getNumMaxSchedules() + " schedules allowed. Try deleting one");
                    }
                    case "ds" -> { // delete a schedule
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
                        else System.out.println("No saved schedules to delete!");
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
                                case "b" -> {}
                                default -> invalidArgument();
                            }
                        }
                        else System.out.println("No saved schedules to edit!");
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
            if (searchBox.getCurrentFilters().size() > 0) { // print current filters
                System.out.println("CURRENT APPLIED FILTERS: ");
                for (Filter f: searchBox.getCurrentFilters()) System.out.println("-" + f.getType() + ": " + f.getValue() + "-");
            }
            else System.out.println("CURRENT APPLIED FILTERS: -none-");

            System.out.println("""
                Type 'dept' to filter by department
                Type 'time' to filter by time
                Type 'days' to filter by days
                case "cred" -> filterCredits();
                Type 'lvl' to filter by course level
                Type 'r' to remove a specific filter
                Type 'ra' to remove all filters
                Type 'b' to go back and view filtered courses
                """);
            filterType = input.nextLine().toLowerCase();
            switch (filterType) {
                case "dept" -> filterDept(); // filter by department
                case "time" -> filterTime(); // filter by the time
                case "days" -> filterDays(); // filter by number of days
                case "cred" -> filterCredits(); // filter by number of credits
                case "lvl" -> filterLevel(); // filter by level
                case "r" -> removeSpecificFilter(); // remove a specific filter
                case "ra" -> {
                    searchBox.getFilteredCourses().clear(); // removes all filters
                    searchBox.refreshFilteredCourses(); // resets filtered courses
                }
                case "b" -> isFiltering = false;
                case "exit" -> endSession();
                default -> invalidArgument();
            }
        }
    }

    /**
     * This is a helper function that will call the filterByCredits function in the search class
     * This helper function handles input and prints direction messages to the console
     */
    private static void filterCredits() {
        searchBox.removeSpecificFilter("credit"); // remove previous filter
        String credits;
        boolean isFiltering = true;

        while (isFiltering) {
            System.out.println("""
                    Please enter the level you would like to filter by:
                    '1' - 1 credit courses
                    '2' - 2 credit courses
                    '3' - 3 credit courses
                    '4' - 4 credit courses
                    Type 'b' to go back
                    """);
            credits = input.nextLine();
            switch (credits) {
                case "1" -> {
                    searchBox.filterByCredits("1");
                    isFiltering = false;
                }
                case "2" -> {
                    searchBox.filterByCredits("2");
                    isFiltering = false;
                }
                case "3" -> {
                    searchBox.filterByCredits("3");
                    isFiltering = false;
                }
                case "4" -> {
                    searchBox.filterByCredits("4");
                    isFiltering = false;
                }
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
                if (command.equals("b")) break;

                try{
                    int temp = Integer.parseInt(command);
                    if(temp >0 && temp <= searchBox.getCurrentFilters().size()){
                        String filter = searchBox.getCurrentFilters().get(temp-1).getType();
                        searchBox.removeSpecificFilter(filter);
                        break;
                    }
                    else System.out.println("Enter a valid number to remove");
                }catch(Exception ignored){
                    invalidArgument();
                }
            }
        }
        else System.out.println("No current filters applied");
    }

    /**
     * This is a helper function that will call the filterByDept function in the search class
     * This helper function handles input and prints direction messages to the console
     */
    private static void filterDept() {
        searchBox.removeSpecificFilter("department"); // remove previous filter
        HashSet<String> departments = new HashSet<>();
        boolean isFiltering = true;
        String command;

        for (Course c: searchBox.getFilteredCourses())
            departments.add(c.getDepartmentInfo().department()); // add to HashSet all department codes from

        while (isFiltering) {
            System.out.println("FILTERABLE DEPARTMENTS: ");
            for (String s: departments) System.out.println(s); // print every department possible to filter by
            System.out.println("""
                Type <department> to filter by that department
                Type 'b' to go back
                """);
            command = input.nextLine().toLowerCase();
            switch (command) {
                case "b" -> isFiltering = false;
                case "exit" -> endSession();
                default -> {
                    if (departments.contains(command.toUpperCase())) { // check that input is valid
                        searchBox.filterByDept(command.toUpperCase());
                        isFiltering = false;
                    }
                    else invalidArgument();
                }
            }
        }
    }

    private static void filterTime() {

        searchBox.removeSpecificFilter("time");

        int startTime = 0;
        int endTime = 0;
        while (true) {
            System.out.println("""
                Enter your time range in the form: __-__
                example: 10-13
                Note: time is hours 1-24
                Enter 'b' to go back
                """);
            String command = input.nextLine().toLowerCase();
            if (command.equals("b")) break;
            else if (command.equals("exit")) endSession();
            else{
                String[] parts = command.split("-");
                try{
                    startTime = Integer.parseInt(parts[0]);
                    endTime = Integer.parseInt(parts[1]);

                }
                catch(Exception ignore){
                    invalidArgument();
                }
                if (startTime > 24 || startTime < 1){
                    System.out.println("Start time ust be between 1 and 24");
                }
                if (endTime > 24 || endTime < 1){
                    System.out.println("End time ust be between 1 and 24");
                }
                if(endTime < startTime){
                    System.out.println("Start time cannot be after the end time");
                }
                LocalTime start = LocalTime.of(startTime, 0, 0);
                LocalTime end = LocalTime.of(endTime, 0, 0);
                searchBox.filterByTime(new ArrayList<>(List.of(start,end)));
                break;
            }
        }

    }

    private static void filterDays() {
        searchBox.removeSpecificFilter("days");
        HashSet<DayOfWeek> setOfDays = new HashSet<>();
        String days;

        Set<Character> charSet = new HashSet<>();

        do {
            System.out.println("""
                    Enter the days you would like to filter by as a list of up to 5 characters(MTWRF):
                    'm' - Monday
                    't' - Tuesday
                    'w' - Wednesday
                    'r' - Thursday
                    'f' - Friday
                    Type 'b' to go back
                    """);
            System.out.println("Enter the days you would like to filter by as a list of up to 5 characters(MTWRF) or type 'b' to go back: ");
            days = input.nextLine().toLowerCase();
            if(days.equals("b")){
                break;
            }
            if(days.equals("exit")) endSession();
        } while (!days.matches("[mtwrf]{1,5}") || !noDuplicates(days, charSet));

        for(char c : charSet){
            if(c=='m'){
                setOfDays.add(DayOfWeek.MONDAY);
            }
            else if(c=='t'){
                setOfDays.add(DayOfWeek.TUESDAY);
            }
            else if(c=='w'){
                setOfDays.add(DayOfWeek.WEDNESDAY);
            }
            else if(c=='r'){
                setOfDays.add(DayOfWeek.THURSDAY);
            }
            else if(c=='f'){
                setOfDays.add(DayOfWeek.FRIDAY);
            }
        }
        searchBox.filterByDays(new ArrayList<>(setOfDays));
    }

    private static boolean noDuplicates(String input, Set<Character> charSet) {
        for (char c : input.toCharArray()) {
            if (!charSet.add(c) || !Character.isLetter(c)) {
                invalidArgument();
                return false;
            }
        }
        return true;
    }

    /**
     * This is a helper function that will call the filterByLevel function in the search class
     * This helper function handles input and prints direction messages to the console
     */
    private static void filterLevel() {
        String level;
        boolean isFiltering = true;
        searchBox.removeSpecificFilter("level"); // remove previous level filter

        while (isFiltering) {
            System.out.println("""
                    Please enter the level you would like to filter by:
                    '1' - 100-199 level classes
                    '2' - 200-299 level classes
                    '3' - 300-399 level classes
                    '4' - 400-499 level classes
                    Type 'b' to go back
                    """);
            level = input.nextLine();
            switch (level) {
                case "1" -> {
                    searchBox.filterByLevel("100");
                    isFiltering=false;
                }
                case "2" -> {
                    searchBox.filterByLevel("200");
                    isFiltering=false;
                }
                case "3" -> {
                    searchBox.filterByLevel("300");
                    isFiltering=false;
                }
                case "4" -> {
                    searchBox.filterByLevel("400");
                    isFiltering=false;
                }
                case "b" -> isFiltering = false;
                case "exit" -> endSession();
                default -> invalidArgument();
            }
        }
    }

    /**
     * This is a helper function for search. Will call the searchByPhrase method from the search class
     * Also has options for user to clear current search and go back
     */
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

            if (command.equals("c")) {
                searchBox.removeSpecificFilter("phrase");
                System.out.println("Clearing search phrase");
                isSearching = false;
            }
            else if (command.equals("b")) isSearching = false;
            else{
                if (command.length() > 30) System.out.println("Too long of a search term");
                else {
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

    /**
     * This function is how the user will create a new user in the database.
     * @return if the user is logged in or not
     */
    private static boolean createNewUser() {
        System.out.println("Creating new user!");
        String name, password, year = "";

        while (true) {
            System.out.println("""
                    Max characters allowed: 20
                    Min characters allowed: 5
                    No whitespace allowed
                    Type 'b' to cancel new account creation
                    Enter a username:
                    """);
            name = input.nextLine().toLowerCase();
            if (name.equals("b")) return false;
            else if (name.equals("exit")) endSession();
            else if (name.length() > 20) System.out.println("Username is too long, must be less then 20 characters!");
            else if (name.length() < 5) System.out.println("Username is too short, 5 characters is the minimum!");
            else if (name.matches(".*\\s+.*")) System.out.println("Username cannot contain whitespace"); // regex expression,
            else if (DatabaseController.checkIfUserInDB(name)) System.out.println("Username already exists. Try again");
            else break;
        }
        while (true) {
            System.out.println("""
                    Max characters allowed: 20
                    No whitespace allowed
                    Type 'b' to cancel new account creation
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
        DatabaseController.insert(currentUser); //(new User(name, year, password, false);
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
                        """);
    }

    private static boolean scheduleMenu() throws Exception {
        System.out.println("""
                            Type 'a' to add courses
                            Type 'r' to remove a course
                            Type 'vc' to see your schedule as a calendar
                            Type 'vt' to see your schedule as a table
                            Type 'sb' to save and go back
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
            System.out.println("COURSES:");
            for (Course c: searchBox.getFilteredCourses()) System.out.println(c);
            System.out.println("NUMBER OF APPLIED FILTERS/SEARCH: " + searchBox.getCurrentFilters().size());
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
            else if (command.equals("exit")) endSession();
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