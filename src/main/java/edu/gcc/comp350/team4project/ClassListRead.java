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

    public ArrayList<String> otherCourseTypes;

//    public static void main(String[] args) {
//        ClassListRead c = new ClassListRead();
//        c.ReadTextFile("Accounting");
//    }

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
                    courseSection = departmentInfo[3].charAt(0);
                } catch (Exception ignored) {
                }

                courseName = data[3];

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

        initializeCSVCourses();
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
    public static void initializeCSVCourses() {
        totalCourses = new ArrayList<>();
        String longCSV = "updated_courses_2324.csv"; // pulls from csv of all courses
        importCoursesFromCSV(longCSV); // imports information as data we can use
    }


    public void ClassesSuggest(Semester semester) {
        otherCourseTypes = new ArrayList<>();
        credits = 0;
        WebController w = new WebController();
        classesToDo = w.getArrayList();
        for (int i = 0; i < classesToDo.size(); i++) {
            String currClass = classesToDo.get(i);
            if (Pattern.matches("[A-Z]{4}\s\\d{3}.*", currClass)) {
                if (sameSemester(currClass, semester)) {
                    int courseCredits = getCredits(currClass, semester);
                    if (credits + courseCredits <= 17) {
                        otherCourseTypes.add(currClass);
                        credits += courseCredits;
                    }
                }
            }
            else if (Character.isDigit(currClass.charAt(0)) && Character.isDigit(currClass.charAt(1))) {
                int genCredits;
                String numCredits = currClass.substring(0,2);
                genCredits = Integer.parseInt(numCredits);
                if (credits + genCredits <= 17) {
                    credits += genCredits;
                    otherCourseTypes.add(currClass);
                }
                else if (credits <= 16){
                    credits = 17;
                    otherCourseTypes.add((17-credits) + " Credit(s) of General Electives");
                }
            }
            else if (Character.isDigit(currClass.charAt(0))) {
                int genCredits;
                String numCredits = currClass.substring(0,1);
                genCredits = Integer.parseInt(numCredits);
                if (credits + genCredits <= 17) {
                    credits += genCredits;
                    otherCourseTypes.add(currClass);
                }
                else if (credits <= 16){
                    otherCourseTypes.add((17-credits) + " Credit(s) of General Electives");
                    credits = 17;
                }
            }
            else if (currClass.equals("Music Method Block Elective")) {
                if (credits <= 16) {
                    credits += 1;
                    otherCourseTypes.add(currClass);
                }
            }
            else if (currClass.equals("Natural Science with Lab")) {
                if (credits <= 13) {
                    credits += 4;
                    otherCourseTypes.add(currClass);
                }
            }
            else if (currClass.equals("SSFT")) {
                if (credits <= 15) {
                    credits += 2;
                    otherCourseTypes.add(currClass);
                }
            }
            else if (currClass.equals("Applied Music")) {
                if (credits <= 15) {
                    credits += 2;
                    otherCourseTypes.add(currClass);
                }
            }
            else if (currClass.equals("Ensemble")) {
                if (credits <= 16) {
                    credits += 1;
                    otherCourseTypes.add(currClass);
                }
            }
            else if (currClass.equals("Music Elective")) {
                if (credits <= 15) {
                    credits += 2;
                    otherCourseTypes.add(currClass);
                }
            }
            else {
                if (credits <= 14) {
                    credits += 3;
                    otherCourseTypes.add(currClass);
                }
            }
        }

        addCourses(otherCourseTypes, semester);
    }

    public boolean sameSemester(String s, Semester semester) {
        String longCSV = "updated_courses_2324.csv"; //pulls from csv of all courses
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
        String longCSV = "updated_courses_2324.csv"; //pulls from csv of all courses
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

    public void addCourses(ArrayList<String> coursesToAdd, Semester semester)  {
        ArrayList<Course> classesToAdd = new ArrayList<>();
        initializeCSVCourses();
        SearchController searcher = new SearchController(totalCourses, semester);
        WebController w = new WebController();
        for (int i = 0; i < coursesToAdd.size(); i++) {
            searcher.refreshFilteredCourses();
            String courseToAdd = coursesToAdd.get(i);
            System.out.println(courseToAdd);
            if (Pattern.matches("[A-Z]{4}\s\\d{3}.*", courseToAdd)) {
                String dept = classes.get(i).substring(0, 4);
                searcher.filterByDept(dept);
                String courseNum = classes.get(i).substring(5, 8);
                searcher.filterByExactLevel(courseNum);
                if (!searcher.getFilteredCourses().isEmpty()) {
                    classesToAdd.add(searcher.getFilteredCourses().get(0));
                }
            }
        }

        for (int i = 0; i < classesToAdd.size(); i++) {
            if (w.addEvent(classesToAdd.get(i))) {
            }
            else {
                w.addConflictingEvent();
            }
        }
    }
}
