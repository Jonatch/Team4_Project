package edu.gcc.comp350.team4project;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class Schedule {
    private String scheduleName;
    private Semester semester;
    private ArrayList<Course> courses;
    private int totalCredits;

    private static final int ROWS = 32;
    private static final int COLS = 6;
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static final String[] TIMES = {"8:00", "8:15", "8:30", "8:45", "9:00", "9:15", "9:30",
            "9:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00",
            "12:15", "12:30", "12:45", "1:00", "1:15", "1:30", "1:45", "2:00", "2:15", "2:30", "2:45", "3:00",
            "3:15", "3:30", "3:45"};

    private static final String[][] schedule = new String[ROWS][COLS];

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
            sb.append("Schedule Name: " + this.scheduleName);
            sb.append("\n"+ "Semester: " + semester);
            sb.append("\n"+ "Courses: ");

        for(Course course : this.courses){
            sb.append("\n" + course.toString());
        }
        return sb.toString();
    }
    public void toCalenderView() {
        //set the top row to the days of the week
        for (int col = 1; col < COLS; col++) {
            schedule[0][col] = DAYS[col-1];
        }

        //set the left column to the time slots
        for (int row = 1; row < ROWS; row++) {
            schedule[row][0] = TIMES[row-1];
        }
        // Fill in the rest of the schedule with empty cells
        for (int row = 1; row < ROWS; row++) {
            for (int col = 1; col < COLS; col++) {
                schedule[row][col] = "";
            }
        }
    }


    public void toTableView() {
        System.out.println(this);
    }

}

