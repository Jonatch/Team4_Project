package edu.gcc.comp350.team4project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class Session {
    private static User currentUser;
    private static Search searchBox;
    private static Schedule tempSchedule;
    private static ArrayList<Course> totalCourses;

    public static void main(String[] args) throws IOException {
        createSession();
        searchBox = new Search(totalCourses);
        searchBox.filterByPhrase("i");

    }

    public static void createSession() throws IOException {
        totalCourses = new ArrayList<>();
        importCoursesFromCSV();
    }

    public static void importCoursesFromCSV() throws IOException {
        String csvFile = "src/main/java/edu/gcc/comp350/team4project/edited_data_2.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine();

            /*
            I'm not sure what is best practice, but the ide gets mad about us reassigning the variables multiple times
            I figure maybe one try catch within the loop to work on all assignments to variables might be good but then
            we lose some specificity in error catching I think, but I figure since we know that the csv file
            contains all the data it shouldn't be an issue, idk
             */

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

    public void logInUser(){

    }

    public void endSession(){

    }

    public void logOutUser(){}

    public void menuLoop(){}

    public void createNewUser(){}

}
