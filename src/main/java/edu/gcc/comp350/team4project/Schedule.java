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
            if(newCourse.doesCourseConflict(course)){
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
            sb.append("NAME: " + this.scheduleName + " ");
            sb.append("SEMESTER: " + this.semester+ " ");
            sb.append("CREDITS: " + this.totalCredits + "\n");
            sb.append("     COURSES: "+ "\n");
        for(Course course : this.courses){
            sb.append("         --"+ course.toString()+"\n");
        }
        return sb.toString();
    }
    public String toCalenderView() {
        final int ROWS = 53;
        final int COLS = 6;
        final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        final ArrayList<LocalTime> TIMES = new ArrayList<>();
        LocalTime l = LocalTime.of(8, 00);
        for (int i = 0; i < 53; i++) {
            TIMES.add(l);
            l = l.plusMinutes(15);
        }
        final String[][] schedule = new String[ROWS][COLS];

        //set the top row to the days of the week
        for (int col = 1; col < COLS; col++) {
            schedule[0][col] = DAYS[col-1];
        }

        //set the left column to the time slots
        for (int row = 1; row < ROWS; row++) {
            String s = TIMES.get(row-1).toString();
            schedule[row][0] = s;
        }

        for (int row = 1; row < ROWS; row++) {
            for (int col = 1; col < COLS; col++) {
                schedule[row][col] = "OPEN";
            }
        }

        for (Course course : courses){
            LocalTime sTime = course.getTimeInfo().startTime();
            LocalTime eTime = course.getTimeInfo().endTime();
            String days = course.getDays().toString();
            if (days.contains("MONDAY")) {
                int timeslot = 0;
                for (LocalTime t : TIMES) {
                    if (sTime.equals(t)) {
                        schedule[timeslot+1][1] = course.getDepartmentInfo().department() +
                    course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                        t = t.plusMinutes(15);
                        timeslot++;
                        while (eTime.isAfter(t)) {
                            schedule[timeslot+1][1] = course.getDepartmentInfo().department() +
                                    course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                            t = t.plusMinutes(15);
                            timeslot++;
                        }
                    }
                    timeslot++;
                }
            }
            if (days.contains("TUESDAY")) {
                int timeslot = 0;
                for (LocalTime t : TIMES) {
                    if (sTime.equals(t)) {
                        schedule[timeslot+1][2] = course.getDepartmentInfo().department() +
                                course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                        t = t.plusMinutes(15);
                        timeslot++;
                        while (eTime.isAfter(t)) {
                            schedule[timeslot+1][2] = course.getDepartmentInfo().department() +
                                    course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                            t = t.plusMinutes(15);
                            timeslot++;
                        }
                    }
                    timeslot++;
                }
            }
            if (days.contains("WEDNESDAY")) {
                int timeslot = 0;
                for (LocalTime t : TIMES) {
                    if (sTime.equals(t)) {
                        schedule[timeslot+1][3] = course.getDepartmentInfo().department() +
                                course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                        t = t.plusMinutes(15);
                        timeslot++;
                        while (eTime.isAfter(t)) {
                            schedule[timeslot+1][3] = course.getDepartmentInfo().department() +
                                    course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                            t = t.plusMinutes(15);
                            timeslot++;
                        }
                    }
                    timeslot++;
                }
            }
            if (days.contains("THURSDAY")) {
                int timeslot = 0;
                for (LocalTime t : TIMES) {
                    if (sTime.equals(t)) {
                        schedule[timeslot+1][4] = course.getDepartmentInfo().department() +
                                course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                        t = t.plusMinutes(15);
                        timeslot++;
                        while (eTime.isAfter(t)) {
                            schedule[timeslot+1][4] = course.getDepartmentInfo().department() +
                                    course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                            t = t.plusMinutes(15);
                            timeslot++;
                        }
                    }
                    timeslot++;
                }
            }
            if (days.contains("FRIDAY")) {
                int timeslot = 0;
                for (LocalTime t : TIMES) {
                    if (sTime.equals(t)) {
                        schedule[timeslot+1][5] = course.getDepartmentInfo().department() +
                                course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                        t = t.plusMinutes(15);
                        timeslot++;
                        while (eTime.isAfter(t)) {
                            schedule[timeslot+1][5] = course.getDepartmentInfo().department() +
                                    course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                            t = t.plusMinutes(15);
                            timeslot++;
                        }
                    }
                    timeslot++;
                }
            }
        }
        //System.out.println(Arrays.deepToString(schedule).replace("], ", "]\n"));        // Fill in the rest of the schedule with empty cells

        return Arrays.deepToString(schedule).replace("], ", "]\n");
    }


    public void toTableView() {
    }

}

