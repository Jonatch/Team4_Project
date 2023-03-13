package edu.gcc.comp350.team4project;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

record TimeInfo(DayOfWeek[] days, LocalTime startTime, LocalTime endTime) {}
public class Course {
    private String name;
    private int refNum;
    private int credits;
    private String professor;
    private String location;
    private String description;
    private TimeInfo timeInfo;

    public Course() {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public LocalTime[] getTime() {
        return new LocalTime[]{LocalTime.now()};
    }


    public DayOfWeek[] getDays() {
        return new DayOfWeek[]{DayOfWeek.MONDAY};
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
    public String toString(){return "";}
}
