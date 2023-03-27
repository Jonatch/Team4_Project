package edu.gcc.comp350.team4project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class User {
    private String username;
    private String year;
    private String password;
    private boolean isGuest;
    private ArrayList<Schedule> schedules;
    private static final int MAX_SCHEDULES = 5;
    private static ArrayList<String> usernames = new ArrayList<String>(); //why does User class save usernames?
    private static HashMap<String, String> login = new HashMap<String, String>();
    private static ArrayList<User> users = new ArrayList<User>();


    public User(String username, String year, String password, boolean isGuest) throws Exception {
        try {
            if (usernames.contains(username)) {
                throw new Exception("Username already exists.");
            }
            this.username = username;
            this.year = year;
            this.password = password;
            this.isGuest = isGuest;
            this.schedules = new ArrayList<Schedule>();
            usernames.add(username);
            login.put(username, password);
            users.add(this);
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
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


    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void saveScheduleToUser(Schedule newSchedule) throws Exception{ //Shouldn't this just pass in a schedule? and be called save schedule?
        schedules.add(newSchedule);
        if(newSchedule.getTotalCredits()<12){
//            throw new Exception("Warning: Schedule is less than 12 credits");
        }
        if(newSchedule.getTotalCredits()>17){
//            throw new Exception("Warning: Schedule is greater than 17 credits");
        }
    }

    public void removeSchedule(int index) {
        schedules.remove(index);
    }

    //basic login function that does minimal authentication
    public static User login(String username, String password) throws Exception {
        if (!authenticate(username, password)) {
            throw new Exception("Invalid username or password.");
        }
        User user = getUserByUsername(username);
        user.isGuest = false;
        return user;
    }

    //helper function for the authentication function to find a user
    private static User getUserByUsername(String username) {
        // loop through the ArrayList of users and return the user with the matching username
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // no user with the matching username was found
    }

    //authentication function used to see if a username is taken
    static boolean authenticate(String username, String password) {
        String storedPassword = login.get(username);
        if (storedPassword == null) {
            return false; // no such user exists
        }
        return storedPassword.equals(password);
    }

    //a basic registration function that allows a user to register as a user
    public static void register(String username, String year, String password) throws Exception {
        // Check if the username is already taken
        if (usernames.contains(username)) {
            System.out.println("Username is already taken."); //these exeptions?
            return;
        }

        // Create a new user object with the given parameters
        User newUser = new User(username, year, password, false);

        // Add the new user to the list of users
        users.add(newUser);

        // Output a message to confirm registration
        System.out.println("Registration successful.");
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
