package edu.gcc.comp350.team4project;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
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
        importCoursesFromCSV();
        searchBox = new Search(totalCourses);
        //If uncommented, the following three lines clear the database


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
        mainMenu();
        String command = input.nextLine().toLowerCase();
        while (!command.equals("exit")) {
            if (command.equals("u")) {
                logInUser();
                isLoggedIn = true;
            }
            else if (command.equals("n")) {
                createNewUser();
                isLoggedIn = true;//make new account
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
                                System.out.println("Enter schedule name (less than 20 characters: ");
                                name = input.nextLine();
                            } while (name.length() > 20 || name.length() == 0);
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
                            System.out.println("Creating schedule " + tempSchedule.getScheduleName() + " (" + tempSchedule.getSemester() + " semester)");

                        }
                        else{
                            System.out.println("Maximum of " + currentUser.getNumMaxSchedules() + " allowed. Try deleting one");
                        }

                        isScheduling = true;

                        while (isScheduling) {
                            isScheduling = scheduleMenu();

//                            scheduleMenu();
//                            command = input.nextLine().toLowerCase();
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
                    case "b" -> {
                        isLoggedIn = false;
                        logOutUser();
                    }
                    case "exit" -> {
                        isLoggedIn = false;
                        endSession();
                    } //exit the program
                    case "vs" -> {
                       if(currentUser.getSchedules().size()>0){
                            System.out.println("Pick a schedule to edit: ");
                            for(int i = 0; i<currentUser.getSchedules().size();i++){
                                System.out.println("" + (i+1) + ": " + currentUser.getSchedules().get(i));
                            }
                            String numberToEdit = input.nextLine();
                            switch (numberToEdit) {
                                case "1" -> {
                                    System.out.println("Editing schedule 1");
                                    tempSchedule = currentUser.getSchedules().get(0);
                                    currentUser.removeSchedule(0);
                                    //Open schedule menu
                                }
                                case "2" -> {
                                    try {
                                        System.out.println("Editing schedule 2");
                                        tempSchedule = currentUser.getSchedules().get(1);
                                        currentUser.removeSchedule(1);
                                        //open schedule menu
                                    } catch (Exception ignored) {
                                        invalidArgument();
                                    }
                                }
                                case "3" -> {
                                    try {
                                        System.out.println("Editing schedule 3");
                                        tempSchedule = currentUser.getSchedules().get(2);
                                        currentUser.removeSchedule(2);
                                        //open schedule menu
                                    } catch (Exception ignored) {
                                        invalidArgument();
                                    }
                                }
                                case "4" -> {
                                    try {
                                        System.out.println("Editing schedule 4");
                                        tempSchedule = currentUser.getSchedules().get(3);
                                        currentUser.removeSchedule(3);
                                        //open schedule menu
                                    } catch (Exception ignored) {
                                        invalidArgument();
                                    }
                                }
                                case "5" -> {
                                    try {
                                        System.out.println("Editing schedule 5");
                                        tempSchedule = currentUser.getSchedules().get(4);
                                        currentUser.removeSchedule(4);
                                        //open schedule menu
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
            System.out.println("""
                Type 'dept' to filter by department
                Type 'time' to filter by time
                Type 'days' to filter by days
                Type 'lvl' to filter by level
                Type 'ra' to filter by remove all filters
                Type 'b' to go back
                Type 'exit' to close the program
                """);
            filterType = input.nextLine().toLowerCase();
            switch (filterType) {
                case "dept" -> filterDept();
                case "time" -> filterTime();
                case "days" -> filterDays();
                case "lvl" -> filterLevel();
                case "ra" -> searchBox.removeAllFilters(); //removes all filters
                case "b" -> isFiltering = false;
                case "exit" -> endSession();
                default -> invalidArgument();
            }
        }
    }

    private static void filterDept() {
        HashSet<String> departments = new HashSet<>();
        boolean isFiltering = true;
        String command;

        for (Course c: searchBox.getFilteredCourses())
            departments.add(c.getDepartmentInfo().department()); //TODO: TEST, IT SHOULD WORK THO

        while (isFiltering) {
            System.out.println("""
                Type 's' to see departments that can be filtered
                Type <department> to filter by that department
                Type 'b' to go back
                Type 'exit' to terminate the program
                """);
            command = input.nextLine().toLowerCase();
            switch (command) {
                case "s" -> {
                    System.out.println("Here is a list of all departments: ");
                    for (String s: departments) System.out.println(s);
                }
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
        searchBox.removeSpecificFilter("search");
        System.out.println("Please enter a search phrase:");
        String searchPhrase = input.nextLine();

        searchBox.filterByPhrase(searchPhrase.toUpperCase());
        System.out.println("Here is a list of all courses that contain '" + searchPhrase + "':");
        if (searchBox.getFilteredCourses().size() > 0) {
            for (Course c: searchBox.getFilteredCourses())
                System.out.println("\t" + c.getName() + " " + c.getDepartmentInfo().courseLevel() + " " + c.getDepartmentInfo().section());
        }
        else System.out.println("No courses found!");
        System.out.println();
    }

    public static void importCoursesFromCSV() throws IOException {
        String csvFile = "src/main/java/edu/gcc/comp350/team4project/edited_data_2.csv";
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

    private static void logInUser() {
        while(true) {
            input = new Scanner(System.in);
            System.out.println("Enter username:");
            String name = input.nextLine().toLowerCase();
            System.out.println("Enter password");
            String password = input.nextLine().toLowerCase();
            if (DatabaseController.authenticateUser(name,password)) {
                System.out.println("Log in successful!");
                currentUser = DatabaseController.pullUser(name);
                break;
            }
            System.out.println("Log in details do not match any stored users");
        }
    }
    private static void logOutUser() {
        System.out.println("Logging out!");
        DatabaseController.updateUser(currentUser);
        currentUser = null;
    }

    private static void createNewUser() {
        input = new Scanner(System.in);
        System.out.println("Creating new user!");
        String name = "";
        String year = "";
        String password = "";


        while (true) {
            System.out.println("Enter  username (less than 20 characters: ");
            name = input.nextLine().toLowerCase();
            if (name.length() > 20 || name.length() == 0) {
                System.out.println("Username too long!");
            }
            else if (DatabaseController.checkIfUserInDB(name)) {//
                System.out.println("Username already exists. Try again");
            }
            else {
                break;
            }
        }
        do {
            System.out.println("Enter  password (less than 20 characters: ");
            password = input.nextLine().toLowerCase();
        } while (password.length() > 20 || password.length() == 0);


        while (true) {
            System.out.println("Enter class year you are scheduling for:");
            System.out.println("""
                    'f' for freshman
                    's' for sophomore
                    'j' for junior
                    'e' for senior
                    """);
            String in = input.nextLine().toLowerCase();
            if (in.equals("f")) {
                year = "freshman";
                break;
            }
            if (in.equals("s")) {
                year = "sophomore";
                break;
            }
            if (in.equals("j")) {
                year = "junior";
                break;
            }
            if (in.equals("e")) {
                year = "senior";
                break;
            }
        }
        currentUser = new User(name, year, password, false);
        DatabaseController.insert(currentUser);
        System.out.println("Account info: \nUsername: " + name + "\nPassowrd: " + password + "\nYear: " + year + "\n");
    }

    private static void endSession() {
        System.out.println("terminating program");
        System.exit(0);
    }


    private static void createNewSchedule() { System.out.println("new schedule created"); }

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

    private static boolean scheduleMenu() {
        System.out.println("""
                            Type 'f' to apply or remove a filter
                            Type 's' to search for a class by a phrase
                            Type 'r' to remove a course
                            Type 'vc' to see your schedule as a calendar
                            Type 'vt' to see your schedule as a table
                            Type 'sb' to save and go back
                            Type 'exit' to terminate the program
                            """);


        String command = input.nextLine().toLowerCase();
        switch (command) {
            case "f" -> filter(); //TODO: make filter method in this class
            case "s" -> search(); //TODO: test, should work
            case "r" -> System.out.println("remove class not implemented"); //TODO: remove a class
            case "vc" -> {
                System.out.println(tempSchedule.toCalenderView());
            }
            case "vt" -> {
                System.out.println(tempSchedule);
            }
            case "sb" -> {
                try {
                    currentUser.saveScheduleToUser(tempSchedule);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
                return false;
            } //TODO: implement save and exit
            case "exit" -> endSession();
            default -> invalidArgument();
        }


        return true;
    }



    private static void invalidArgument() { System.out.println("Invalid argument!"); }


}