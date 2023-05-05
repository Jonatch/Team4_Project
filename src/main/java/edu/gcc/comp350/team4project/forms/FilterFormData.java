package edu.gcc.comp350.team4project.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class FilterFormData {
    private String professor;
    private String department;
    private String startTime;
    private String endTime;
    private String days;
    private String credits;
    private String level;

    public FilterFormData(){
        this.professor = "";
        this.department = "";
        this.startTime = "";
        this.endTime = "";
        this.days = "";
        this.credits = "";
        this.level = "";
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
