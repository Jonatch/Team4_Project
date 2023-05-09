package edu.gcc.comp350.team4project;

import com.healthmarketscience.jackcess.Database;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

record DepartmentInfo(String department, String courseLevel, char section) {}
public class Course extends ScheduleElement {
    private DepartmentInfo departmentInfo;
    private String professor, courseLevel;
    private Semester semester;
    private char section;

    public Course(int refNum, String department, Semester semester, String courseLevel, char section, String name, int credits, String professor, String description, ArrayList<DayOfWeek> days, LocalTime startTime, LocalTime endTime) {
        super(name, days, startTime, endTime, description, credits, refNum);
        this.professor = professor;
        this.semester = semester;
        this.courseLevel = courseLevel;
        this.section = section;
        this.departmentInfo = new DepartmentInfo(department,courseLevel,section);
    }

    @Override
    public boolean equals(Object obj) {
        //TODO: Test!
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course other = (Course) obj;
        return refNum == other.refNum;
    }

    @Override
    public String toString(){
        return "REFNUM: " + refNum + ", DEPARTMENT_INFO: " + departmentInfo.department() + " " + departmentInfo.courseLevel() + " " + departmentInfo.section() + ", NAME: " + name + ", SEMESTER: "+ semester.toString().toLowerCase() +", DAYS: " + timeInfo.days() + ", TIME: " + timeInfo.startTime() + "-" + timeInfo.endTime() + ", PROF: " + professor + ", CREDITS: " + credits + ", DESCRIPTION: "+ description ;
    }

    public String getLevel() { return courseLevel; }
    public char getSection() { return section; }
    public boolean isAnEvent() { return false; }
    @Override
    public int hashCode() { return refNum; }
    public int getRefNum() { return refNum; }
    public Semester getSemester() { return semester; }
    public String getProfessor() { return professor; }
    public String getDescription() { return description; }
    public DepartmentInfo getDepartmentInfo(){ return this.departmentInfo; }
    public String getFormattedCourseTitle(){
        return this.departmentInfo.department() + " " + this.departmentInfo.courseLevel() + " " + this.departmentInfo.section()+ ": " + this.name.toUpperCase();
    }
    public String getNameLabel(){
        return departmentInfo.department() + " " + departmentInfo.courseLevel() + " " + departmentInfo.section() + " \n" + name;
    }
    public String getTimeLabel(){
        String daysFormatted = "";
        if(days.contains(DayOfWeek.MONDAY)){
            daysFormatted += "M";
        }
        if(days.contains(DayOfWeek.TUESDAY)){
            daysFormatted += "T";
        }
        if(days.contains(DayOfWeek.WEDNESDAY)){
            daysFormatted += "W";
        }
        if(days.contains(DayOfWeek.THURSDAY)){
            daysFormatted += "R";
        }
        if(days.contains(DayOfWeek.FRIDAY)){
            daysFormatted += "F";
        }

        String time;
        if(timeInfo.startTime()!=null){
            time = timeInfo.startTime() + "-" + timeInfo.endTime();
        }else{
            time = "Online course / No time";
        }
        return daysFormatted + " \n"
                + time;
    }

    public String getCredLabel(){
        return "Credits: " + this.credits;
    }



}
