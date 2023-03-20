package edu.gcc.comp350.team4project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
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
        mainMenu();
        String command = input.nextLine().toLowerCase();
        while (!command.equals("exit")) {
            //login
            if (command.equals("u") || command.equals("g")) {
                if (command.equals("u")) logInUser();
                else System.out.println("logged in as a guest");
                loggedInMenu();
                command = input.nextLine().toLowerCase();
                while (!command.equals("exit") || !command.equals("b")) {
                    if (command.equals("ns")) createNewSchedule();
                    else if (command.equals("ds")) {
                        //delete a schedule
                    }
                    else if (command.equals("b")) logOutUser();

                    else if (command.equals("exit")) endSession();

                    else if (command.equals("c")) {
                        selectSchedule();
                        scheduleMenu();
                        command = input.nextLine().toLowerCase();
                        while (!command.equals("se") || !command.equals("e")) {
                            if (command.equals("f")) {
                                //choose a filter
                            }
                            else if (command.equals("s")) {
                                search();
                            }
                            else if (command.equals("r")) {
                                //remove a class
                            }
                            else if (command.equals("v")) {
                                //view schedule
                            }
                            else invalidArgument();

                            scheduleMenu();
                            command = input.nextLine().toLowerCase();
                        }
                        if (command.equals("se")) {

                        }
                        else endSession();
                    }
                    else invalidArgument();
                }
            }
            //make new account
            else if (command.equals("n")) createNewUser();

            //invalid argument
            else invalidArgument();
            mainMenu();
            command = input.nextLine().toLowerCase();
        }
        endSession();
    }

    private static void createNewSchedule() {

    }

    private static void mainMenu() {
        System.out.println("""
                Type 'u' to login as a user
                Type 'g' to login as a guest
                Type 'n' to create a new account
                Type 'exit' to exit the program
                """);
    }

    private static void loggedInMenu() {
        System.out.println("""
                        Type 'ns' to create a new schedule
                        Type 'ds' to delete a schedule
                        Type 'c' to select a schedule to use
                        Type 'b' to go logout
                        Type 'exit' to exit the program
                        """);
    }
    private static void scheduleMenu() {
        System.out.println("""
                            Type 'f' to apply or remove a filter
                            Type 's' to search for a class by a phrase
                            Type 'c' to view credit count
                            Type 'r' to remove a course
                            Type 'v' to see your schedule
                            Type 'se' to save and exit
                            Type 'exit' to exit the program
                            """);
    }

    private static void invalidArgument() {
        System.out.println("Invalid argument!");
    }

    private static void selectSchedule() {
        System.out.println("selecting a schedule");
    }

    private static void search() {
        input = new Scanner(System.in);

        System.out.print("Please enter a search phrase: ");
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
        System.out.println("quitting session");
        System.exit(0);
    }

    private static void logOutUser(){
        System.out.println("logged out");
    }

    private static void createNewUser(){
        System.out.println("creating new user");
    }

}
