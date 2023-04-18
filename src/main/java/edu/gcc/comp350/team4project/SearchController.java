package edu.gcc.comp350.team4project;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Search Object holds list of classes and and filters them
 * based on user's preferences
 */
public class SearchController {
    private final ArrayList<Course> courseList;
    private ArrayList<Course> filteredCourses;
    private ArrayList<Filter> currentFilters;

    public SearchController(ArrayList<Course> courseList, Semester semester) {
        //restrict courseList to courses of only one semester
        ArrayList<Course> oneSemCourses = new ArrayList<>(courseList);
        Iterator<Course> iterator = oneSemCourses.iterator();
        //For each course in the current available courses
        while (iterator.hasNext()) {
            Course c = iterator.next();
            //If the semester is not the one from te key remove if from the filtered course list
            if (!c.getSemester().equals(semester)){
                iterator.remove();
            }
        }
        currentFilters = new ArrayList<>();
        filteredCourses = new ArrayList<>(oneSemCourses);
        this.courseList = oneSemCourses;
    }


    /**
     *
     * @param times: An ArrayList of only two LocalTime objects.
     *             The first one is the start time. The second one is the end time.
     */
    public void filterByTime(ArrayList<LocalTime> times) {
        Iterator<Course> iterator = filteredCourses.iterator();
        //For each course in the current available courses
        while (iterator.hasNext()) {
            Course c = iterator.next();
            try{
                //If the start time of a course is before the key start time remove it
                //If the end time of a course if after the key end time remove it from the filtered courses list
                if (!((c.getTimeInfo().startTime().isAfter(times.get(0))||c.getTimeInfo().startTime().equals(times.get(0)))  && (c.getTimeInfo().endTime().isBefore(times.get(1))|| c.getTimeInfo().endTime().equals(times.get(1))))) {
                    iterator.remove();
                }
            }catch(Exception ignore){
                iterator.remove();
            }
        }
        //Saving the filter used
        Filter currentfilter = new Filter(FilterTypes.TIME, times);
        currentFilters.add(currentfilter);
    }

    public void filterByDays(ArrayList<DayOfWeek> days) {
        Iterator<Course> iterator = filteredCourses.iterator();
        //For each course in the current available courses
        while (iterator.hasNext()) {
            Course c = iterator.next();
            for (int i = 0; i < days.size(); i++){
                //If the day is not in the key days remove it from the filter courses list
                if (!c.getDays().contains(days.get(i))){
                    iterator.remove();
                    break;
                }
            }
        }
        //Saving the filter used
        Filter currentfilter = new Filter(FilterTypes.DAYS, days);
        currentFilters.add(currentfilter);

    }

    public void filterByDept(String dept) {
        Iterator<Course> iterator = filteredCourses.iterator();
        //For each course in the current available courses
        while (iterator.hasNext()) {
            Course course = iterator.next();
            //if the course department does not match the key department remove it
            if (!course.getDepartmentInfo().department().equals(dept)) iterator.remove();
        }

        //Saving the filter used
        Filter currentfilter = new Filter(FilterTypes.DEPT, dept);
        currentFilters.add(currentfilter);

    }

    public void filterByProf(String profName) {
        Iterator<Course> iterator = filteredCourses.iterator();
        //For each course in the current available courses
        while (iterator.hasNext()) {
            Course c = iterator.next();
            //If the course's professor's name does not match the key professor name remove it from the filtered courses list
            if (!profName.equals(c.getProfessor())) { //needs to be iterator.remove() or will cause bugs, still needs to be tested
                iterator.remove();
            }
        }
        //Saving the filter used
        Filter currentfilter = new Filter(FilterTypes.PROF, profName);
        currentFilters.add(currentfilter);
    }

    public void filterByCredits(String credits) {
        Iterator<Course> iterator = filteredCourses.iterator();
        //For each course in the current available courses
        int creditsInt = Integer.parseInt(credits);
        while (iterator.hasNext()) {
            Course c = iterator.next();
            //If the number of credits does not match the key number of credits remove the course from filtered courses
            if (c.getCredits() != creditsInt) iterator.remove();
        }
        //Saving the filter used
        Filter currentfilter = new Filter(FilterTypes.CRED, credits);
        currentFilters.add(currentfilter);
    }

    public void filterByLevel(String level) {
        Iterator<Course> iterator = filteredCourses.iterator();
        //For each course in the current available courses
        while (iterator.hasNext()) {
            Course c = iterator.next();
            //If the Course level does not match the key level remove it from the filtered courses list
            if (c.getDepartmentInfo().courseLevel().charAt(0) != (level.charAt(0))) iterator.remove();
        }
        //Saving the filter used
        Filter currentfilter = new Filter(FilterTypes.LVL, level);
        currentFilters.add(currentfilter);
    }

    public void filterByPhrase(String searchPhrase) {
        Iterator<Course> iterator = filteredCourses.iterator();
        //For each course in the current available courses
        while (iterator.hasNext()) {
            Course course = iterator.next();
            //If the course name does not contain the key string phrase remove it from the filtered course list
            if (!course.getName().contains(searchPhrase.toUpperCase())) iterator.remove();
        }
        //Saving the filter used
        Filter currentfilter = new Filter(FilterTypes.PHRASE, searchPhrase);
        currentFilters.add(currentfilter);
    }

    public void refreshFilteredCourses() {
        filteredCourses.clear();
        filteredCourses.addAll(courseList);
        currentFilters.clear();
    }

    public Course searchForRefNum(int refNum) {
        //Goes through all courses
        for (Course c: courseList) {
            //if the reference number matches the one from the course return it
            if (c.getRefNum() == refNum) return c;
        }
        return null;
    }

    public void removeSpecificFilter(FilterTypes type) {
        boolean containsFilter = false;
        //Goes through all filters currently used
        for(Filter f :currentFilters){
            //check if filter specified is in current filters used
            if(f.getType().equals(type)){
                containsFilter = true;
            }
        }
        if(containsFilter){
            //Iterates through the filters
            Iterator<Filter> iterator = currentFilters.iterator();
            while (iterator.hasNext()) {
                Filter f = iterator.next();
                //If filter type is a match remove it
                if (f.getType().equals(type)) {
                    currentFilters.remove(f);
                    break;
                }
            }
            //Creates a deep copy of the current filters
            ArrayList<Filter> tempList = new ArrayList<>(currentFilters);
            currentFilters.clear();
            this.refreshFilteredCourses();
            //Refilters but without the filte just removed
            for (Filter f : tempList) {
                if (f.getType().equals(FilterTypes.TIME)) {
                    filterByTime((ArrayList<LocalTime>) f.getValue());
                }
                if (f.getType().equals(FilterTypes.DAYS)) {
                    filterByDays((ArrayList<DayOfWeek>) f.getValue());
                }
                if (f.getType().equals(FilterTypes.DEPT)) {
                    filterByDept((String) f.getValue());
                }
                if (f.getType().equals(FilterTypes.PROF)) {
                    filterByProf((String) f.getValue());
                }
                if (f.getType().equals(FilterTypes.CRED)){
                    filterByCredits((String)f.getValue());
                }
                if (f.getType().equals(FilterTypes.LVL)) {
                    filterByLevel((String) f.getValue());
                }
                if (f.getType().equals(FilterTypes.PHRASE)) {
                    filterByPhrase((String) f.getValue());
                }
            }
        }
    }

    /**
     * Gets the value of the filterByPhrase filter
     * @return value of the filterByPhrase filter
     */
    public String getCurrentSearchPhrase(){
        for(Filter f : currentFilters){
            if(f.getType().equals(FilterTypes.PHRASE)){
                return (String) f.getValue();
            }
        }
        return "-none-";
    }

    /**
     *
     * @return the list of Filtered Courses
     */
    public ArrayList<Course> getFilteredCourses() {
        return filteredCourses;
    }

    /**
     *
     * @return returns filters currently being used
     */
    public ArrayList<Filter> getCurrentFilters() {
        return currentFilters;
    }

}

/**
 * Filter object holds the type of filter applied as well as the value
 * of the filter applied
 */
class Filter{
    private FilterTypes type;
    private Object value;

    public Filter(FilterTypes type, Object value){
        this.type = type;
        this.value = value;
    }

    public FilterTypes getType(){
        return type;
    }
    public Object getValue(){
        return value;
    }
}
