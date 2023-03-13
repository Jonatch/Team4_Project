package edu.gcc.comp350.team4project;

import java.util.ArrayList;

public class Schedule {
    private String scheduleName;
    private Semester semester;
    private ArrayList<Course> courses;
    private int totalCredits;

    public Schedule() {
        //probably want two constructors:
        // one that creates empty courses
        // and one that takes an ArrayList of courses
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
    public void addCourse(Course course)throws Exception{
    }
    public void addCourses(ArrayList<Course> courses)throws Exception{
    }
    public void removeCourse(Course course)throws Exception{
    }
    public void removeCourses(ArrayList<Course> courses)throws Exception{
    }

    public int getTotalCredits() {
        return totalCredits;
    }
    public String toCalenderView(){
        return "String";
    }

    public String toTableView(){
        return "String";
    }

}
