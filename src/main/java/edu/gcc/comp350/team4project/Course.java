package edu.gcc.comp350.team4project;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

record TimeInfo(ArrayList<DayOfWeek> days, LocalTime startTime, LocalTime endTime) {
    public boolean doesOverLap(TimeInfo timeInfo){ //needs extensive testing
        Set<DayOfWeek> commonDays = new HashSet<>(this.days);
        commonDays.retainAll(timeInfo.days);

        for (DayOfWeek day: commonDays) {
            if (!(timeInfo.startTime.isAfter(this.endTime) || timeInfo.endTime.isBefore(this.startTime))) {
                return true;
            }
        }
        return false;
    }
}

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
    public Semester getSemester(){return this.semester;};
    public int getRefNum() {
        return refNum;
    }

//    public void setRefNum(int refNum) {
//        this.refNum = refNum;
//    }

    public int getCredits() {
        return credits;
    }

//    public void setCredits(int credits) {
//        this.credits = credits;
//    }

    public String getProfessor() {
        return professor;
    }

//    public void setProfessor(String professor) {
//        this.professor = professor;
//    }

    public String getDescription() {
        return description;
    }

//    public void setDescription(String description) {
//        this.description = description;
//    }

    public TimeInfo getTimeInfo(){
        return this.timeInfo;
    }

//    public void setTimeInfo(TimeInfo timeInfo){
//        this.timeInfo = timeInfo;
//    }

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

    public DepartmentInfo getDepartmentInfo(){
        return this.departmentInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;


        return this.refNum == course.refNum;
    }

    @Override
    public int hashCode() {
        return refNum;
    }

    public boolean doesCourseConflict(Course course){
        if(this.equals(course)){
            return true;
        }

        if(this.timeInfo.doesOverLap(course.timeInfo)){
            return true;
        }

        return false;
    }


    @Override
    public String toString(){
        //return "REFNUM: " + refNum + ", " + departmentInfo + ", " + timeInfo + ", SEMESTER: "+ semester + ", CREDITS: " + credits + ", PROF: " + professor + ", NAME: " + name + ", DESCRIPTION: " + description;
        return "REFNUM: " + refNum + ", DEPARTMENT_INFO: " + departmentInfo.department() + " " + departmentInfo.courseLevel() + " " + departmentInfo.section() + ", NAME: " + name + ", SEMESTER: "+ semester.toString().toLowerCase() +", TIME: " + timeInfo.startTime() + "-" + timeInfo.endTime() + ", PROF: " + professor + ", CREDITS: " + credits + ", DESCRIPTION: "+ description ;
    }
}
