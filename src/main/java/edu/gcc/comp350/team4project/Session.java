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


    public static void main(String[] args) throws IOException {
        createSession();
        //TODO: after, figure out where to close Scanner
    }

    public static void createSession() throws IOException {
        totalCourses = new ArrayList<>();
        importCoursesFromCSV();
        searchBox = new Search(totalCourses);
        menuLoop();
    }

    private static void menuLoop() {
        input = new Scanner(System.in);
        boolean isLoggedIn, isScheduling;
        mainMenu();
        String command = input.nextLine().toLowerCase();
        while (!command.equals("exit")) {
            if (command.equals("u") || command.equals("g")) {
                if (command.equals("u")) logInUser();
                else System.out.println("logged in as a guest");
                isLoggedIn = true;
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
                                System.out.println("Creating schedule " + tempSchedule.getScheduleName() + " (" + tempSchedule.getSemester() + "semester)");

                            }
                            else{
                                System.out.println("Maximum of " + currentUser.getNumMaxSchedules() + " allowed. Try deleting one");
                            }

                        }
                        case "ds" -> {
                            if(currentUser.getSchedules().size()>0){
                                System.out.println("Pick a schedule to delete: ");
                                for(int i = 0; i<currentUser.getSchedules().size();i++){
                                    System.out.println("" + (i+1) + ": " + currentUser.getSchedules().get(i));
                                }
                            }
                            else{
                                System.out.println("No saved schedules to delete!");
                            }
                        }
                        case "b" -> {
                            isLoggedIn = false;
                            logOutUser();
                        } //TODO: logout from account
                        case "exit" -> {
                            isLoggedIn = false;
                            endSession();
                        } //exit the program
                        case "ss" -> {//TODO: test, selecting schedule
                            isScheduling = true;
                            selectSchedule(); //TODO: needs to be implemented
                            scheduleMenu();
                            command = input.nextLine().toLowerCase();
                            while (isScheduling) {
                                switch (command) {
                                    case "f" -> filter(); //TODO: make filter method in this class
                                    case "s" -> search(); //TODO: test, should work
                                    case "r" -> System.out.println("remove class not implemented"); //TODO: remove a class
                                    case "v" -> System.out.println("view schedule not implemented"); //TODO: view schedule
                                    case "se" -> {
                                        isScheduling = false;
                                        System.out.println("save and exit not implemented");
                                        endSession();
                                    } //TODO: implement save and exit
                                    case "b" -> {
                                        isScheduling = false;
                                        continue;
                                    }
                                    case "exit" -> endSession();
                                    default -> invalidArgument();
                                }
                                scheduleMenu();
                                command = input.nextLine().toLowerCase();
                            }
                        }
                        default -> invalidArgument();
                    }
                }
            }
            else if (command.equals("n")) createNewUser(); //make new account
            else invalidArgument(); //invalid argument
            mainMenu();
            command = input.nextLine().toLowerCase();
        }
        endSession();
    }

    private static void filter() {
        input = new Scanner(System.in);
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
                case "dept" -> filterDept(); //TODO: filter by department
                case "time" -> filterTime(); //TODO: filter by time
                case "days" -> filterDays(); //TODO: filter by days
                case "lvl" -> filterLevel(); //TODO: filter by level
                case "ra" -> searchBox.removeAllFilters(); //removes all filters
                case "b" -> isFiltering = false; //TODO: go back
                case "exit" -> endSession();
                default -> invalidArgument();
            }
        }
    }

    private static void filterDept() {
        HashSet<String> departments = new HashSet<>();
        boolean isFiltering = true;
        input = new Scanner(System.in);
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
        //TODO: figure this out
    }

    private static void filterLevel() { //TODO: needs testing
        input = new Scanner(System.in);
        char level;
        boolean isFiltering = true;

        while (isFiltering) {
            System.out.println("""
                    Please enter the level you would like to filter by:
                    1 - 100 level classes
                    2 - 200 level classes
                    3 - 300 level classes
                    4 - 400 level classes
                    Type 'b' to go back
                    Type 'exit' to terminate the program
                    """);
            level = input.nextLine().charAt(0);
            switch (level) {
                case '1' -> searchBox.filterByLevel(100);
                case '2' -> searchBox.filterByLevel(200);
                case '3' -> searchBox.filterByLevel(300);
                case '4' -> searchBox.filterByLevel(400);
                case 'b' -> isFiltering = false;
                case 'e' -> endSession();
                default -> invalidArgument();
            }
        }
    }

    private static void search() {
        input = new Scanner(System.in);
        System.out.println("Please enter a search phrase:");
        String searchPhrase = input.nextLine();

        searchBox.filterByPhrase(searchPhrase.toUpperCase());
        System.out.println("Here is a list of all courses that contain '" + searchPhrase + "':");
        if (searchBox.getFilteredCourses().size() > 0) {
            for (Course c: searchBox.getFilteredCourses())
                System.out.println("\t" + c.getName());
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
        System.out.println("logging in");
    }

    private static void endSession() {
        System.out.println("terminating program");
        System.exit(0);
    }

    private static void logOutUser() { System.out.println("logging out"); }

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
                        Type 'ss' to select a schedule to use
                        Type 'b' to go logout
                        Type 'exit' to terminate the program
                        """);
    }

    private static void scheduleMenu() {
        System.out.println("""
                            Type 'f' to apply or remove a filter
                            Type 's' to search for a class by a phrase
                            Type 'c' to view credit count
                            Type 'r' to remove a course
                            Type 'v' to see your schedule
                            Type 'b' to go back
                            Type 'se' to save and exit
                            Type 'exit' to terminate the program
                            """);
    }

    private static void invalidArgument() { System.out.println("Invalid argument!"); }

    private static void selectSchedule() { System.out.println("selecting a schedule"); }

    private static void createNewUser() { System.out.println("creating new user"); }

}