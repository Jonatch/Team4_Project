package edu.gcc.comp350.team4project;

import java.util.ArrayList;

public class Schedule {
    private String scheduleName;
    private enum semester{Fall, Spring};
    private ArrayList<Course> courses;
    private int totalCredits;

    public Schedule() {
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }


    public void addCourse(Course course){
    }

    public void addCourses(ArrayList<Course> courses){
    }



    public void removeCourse(Course course){
    }

    public void removeCourses(ArrayList<Course> courses){
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    private boolean checkConflict(Course course){
        return true;
    }

    public String toCalenderView(){
        return "String";
    }

    public String toTableView(){
        return "String";
    }

}
