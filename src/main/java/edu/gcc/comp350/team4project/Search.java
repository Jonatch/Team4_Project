package edu.gcc.comp350.team4project;

import java.util.ArrayList;

public class Search {
    String searchPhrase;
    ArrayList<Course> courseList;
    ArrayList<Course> filteredCourses;
    ArrayList<String> currentFilters;

    public Search(ArrayList<Course> courseList) {
        /*
        I'm thinking that it might be a good idea to make filteredCourses hold all classes to begin and remove classes
        that don't fit the filters, that would probably make the process more efficient and make implementing multiple
        filters easier
        */
        filteredCourses = new ArrayList<>();
        this.courseList = courseList;
    }

    public void filterByTime(String time) {

    }

    public void filterByDays(String days) {

    }

    public void filterByDept(String dept) {

    }

    public void filterByProf(String profName) {

    }

    public void filterByLevel(int level) {

    }

    public void filterByPhrase(String searchPhrase) {
        /*
        currently this works for a super basic search, a way to make it pretty complicated would be to have suggested
        courses whenever you typed a letter in, I was thinking a way to do that would be to have a txt file with all
        class names and have a search tree or something like that.
         */
        for (Course c: courseList) {
            if (c.getName().contains(searchPhrase.toUpperCase())) filteredCourses.add(c);
        }
    }

    public void removeAllFilters() {

    }

    public void removeSpecificFilter(String filter) {

    }

    public ArrayList<Course> getFilteredCourses() {
        return filteredCourses;
    }

    @Override
    public String toString() {
        /*
        I forgot what we said the toString for this class was going to do, is it necessary?
         */
        return "";
    }
}
