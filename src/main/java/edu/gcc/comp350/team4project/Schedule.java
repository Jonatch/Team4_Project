package edu.gcc.comp350.team4project;

import java.sql.Array;
import java.sql.SQLOutput;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Schedule {
    private String scheduleName;
    private Semester semester;
    private ArrayList<Course> courses;
    private int totalCredits;



    public Schedule(String scheduleName, Semester semester) {
        this.scheduleName = scheduleName;
        this.semester = semester;
        this.totalCredits = 0;
        this.courses = new ArrayList<>();
    }

    public Schedule(String scheduleName, Semester semester, ArrayList<Course> courses) throws Exception {
        this.scheduleName = scheduleName;
        this.semester = semester;
        this.totalCredits = 0;
        this.courses = new ArrayList<>();

        this.addCourses(courses);
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }
    public Semester getSemester() {
        return semester;
    }
    public void setSemester(Semester semester) {
        this.semester = semester;
    }
    public ArrayList<Course> getCourses() {
        return courses;
    }
    public void addCourse(Course newCourse) throws Exception{ //not tested
        for(Course course : courses){
            if(newCourse.getRefNum()==course.getRefNum()){
                throw new Exception("Cannot add the same course to a schedule twice");
            }
            else if(newCourse.doesCourseConflict(course)){
                throw new Exception("New course " + newCourse.getName() + " conflicts with already scheduled course " + course.getName());
            }
        }
        if(newCourse.getSemester().equals(this.semester)){
            this.courses.add(newCourse);
            this.totalCredits+=newCourse.getCredits();
        }else{
            throw new Exception("Cannot add a " + newCourse.getSemester() + " course to a " + this.semester + " schedule");
        }

    }

    public void addCourses(ArrayList<Course> courses) throws Exception {//not tested
        for(Course course : courses){
            try{
                this.addCourse(course);
            }catch(Exception e){
                continue;
            };
        }
    }



    public void removeCourse(Course course) throws Exception{ //not tested
        if(!(this.courses.remove(course))){
            throw new Exception("Course " + course.getName() + " was not found in the schedule and was not removed");
        }
        else{
            this.totalCredits -= course.getCredits();
        }
    }

    public void removeCourses(ArrayList<Course> courses) throws Exception{//not tested
        for(Course course : courses){
            this.removeCourse(course);
        }
    }

    public void removeAllCourses(){//not tested
        this.courses = new ArrayList<>();
    }

    public int getTotalCredits() {
        return totalCredits;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("SCHEDULE_NAME: " + this.scheduleName + " ");
        sb.append("SEMESTER: " + this.semester.toString().toLowerCase()+ " ");
        sb.append("CREDITS: " + this.totalCredits);
        return sb.toString();
    }
    public String toCalenderView() {

        final int ROWS = 53;
        final int COLS = 6;
        final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}; // Declare a constant array of days of the week
        final ArrayList<LocalTime> TIMES = new ArrayList<>(); // Declare an array list to store time slots for the calendar
        LocalTime l = LocalTime.of(8, 00);
        for (int i = 0; i < 53; i++) { // Populate the TIMES array list with time slots
            TIMES.add(l);
            l = l.plusMinutes(15);
        }

        final ArrayList<LocalTime> smallTIMES = new ArrayList<>(); // Declare an array list to store smaller time slots for each course
        LocalTime l2 = LocalTime.of(8, 00);
        for (int i = 0; i < 159; i++) { // Populate the smallTIMES array list with smaller time slots
            smallTIMES.add(l2);
            l2 = l2.plusMinutes(5);
        }

        final String[][] schedule = new String[ROWS][COLS]; // Declare a 2D array to represent the schedule

        //set the top row to the days of the week
        for (int col = 1; col < COLS; col++) { // Set the top row of the schedule with the days of the week
            schedule[0][col] = DAYS[col-1];
        }

        //set the left column to the time slots
        for (int row = 1; row < ROWS; row++) { // Set the left column of the schedule with the time slots
            String s = TIMES.get(row-1).toString();
            schedule[row][0] = s;
        }

        for (int row = 1; row < ROWS; row++) {
            for (int col = 1; col < COLS; col++) {
                schedule[row][col] = "\t\t"; // Initialize all other cells of the schedule with empty strings
            }
        }

        schedule[0][0] = "\t\t"; // Initialize the top left cell of the schedule with an empty string


// Loop through each course in the list of courses
        for (Course course : courses){

            // Get the start and end times of the course, and the days it occurs
            LocalTime sTime = course.getTimeInfo().startTime();
            LocalTime eTime = course.getTimeInfo().endTime();
            String days = course.getDays().toString();

            // If the course occurs on Monday
            if (days.contains("MONDAY")) {

                // Initialize a timeslot counter
                int timeslot = 0;

                // Loop through each small time slot
                for (LocalTime t : smallTIMES) {

                    // If the start time of the course matches the current small time slot
                    if (sTime.equals(t)) {

                        // Assign the course information to the corresponding slot in the schedule array
                        schedule[(timeslot/3)+1][1] = course.getDepartmentInfo().department() +
                                course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();

                        // Move to the next small time slot and increment the timeslot counter
                        t = t.plusMinutes(5);
                        timeslot++;

                        // Loop through the remaining small time slots until the end time of the course is reached
                        while (eTime.isAfter(t)) {
                            schedule[(timeslot/3)+1][1] = course.getDepartmentInfo().department() +
                                    course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                            t = t.plusMinutes(5);
                            timeslot++;
                        }
                    }
                    timeslot++;
                }
            }

            // Repeat the above process for each day of the week that the course occurs on
            if (days.contains("TUESDAY")) {
                int timeslot = 0;
                for (LocalTime t : smallTIMES) {
                    if (sTime.equals(t)) {
                        schedule[(timeslot/3)+1][2] = course.getDepartmentInfo().department() +
                                course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                        t = t.plusMinutes(5);
                        timeslot++;
                        while (eTime.isAfter(t)) {
                            schedule[(timeslot/3)+1][2] = course.getDepartmentInfo().department() +
                                    course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                            t = t.plusMinutes(5);
                            timeslot++;
                        }
                    }
                    timeslot++;
                }
            }
            if (days.contains("WEDNESDAY")) {
                int timeslot = 0;
                for (LocalTime t : smallTIMES) {
                    if (sTime.equals(t)) {
                        schedule[(timeslot/3)+1][3] = course.getDepartmentInfo().department() +
                                course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                        t = t.plusMinutes(5);
                        timeslot++;
                        while (eTime.isAfter(t)) {
                            schedule[(timeslot/3)+1][3] = course.getDepartmentInfo().department() +
                                    course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                            t = t.plusMinutes(5);
                            timeslot++;
                        }
                    }
                    timeslot++;
                }
            }
            if (days.contains("THURSDAY")) {
                int timeslot = 0;
                for (LocalTime t : smallTIMES) {
                    if (sTime.equals(t)) {
                        schedule[(timeslot/3)+1][4] = course.getDepartmentInfo().department() +
                                course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                        t = t.plusMinutes(5);
                        timeslot++;
                        while (eTime.isAfter(t)) {
                            schedule[(timeslot/3)+1][4] = course.getDepartmentInfo().department() +
                                    course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                            t = t.plusMinutes(5);
                            timeslot++;
                        }
                    }
                    timeslot++;
                }
            }
            if (days.contains("FRIDAY")) {
                int timeslot = 0;
                for (LocalTime t : smallTIMES) {
                    if (sTime.equals(t)) {
                        schedule[(timeslot/3)+1][5] = course.getDepartmentInfo().department() +
                                course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                        t = t.plusMinutes(5);
                        timeslot++;
                        while (eTime.isAfter(t)) {
                            schedule[(timeslot/3)+1][5] = course.getDepartmentInfo().department() +
                                    course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                            t = t.plusMinutes(5);
                            timeslot++;
                        }
                    }
                    timeslot++;
                }
            }
        }
        // Return a string representation of the schedule array
        return this.toString() + "\n" + Arrays.deepToString(schedule).replace("], ", "]\n");    }


    public String toTableView() {
        StringBuilder sb = new StringBuilder();
        // Append schedule name
        sb.append("NAME: " + this.scheduleName + " ");
        // Append semester name in lowercase
        sb.append("SEMESTER: " + this.semester.toString().toLowerCase() + " ");
        // Append total credits
        sb.append("CREDITS: " + this.totalCredits + "\n");
        // Append courses header
        sb.append("     COURSES: " + "\n");
        // Append each course in the list of courses
        for (Course course : this.courses) {
            sb.append("         --" + course.toString() + "\n");
        }
        // Convert StringBuilder to String and return
        return sb.toString();
    }
}

