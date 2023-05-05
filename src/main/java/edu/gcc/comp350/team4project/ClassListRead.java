package edu.gcc.comp350.team4project;

import java.io.*;
import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClassListRead {
    public ArrayList<String> classes;
    public static ArrayList<Course> totalCourses;
    public static ArrayList<String> classesToDo;
    int credits;

//    public static void main(String[] args) {
//        ClassListRead c = new ClassListRead();
//        c.ReadTextFile("Accounting");
//    }

    public static void importCoursesFromCSV(String ext) {//handles importing a course from csv. Takes all csv values and converts to data types. Only takes in good data
        totalCourses = new ArrayList<>();
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


    public void ReadTextFile(String major) {
        classes = new ArrayList<String>();
        File file = new File("GCC Major Reqs/" + major + ".txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String numCredits = scanner.nextLine();
                classes.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String longCSV = "large_courses.csv"; //pulls from csv of all courses
        importCoursesFromCSV(longCSV); //imports information as data we can use
        SearchController sFall = new SearchController(totalCourses, Semester.FALL);
        SearchController sSpring = new SearchController(totalCourses, Semester.SPRING);
        for (int i = 0; i < classes.size(); i++) {
            sFall.refreshFilteredCourses();
            if (Pattern.matches("[A-Z]{4}\s\\d{3}", classes.get(i))) {
                String dept = classes.get(i).substring(0, 4);
                sFall.filterByDept(dept);
                String courseNum = classes.get(i).substring(5, 8);
                sFall.filterByExactLevel(courseNum);
                if (!sFall.getFilteredCourses().isEmpty()) {
                    String courseName = sFall.getFilteredCourses().get(0).getName();
                    classes.set(i, classes.get(i) + " " + courseName);
                }
            }
        }
        for (int i = 0; i < classes.size(); i++) {
            sSpring.refreshFilteredCourses();
            if (Pattern.matches("[A-Z]{4}\s\\d{3}", classes.get(i))) {
                String dept = classes.get(i).substring(0, 4);
                sSpring.filterByDept(dept);
                String courseNum = classes.get(i).substring(5, 8);
                sSpring.filterByExactLevel(courseNum);
                if (!sSpring.getFilteredCourses().isEmpty()) {
                    String courseName = sSpring.getFilteredCourses().get(0).getName();
                    classes.set(i, classes.get(i) + " " + courseName);
                }
            }
        }
    }

    public void ClassesSuggest(Semester semester) {
        credits = 0;
        WebController w = new WebController();
        //classesToDo = w.getArrayList();
        while (credits <= 16) {
            for (int i = 0; i < classesToDo.size(); i++) {
                System.out.println(classesToDo.get(i));
                if (Pattern.matches("[A-Z]{4}\s\\d{3}", classesToDo.get(i))) {
                    if (sameSemester(classesToDo.get(i), semester)) {
                        getCredits(classesToDo.get(i), semester);
                    }
                }
            }
        }
    }

    public boolean sameSemester(String s, Semester semester) {
        String longCSV = "large_courses.csv"; //pulls from csv of all courses
        importCoursesFromCSV(longCSV); //imports information as data we can use
        if (semester == Semester.FALL) {
            SearchController sFall = new SearchController(totalCourses, Semester.FALL);
            String dept = s.substring(0,4);
            String courseNum = s.substring(5,8);
            sFall.filterByDept(dept);
            sFall.filterByExactLevel(courseNum);
            if (!sFall.getFilteredCourses().isEmpty()) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            SearchController sSpring = new SearchController(totalCourses, Semester.SPRING);
            String dept = s.substring(0, 4);
            String courseNum = s.substring(5, 8);
            sSpring.filterByDept(dept);
            sSpring.filterByExactLevel(courseNum);
            if (!sSpring.getFilteredCourses().isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
    }
    public int getCredits(String s, Semester semester) {
        String longCSV = "large_courses.csv"; //pulls from csv of all courses
        importCoursesFromCSV(longCSV); //imports information as data we can use

        if (semester == Semester.FALL) {
            SearchController sFall = new SearchController(totalCourses, Semester.FALL);
            String dept = s.substring(0,4);
            String courseNum = s.substring(5,8);
            sFall.filterByDept(dept);
            sFall.filterByExactLevel(courseNum);
            return sFall.getFilteredCourses().get(0).getCredits();
        }

        SearchController sSpring = new SearchController(totalCourses, Semester.SPRING);
        String dept = s.substring(0,4);
        String courseNum = s.substring(5,8);
        sSpring.filterByDept(dept);
        sSpring.filterByExactLevel(courseNum);
        return sSpring.getFilteredCourses().get(0).getCredits();
    }
}
