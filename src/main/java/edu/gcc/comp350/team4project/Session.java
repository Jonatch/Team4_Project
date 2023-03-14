package edu.gcc.comp350.team4project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Session {
    private static User currentUser;
    //private Search searchBox;
    private static Schedule tempSchedule;
    private static ArrayList<Course> totalCourses;

    public static void main(String[] args) throws IOException {
        createSession();
        for(Course course : totalCourses){
            System.out.println(course);
        }
    }

    public static void createSession() throws IOException {
        totalCourses = new ArrayList<>();
        importCoursesFromCSV();
    }
    public static void importCoursesFromCSV() throws IOException {
        String csvFile = "C:\\Users\\WEIRJR20\\OneDrive - Grove City College\\Spring 2023\\Software Engineering\\Team4SharedFolder\\edited_data.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
               // System.out.println(Arrays.toString(data));
                if(firstLine){
                    firstLine = false;
                    continue;
                }
                String name;
                String department;
                String courseLevel;
                char section = ' ';
                int refNum=0;
                int credits=0;
                String professor;
                Semester semester = null;
                String description = "";
                ArrayList<DayOfWeek> days = new ArrayList<>();
                LocalTime startTime = null;
                LocalTime endTime = null;

                try {
                    if (Integer.parseInt(data[0]) == 10) {
                        semester = Semester.FALL;
                    } else if (Integer.parseInt(data[0]) == 30) {
                        semester = Semester.SPRING;
                    }
                }
                catch(Exception ignore){}

                department = data[1];
                courseLevel = data[2];
                try {
                    section = data[3].charAt(0);
                }catch(Exception ignored){}
                name = data[4];
                try {
                    credits = Integer.parseInt(data[5]);
                }catch(Exception ignored){}


                if(data[6].equals("M")){
                    days.add(DayOfWeek.MONDAY);
                }
                if(data[7].equals("T")){
                    days.add(DayOfWeek.TUESDAY);
                }
                if(data[8].equals("W")){
                    days.add(DayOfWeek.WEDNESDAY);
                }
                if(data[9].equals("R")){
                    days.add(DayOfWeek.THURSDAY);
                }
                if(data[10].equals("F")){
                    days.add(DayOfWeek.FRIDAY);
                }

                try{
                    String[] tempTime = data[11].split(":");
                    int hour = 0;
                    int min = 0;
                    if (tempTime[2].charAt(3)== 'A') {
                        hour = Integer.parseInt(tempTime[0]);
                    } else if (tempTime[2].charAt(3) == 'P') {
                        hour = Integer.parseInt(tempTime[0]) + 12;
                    }
                    min = Integer.parseInt(tempTime[1]);
                    startTime = LocalTime.of(hour, min);
                }catch(Exception ignored){}

                try{
                    String[] tempTime = data[12].split(":");
                    int hour = 0;
                    int min = 0;


                    if (tempTime[2].charAt(3) == 'A') {
                        hour = Integer.parseInt(tempTime[0]);
                    } else if (tempTime[2].charAt(3) == 'P') {
                        hour = Integer.parseInt(tempTime[0]) + 12;
                    }
                    min = Integer.parseInt(tempTime[1]);
                    endTime = LocalTime.of(hour, min);
                }catch(Exception ignore){}

                professor = data[13];
                try {
                    refNum = Integer.parseInt(data[14]);
                }catch(Exception ignore){}
                for(int i = 15;i<data.length;i++){
                    description = description + data[i]+" ";
                }

                totalCourses.add(new Course(refNum,department,semester,courseLevel,section,name,credits,professor,description,days,startTime,endTime));
            }
        } catch (IOException ignored) {}
    }


    public void logInUser(){

    }

    public void endSession(){

    }

    public void logOutUser(){}

    public void menuLoop(){}

    public void createNewUser(){}



}
