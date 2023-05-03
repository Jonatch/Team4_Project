package edu.gcc.comp350.team4project.forms;

public class ScheduleFormData {
    private String name;
    private String semester;

    public ScheduleFormData(){
        this.name = "";
        this.semester = "";
    }
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
    public String getSemester() {return semester;}

    public void setSemester(String semester) {this.semester = semester;}

}
