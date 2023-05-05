package edu.gcc.comp350.team4project;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

record DepartmentInfo(String department, String courseLevel, char section) {}
public class Course extends ScheduleElement {
    private DepartmentInfo departmentInfo;
    private int refNum, credits;
    private String name, professor, courseLevel, description;
    private Semester semester;
    private TimeInfo timeInfo;
    private char section;

    public Course(int refNum, String department, Semester semester, String courseLevel, char section, String name, int credits, String professor, String description, ArrayList<DayOfWeek> days, LocalTime startTime, LocalTime endTime) {
        super(name, days, startTime, endTime, description);
        this.name = name;
        this.refNum = refNum;
        this.credits = credits;
        this.professor = professor;
        this.description = description;
        this.semester = semester;
        this.courseLevel = courseLevel;
        this.section = section;
        this.timeInfo = new TimeInfo(days,startTime,endTime);
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
    public String getCourseLabel(){
        String daysFormatted = "";
        if(days.contains("MONDAY")){
            daysFormatted += "M";
        }
        if(days.contains("TUESDAY")){
            daysFormatted += "T";
        }
        if(days.contains("WEDNESDAY")){
            daysFormatted += "W";
        }
        if(days.contains("THURSDAY")){
            daysFormatted += "R";
        }
        if(days.contains("FRIDAY")){
            daysFormatted += "F";
        }

        return departmentInfo.department() + " " + departmentInfo.courseLevel() + " " + departmentInfo.section() + " \n"
                + name + " \n"
                + daysFormatted + " \n"
                + timeInfo.startTime() + "-" + timeInfo.endTime();
    }

}
