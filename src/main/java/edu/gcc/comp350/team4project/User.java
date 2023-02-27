package edu.gcc.comp350.team4project;

import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<String> majors;
    private String year;
    private ArrayList<String> minors;
    private String email;
    private String password;
    private boolean isGuest;
    private ArrayList<Schedule> schedules;
    private static final int MAX_SCHEDULES = 5;


    public User(String name) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getMajors() {
        return majors;
    }

    public void addMajor(String major) {

    }
    public void removeMajor(String major) {

    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ArrayList<String> getMinors() {
        return minors;
    }

    public void addMinor(String minor) {

    }
    public void removeMinor(String minor) {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void addSchedule(Schedule schedule) {

    }
    public void removeSchedule(Schedule schedule) {

    }

    @Override
    public String toString(){
        return "string";
    }

    @Override
    public boolean equals(Object user){
        return true;
    }
}
