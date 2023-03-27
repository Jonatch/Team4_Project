package edu.gcc.comp350.team4project;

import java.util.ArrayList;
import java.util.Iterator;

public class Search {
    private ArrayList<Course> courseList;
    private ArrayList<Course> filteredCourses;
    ArrayList<String> currentFilters;

    public Search(ArrayList<Course> courseList) {
        currentFilters = new ArrayList<>();
        filteredCourses = new ArrayList<>(courseList);
        this.courseList = courseList;
    }

    public void filterByTime(String time) {

    }

    public void filterByDays(String days) {

    }

    public void filterByDept(String dept) {
        Iterator<Course> iterator = filteredCourses.iterator();
        while (iterator.hasNext()) {
            Course course = iterator.next();
            if (!course.getDepartmentInfo().department().equals(dept)) iterator.remove();
        }
        currentFilters.add("department");
    }

    public void filterByProf(String profName) {
        Iterator<Course> iterator = filteredCourses.iterator();
        while (iterator.hasNext()) {
            Course c = iterator.next();
            if (!profName.equals(c.getProfessor())) { //needs to be iterator.remove() or will cause bugs, still needs to be tested
                iterator.remove();
            }
        }
        currentFilters.add("professor");
    }

    public void filterByLevel(int level) {
        /*
        TODO: THIS NEEDS TO BE IMPLEMENTED SO THAT ALL COURSES WITH THAT LEVEL ARE SHOWN
        TODO: i.e.: if level == 100, then all 100 level courses are kept like 101, 160, 180, etc...
        I (Ammiel) can do this when I get a chance
         */
    }

    public void filterByPhrase(String searchPhrase) {
        Iterator<Course> iterator = filteredCourses.iterator();
        while (iterator.hasNext()) {
            Course course = iterator.next();
            if (!course.getName().contains(searchPhrase.toUpperCase())) iterator.remove();
        }
        currentFilters.add("phrase");
    }

    public void removeAllFilters() {
        filteredCourses.clear();
        filteredCourses.addAll(courseList);
    }

    public void removeSpecificFilter(String filter) {

    }

    public ArrayList<Course> getFilteredCourses() {
        return filteredCourses;
    }

    public void setFilteredCourses(ArrayList<Course> courseList) {
        this.filteredCourses = courseList;
    }

}
