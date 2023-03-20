package edu.gcc.comp350.team4project;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class Schedule {
    private String scheduleName;
    private Semester semester;
    private ArrayList<Course> courses;
    private int totalCredits;

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
        System.out.println("Day     Sunday     Monday     Tuesday     Wednesday     Thursday     Friday     Saturday");
        System.out.println("Times of Day");
    }


    public String toTableView() {

        return courses.toString();
    }

}

