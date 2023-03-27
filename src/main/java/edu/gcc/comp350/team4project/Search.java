package edu.gcc.comp350.team4project;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

class Filter{
    private String type;
    private Object value;

    public Filter(String type, Object value){
        this.type = type;
        this.value = value;
    }

    public String getType(){
        return type;
    }
    public Object getValue(){
        return value;
    }
}

public class Search {
    private ArrayList<Course> courseList;
    private ArrayList<Course> filteredCourses;
    ArrayList<Filter> currentFilters;

    public Search(ArrayList<Course> courseList) {
        /*
        I'm thinking that it might be a good idea to make filteredCourses hold all classes to begin and remove classes
        that don't fit the filters, that would probably make the process more efficient and make implementing multiple
        filters easier
        */
        filteredCourses = new ArrayList<>(courseList);
        this.courseList = courseList;
    }

    public void filterByTime(ArrayList<LocalTime> start_times) {
        Iterator<Course> iterator = filteredCourses.iterator();
        while (iterator.hasNext()) {
            Course c = iterator.next();
            if (!start_times.contains(c.getTimeInfo().startTime())){
                filteredCourses.remove(c);
            }
        }
        //Saving the filter used
        Filter currentfilter = new Filter("time", start_times);
        currentFilters.add(currentfilter);
    }

    public void filterByDays(ArrayList<DayOfWeek> days) {
        Iterator<Course> iterator = filteredCourses.iterator();
        while (iterator.hasNext()) {
            Course c = iterator.next();
            for (int i = 0; i < days.size(); i++){
                if (!c.getDays().contains(days.get(i))){
                    filteredCourses.remove(c);
                }
            }
        }
        //Saving the filter used
        Filter currentfilter = new Filter("days", days);
        currentFilters.add(currentfilter);

    }

    public void filterByDept(String dept) {
        Iterator<Course> iterator = filteredCourses.iterator();
        while (iterator.hasNext()) {
            Course c = iterator.next();
            if (!c.getDepartmentName().equals(dept)){
                filteredCourses.remove(c);
            }
        }
        //Saving the filter used
        Filter currentfilter = new Filter("department", dept);
        currentFilters.add(currentfilter);
    }

    public void filterByProf(String profName) {
        Iterator<Course> iterator = filteredCourses.iterator();
        while (iterator.hasNext()) {
            Course c = iterator.next();
            if (!profName.equals(c.getProfessor())) {
                filteredCourses.remove(c);
            }
        }
        //Saving the filter used
        Filter currentfilter = new Filter("professor", profName);
        currentFilters.add(currentfilter);
    }

    public void filterByLevel(int level) {
        Iterator<Course> iterator = filteredCourses.iterator();
//        while (iterator.hasNext()) {
//            Course course = iterator.next();
//            if (course.getDepartmentInfo().getCourseLevel() != level) {
//                iterator.remove();
//            }
//        }
        //Saving the filter used
        Filter currentfilter = new Filter("phrase", level);
        currentFilters.add(currentfilter);
    }

    public void filterByPhrase(String searchPhrase) {
        Iterator<Course> iterator = filteredCourses.iterator();
        while (iterator.hasNext()) {
            Course course = iterator.next();
            if (!course.getName().contains(searchPhrase.toUpperCase())) iterator.remove();
        }
        //Saving the filter used
        Filter currentfilter = new Filter("phrase", searchPhrase);
        currentFilters.add(currentfilter);
    }

    public void removeAllFilters() {
        filteredCourses.clear();
        filteredCourses.addAll(courseList);
    }

    public void removeSpecificFilter(String filter) {
        Iterator<Filter> iterator = currentFilters.iterator();
        while(iterator.hasNext()){
            Filter f = iterator.next();
            if(f.getType().equals(filter)){
                currentFilters.remove(f);
            }
        }
        for (Filter f : currentFilters){
            if (f.getType().equals("time")){
                filterByTime((ArrayList<LocalTime>)f.getValue());
            }
            if (f.getType().equals("days")){
                filterByDays((ArrayList<DayOfWeek>) f.getValue());
            }
            if (f.getType().equals("department")){
                filterByDept((String)f.getValue());
            }
            if (f.getType().equals("professor")){
                filterByProf((String)f.getValue());
            }
            if (f.getType().equals("level")){
                filterByLevel((int)f.getValue());
            }
            if (f.getType().equals("phrase")){
                filterByPhrase((String)f.getValue());
            }
        }
    }

    public ArrayList<Course> getFilteredCourses() {
        return filteredCourses;
    }

}
