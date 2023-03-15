package edu.gcc.comp350.team4project;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

record TimeInfo(ArrayList<DayOfWeek> days, LocalTime startTime, LocalTime endTime) {}

record DepartmentInfo(String department, String courseLevel, char section){}
public class Course{
    private String name;
    private DepartmentInfo departmentInfo;
    private int refNum;
    private int credits;
    private String professor;
    private Semester semester;
    private String description;
    private TimeInfo timeInfo;

    public Course(int refNum, String department, Semester semester, String courseLevel, char section, String name, int credits, String professor, String description, ArrayList<DayOfWeek> days, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.refNum = refNum;
        this.credits = credits;
        this.professor = professor;
        this.description = description;
        this.semester = semester;
        this.timeInfo = new TimeInfo(days,startTime,endTime);
        this.departmentInfo = new DepartmentInfo(department,courseLevel,section);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRefNum() {
        return refNum;
    }

    public void setRefNum(int refNum) {
        this.refNum = refNum;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TimeInfo getTimeInfo(){
        return this.timeInfo;
    }

    public void setTimeInfo(TimeInfo timeInfo){
        this.timeInfo = timeInfo;
    }

    public ArrayList<LocalTime> getTime() {
        try {
            return new ArrayList<>(List.of(this.timeInfo.startTime(), this.timeInfo.endTime()));
        }catch(Exception e){
            return new ArrayList<>();
        }
    }
    public ArrayList<DayOfWeek> getDays() {
        return this.timeInfo.days();
    }

    public String getDepartmentName(){
        return this.departmentInfo.department() + " " + this.departmentInfo.courseLevel() + " " + this.departmentInfo.section();
    }

    @Override //this needs verified
    public boolean equals(Object o) {
        return true;
        //compare based only on ref number
    }

    @Override //assumes unique refNum
    public int hashCode() {
        return refNum;
    }
    public boolean doesCourseConflict(Course course){
        return true;
    }

    @Override
    public String toString(){
        return "Name: " + name + " DepInfo: " + departmentInfo + " TimeInfo: " + timeInfo + " Semester: "+ semester + " Credits: " + credits + " Prof: "+ professor + " ref: " + refNum + " Description: " + description;
    }
}
