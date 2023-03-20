package edu.gcc.comp350.team4project;

import java.util.ArrayList;
import java.util.Iterator;

public class Search {
    private ArrayList<Course> courseList;
    private ArrayList<Course> filteredCourses;
    ArrayList<String> currentFilters;

    public Search(ArrayList<Course> courseList) {
        /*
        I'm thinking that it might be a good idea to make filteredCourses hold all classes to begin and remove classes
        that don't fit the filters, that would probably make the process more efficient and make implementing multiple
        filters easier
        */
        filteredCourses = new ArrayList<>(courseList);
        this.courseList = courseList;
    }

    public void filterByTime(String time) {

    }

    public void filterByDays(String days) {

    }

    public void filterByDept(String dept) {

    }

    public void filterByProf(String profName) {
        for (Course c: filteredCourses){
            if (!profName.equals(c.getProfessor())) {
                filteredCourses.remove(c);
            }
        }
        currentFilters.add("professor");
    }

    public void filterByLevel(int level) {

    }

    public void filterByPhrase(String searchPhrase) {
        Iterator<Course> iterator = filteredCourses.iterator();
        while (iterator.hasNext()) {
            Course course = iterator.next();
            if (!course.getName().contains(searchPhrase.toUpperCase())) {
                iterator.remove();
            }
        }
    }

    public void removeAllFilters() {

    }

    public void removeSpecificFilter(String filter) {

    }

    public ArrayList<Course> getFilteredCourses() {
        return filteredCourses;
    }

}
