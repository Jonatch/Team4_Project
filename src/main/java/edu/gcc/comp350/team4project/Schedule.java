package edu.gcc.comp350.team4project;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Schedule {
    private String scheduleName;
    private Semester semester;
    private ArrayList<Course> courses;
    private ArrayList<ScheduleElement> events;
    private int totalCredits;

    final int ROWS = 53;
    final int COLS = 6;
    final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}; // Declare a constant array of days of the week
    final ArrayList<LocalTime> TIMES = new ArrayList<>(); // Declare an array list to store time slots for the calendar
    final ArrayList<LocalTime> smallTIMES = new ArrayList<>(); // Declare an array list to store smaller time slots for each course
    private LocalTime sTime;
    private LocalTime eTime;
    final String[][] schedule = new String[ROWS][COLS];


    public Schedule(String scheduleName, Semester semester) {
        this.scheduleName = scheduleName;
        this.semester = semester;
        this.totalCredits = 0;
        this.courses = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    /**
     * just a stub method but later will be used to prompt user to add a custom event
     */
    public void createCustomEvent() {

    }

    public String getScheduleName() { return scheduleName; }
    public void setScheduleName(String scheduleName) { this.scheduleName = scheduleName; }
    public Semester getSemester() { return semester; }
    public ArrayList<ScheduleElement> getEvents() { return events; }
    public ArrayList<Course> getCourses() { return courses; }
    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }
    public String getSemesterString(){
        return getSemester().toString();
    }

    public String getTotalCreditsString(){
        return ""+getTotalCredits();
    }

    public void addCourse(Course newCourse) throws Exception{ //adds courses and throws an exception if there are conflicts
        for(Course course : courses){
            if(newCourse.getRefNum()==course.getRefNum()){
                throw new Exception("Cannot add the same course to a schedule twice");
            }
            else if(newCourse.doesCourseConflict(course)){
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

    public void addCourses(ArrayList<Course> courses) throws Exception {//adds multiple courses if possible
        for(Course course : courses){
            try{
                this.addCourse(course);
            }catch(Exception e){
                continue;
            };
        }
    }

    public void removeEvent(ScheduleElement event) throws Exception {
        //TODO: Test!
        if (events.remove(event)) totalCredits -= event.getCredits();
        else throw new Exception("Event " + event.getName() + " was not found in the schedule.");
    }

    public void removeEventByID(int id) {
        for(ScheduleElement e : this.events){
            if(id == e.getRefNum()){
                events.remove(e);
                totalCredits -= e.getCredits();
                break;
            }
        }
    }

    public void removeCourse(Course course) throws Exception{ //removes courses and updates credit amnt
        if(!(this.courses.remove(course))){
            throw new Exception("Course " + course.getName() + " was not found in the schedule and was not removed");
        }
        else{
            this.totalCredits -= course.getCredits();
        }
    }

    public void removeEvents(ArrayList<ScheduleElement> events) throws Exception {
        for (ScheduleElement event: events) {
            removeEvent(event);
        }
    }


    public void removeCourses(ArrayList<Course> courses) throws Exception{//remove multiple courses
        for(Course course : courses){
            try{
                this.removeCourse(course);
            }catch(Exception ignored){}
        }
    }

    public void removeAllEvents() {
        events.clear();
    }

    public void removeAllCourses(){//not tested
        courses.clear();
    }
    public int getTotalCredits() {
        return totalCredits;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("SCHEDULE_NAME: ").append(scheduleName).append(" ");
        sb.append("SEMESTER: ").append(semester.toString().toLowerCase()).append(" ");
        sb.append("CREDITS: ").append(totalCredits);
        return sb.toString();
    }

    public String toCalenderView() {

        LocalTime l = LocalTime.of(8, 00);
        for (int i = 0; i < 53; i++) { // Populate the TIMES array list with time slots
            TIMES.add(l);
            l = l.plusMinutes(15);
        }

        LocalTime l2 = LocalTime.of(8, 00);
        for (int i = 0; i < 159; i++) { // Populate the smallTIMES array list with smaller time slots
            smallTIMES.add(l2);
            l2 = l2.plusMinutes(5);
        }

        //set the top row to the days of the week
        for (int col = 1; col < COLS; col++) { // Set the top row of the schedule with the days of the week
            schedule[0][col] = DAYS[col-1];
        }

        //set the left column to the time slots
        for (int row = 1; row < ROWS; row++) { // Set the left column of the schedule with the time slots
            String s = TIMES.get(row-1).toString();
            schedule[row][0] = s;
        }

        for (int row = 1; row < ROWS; row++) {
            for (int col = 1; col < COLS; col++) {
                schedule[row][col] = "\t\t"; // Initialize all other cells of the schedule with empty strings
            }
        }

        schedule[0][0] = "\t\t"; // Initialize the top left cell of the schedule with an empty string


// Loop through each course in the list of courses
        for (Course course : courses){

            // Get the start and end times of the course, and the days it occurs
            sTime = course.getTimeInfo().startTime();
            eTime = course.getTimeInfo().endTime();
            String days = course.getDays().toString();

            // If the course occurs on Monday
            if (days.contains("MONDAY")) {
                calendarHelper(1, course);
            }

            // Repeat the above process for each day of the week that the course occurs on
            if (days.contains("TUESDAY")) {
                calendarHelper(2, course);
            }
            if (days.contains("WEDNESDAY")) {
                calendarHelper(3, course);
            }
            if (days.contains("THURSDAY")) {
                calendarHelper(4, course);
            }
            if (days.contains("FRIDAY")) {
                calendarHelper(5, course);
            }
        }
        // Return a string representation of the schedule array
        return this.toString() + "\n" + Arrays.deepToString(schedule).replace("], ", "]\n");    }

    public void calendarHelper(int dayNum, Course course) {
        int timeslot = 0;
        for (LocalTime t : smallTIMES) {
            if (sTime.equals(t)) {
                schedule[(timeslot/3)+1][dayNum] = course.getDepartmentInfo().department() +
                        course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                t = t.plusMinutes(5);
                timeslot++;
                while (eTime.isAfter(t)) {
                    schedule[(timeslot/3)+1][dayNum] = course.getDepartmentInfo().department() +
                            course.getDepartmentInfo().courseLevel() + course.getDepartmentInfo().section();
                    t = t.plusMinutes(5);
                    timeslot++;
                }
            }
            timeslot++;
        }
    }

    public String toTableView() {
        StringBuilder sb = new StringBuilder();
        // Append schedule name
        sb.append("NAME: " + this.scheduleName + " ");
        // Append semester name in lowercase
        sb.append("SEMESTER: " + this.semester.toString().toLowerCase() + " ");
        // Append total credits
        sb.append("CREDITS: " + this.totalCredits + "\n");
        // Append courses header
        sb.append("     COURSES: " + "\n");
        // Append each course in the list of courses
        for (Course course : this.courses) {
            sb.append("         --" + course.toString() + "\n");
        }
        // Convert StringBuilder to String and return
        return sb.toString();
    }

    public ArrayList<String> getRefNumsAsStrings(){
        ArrayList<String> refNumsAsStrings = new ArrayList<>();
        for(Course c : this.courses){
            refNumsAsStrings.add(String.valueOf(c.getRefNum()));
        }
        return refNumsAsStrings;
    }

//    public boolean addEvent(ScheduleElement newEvent) {
//        for (ScheduleElement event: events) {
//            if (event.equals(newEvent)) { //event is already added, do not add
//                System.out.println("THIS EVENT IS ALREADY ADDED");
//                return false;
//            }
//            else if (event.doesCourseConflict(newEvent)) { //
//                if (newEvent.isAnEvent()) {
//                    System.out.println("THERE IS AN EVENT OCCUPYING THIS TIMESLOT");
//                    return false;
//                }
//                Course conflictingCourse = (Course) newEvent; //cast to Course because it has a getSection() method
//                SearchController sb = new SearchController(totalCourses, semester);
//                HashSet<Course> potentialCourses = getAllOtherSections(conflictingCourse, sb.getFilteredCourses());
//
//                ArrayList<Course> suggestions = suggestOtherCourses(potentialCourses, events);
//                Course course = chooseSuggestions(suggestions);
//                events.add(course);
//                totalCredits += course.getCredits();
//                return true;
//            }
//        }
//        events.add(newEvent);
//        totalCredits += newEvent.getCredits();
//        return true;
//    }
//
//    private ArrayList<Course> suggestOtherCourses(HashSet<Course> potentialCourses, ArrayList<ScheduleElement> events) {
//        ArrayList<Course> suggestions = new ArrayList<>();
//
//        for (Course course : potentialCourses) {
//            boolean conflict = false;
//            for (ScheduleElement event : events) {
//                if (course.doesCourseConflict(event)) {
//                    conflict = true;
//                    break;
//                }
//            }
//            if (!conflict) suggestions.add(course);
//        }
//        return suggestions;
//    }
//
//    private HashSet<Course> getAllOtherSections(Course course, ArrayList<Course> courses) {
//        HashSet<Course> set = new HashSet<>();
//
//        for (Course c: courses) {
//            if (c.getRefNum() == course.getRefNum() && c.getSection() != course.getSection()) set.add(c);
//        }
//
//        return set;
//    }
//
//    private Course chooseSuggestions(ArrayList<Course> suggestions) {
//        Scanner input = new Scanner(System.in);
//        for (int i = 0; i < suggestions.size(); i++) {
//            System.out.println(i + ": " + suggestions.get(i));
//        }
//        System.out.println("Input the number corresponding to the course you wish to add: ");
//        int i = input.nextInt();
//
//        return suggestions.get(i);
//    }
}