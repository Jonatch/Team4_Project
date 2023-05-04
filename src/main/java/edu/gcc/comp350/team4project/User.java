package edu.gcc.comp350.team4project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class User {
    private String username;
    private String password;
    private String year;
    private boolean isGuest;
    private ArrayList<Schedule> schedules;
    private static final int MAX_SCHEDULES = 5;


    public User(String username, String year, String password, boolean isGuest) {
        this.username = username;
        this.password = password;
        this.year = year;
        this.isGuest = isGuest;
        this.schedules = new ArrayList<>();
    }


    public String getUsername() {
        return username;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public int getNumMaxSchedules() {
        return MAX_SCHEDULES;
    }


    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public Schedule getSchedule(String scheduleName){
        for (Schedule s : schedules){
            if (s.getScheduleName().equals(scheduleName)){
                return s;
            }
        }
        return new Schedule("Schedule not found", Semester.FALL);
    }

    public void saveScheduleToUser(Schedule newSchedule) { //Shouldn't this just pass in a schedule? and be called save schedule?
        schedules.add(newSchedule);
//        if (newSchedule.getTotalCredits() < 12) {
//            throw new Exception("Warning: Schedule is less than 12 credits");
//        }
//        if (newSchedule.getTotalCredits() > 17) {
//            throw new Exception("Warning: Schedule is greater than 17 credits");
//        }
    }

    public void removeSchedule(int index) {
        schedules.remove(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("USER:\n");
        sb.append("--Username: " + this.username + " ");
        sb.append("Password: " + this.password + " ");
        sb.append("Year: " + this.year + " ");
        sb.append("Guest?: " + this.isGuest + "\n");
        sb.append("     Schedules:\n");
        for (Schedule s : schedules) {
            sb.append("     --");
            sb.append(s);
        }
        return sb.toString();
    }
}
