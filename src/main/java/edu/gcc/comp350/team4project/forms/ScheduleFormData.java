package edu.gcc.comp350.team4project.forms;

import java.util.ArrayList;

public class ScheduleFormData {
    private String name;
    private String semester;
    private String major;
    private boolean suggestCourses;

    public ScheduleFormData(){
        this.name = "";
        this.semester = "";
        this.major = "";
        this.suggestCourses = false;
    }
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
    public String getSemester() {return semester;}

    public void setSemester(String semester) {this.semester = semester;}

    public String getMajor() {
        return major;
    }
    public void setMajor(String major) {
        this.major = major;
    }

    public boolean getSuggestCourses() {
        return suggestCourses;
    }

    public void setSuggestCourses(boolean suggestCourses) {
        this.suggestCourses = suggestCourses;
    }
}