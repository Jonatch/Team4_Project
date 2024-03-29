package edu.gcc.comp350.team4project;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Event extends ScheduleElement {


    public Event(String name, ArrayList<DayOfWeek> days, LocalTime startTime, LocalTime endTime, String description) {
        super(name, days, startTime, endTime, description,0,0);
        this.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Event other = (Event) obj;
        return Objects.equals(hashCode(), other.hashCode());
    }

    @Override
    public int hashCode() { return (refNum = Objects.hash(name, startTime, endTime)); }

    @Override
    public String toString() {
        return "EVENT: " + name + ", DAYS:" + timeInfo.days() + ", TIME: " + timeInfo.startTime() + "-" + timeInfo.endTime() + ", CREDITS: " + credits;
    }

    /**
     * this method will ask the user to input the number of credits this event will take.
     * this allows for max of 17 credits and a min of 0
     */

    public String getLevel() { return null; }
    public boolean isAnEvent() { return true; }
    public int getRefNum() { return hashCode(); }
    public void setCredits(int credits) { this.credits = credits; }
    public void setDescription(String description) { this.description = description; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public void setDays(ArrayList<DayOfWeek> days) { this.days = days; }

    public String getFormattedCourseTitle(){
        return "Event: " + this.name.toUpperCase() + " (" + this.refNum + ")";
    }

    public String getNameLabel(){
        return this.name;
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            String start = timeInfo.startTime().format(formatter);
            String end = timeInfo.endTime().format(formatter);
            time = start + "-" + end;
        }else{
            time = "Online course / No time";
        }
        return daysFormatted + " \n"
                + time;
    }
}
