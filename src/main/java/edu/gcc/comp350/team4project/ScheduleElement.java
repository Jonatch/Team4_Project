package edu.gcc.comp350.team4project;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class ScheduleElement {
    protected String name, description;
    protected int credits, refNum;
    protected ArrayList<DayOfWeek> days;
    protected TimeInfo timeInfo;
    protected LocalTime startTime, endTime;
    public ScheduleElement(String name, ArrayList<DayOfWeek> days, LocalTime startTime, LocalTime endTime, String description) {
        this.name = name;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.timeInfo = new TimeInfo(days, startTime, endTime);
    }

    record TimeInfo(ArrayList<DayOfWeek> days, LocalTime startTime, LocalTime endTime) {
        public boolean doesOverLap(TimeInfo timeInfo) {
            //TODO: TEST!
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

    public boolean doesCourseConflict(ScheduleElement e) {
        //TODO: Test!
        if (this.equals(e)) return true;
        else return this.timeInfo.doesOverLap(e.getTimeInfo());
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    public boolean conflictsWith(ScheduleElement event) {
        return this.equals(event);
    }

    public String getName() { return name; }
    public abstract int getRefNum();
    public int getCredits() { return credits; }
    public ArrayList<DayOfWeek> getDays() { return days; }
    public TimeInfo getTimeInfo() { return timeInfo; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }


}
