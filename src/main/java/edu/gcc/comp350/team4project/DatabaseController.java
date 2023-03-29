package edu.gcc.comp350.team4project;


import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DatabaseController {

    public static boolean authenticateUser(String name, String password) {
        String sql = "SELECT username, password from Users WHERE username = ?";
        try (Connection conn = DatabaseController.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();

            String username = "";
            String pass = "";
            while (rs.next()) {
                username = rs.getString("username");
                pass = rs.getString("password");
            }

            if (username.equals(name) && pass.equals(password)) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    public static void pullUser(String name){
        String sql = "SELECT * FROM Users WHERE username = ?";
        String username = "", year = "", password = "", schedules = "";
        String schedName = "";

        try (Connection conn = DatabaseController.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                username = rs.getString("username");
                 year = rs.getString("year");
                 password = rs.getString("password");
                schedules = rs.getString("schedules");
            }
            User pulledUser = new User(username, year, password, false);
            Scanner sc = new Scanner(schedules);
            //TODO: Loop through sc to find + create the schedule and course info




        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateUser(User user){
        String sql = "UPDATE Users SET schedules = ? WHERE username = ?";

        String stringSchedules = "";
        for (Schedule sched : user.getSchedules()){
            String temp = sched + "\n\n";
            stringSchedules += temp;
        }

        try (Connection conn = DatabaseController.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, stringSchedules);
            pstmt.setString(2, user.getUsername());

            int rowsUpdated = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * @param fileName the database file name
     */
    public static void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/team4_project.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/team4_project.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                + "  id integer PRIMARY KEY,\n"
                + "  username text NOT NULL,\n"
                + "  year text NOT NULL,\n"
                + "  password text NOT NULL,\n"
                + "  schedules text\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    public static void insert(String username, String year, String password, ArrayList<Schedule> schedules) {
        String sql = "INSERT INTO Users(username,year,password,schedules) VALUES(?,?,?,?)";
        String stringSchedules = "";
        for (Schedule sched : schedules){
            String temp = sched + "\n\n";
            stringSchedules += temp;
        }

        try (Connection conn = DatabaseController.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, year);
            pstmt.setString(3, password);
            pstmt.setString(4, stringSchedules);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void drop(){
        String url = "jdbc:sqlite:C://sqlite/db/team4_project.db";

        String sql = "DROP TABLE Users;";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
//        ArrayList<DayOfWeek> testDays = new ArrayList<DayOfWeek>();
//        testDays.add(DayOfWeek.MONDAY);
//        testDays.add(DayOfWeek.WEDNESDAY);
//        testDays.add(DayOfWeek.FRIDAY);
//        Course test2 = new Course(2, "COMP", Semester.FALL, "400", 'A',
//                "test course", 3, "Dr. test", "a test course", testDays, LocalTime.of(10,0,0), LocalTime.of(10,50,0));
//        Course test1 = new Course(1, "COMP", Semester.FALL, "300", 'A',
//                "test course", 3, "Dr. test", "a test course", testDays, LocalTime.of(9,0,0), LocalTime.of(9,50,0));
//        Course test3 = new Course(20, "EDUC", Semester.SPRING, "400", 'A',
//                "test course", 3, "Dr. test", "a test course", testDays, LocalTime.of(10,0,0), LocalTime.of(10,50,0));
//        Course test4 = new Course(10, "EDUC", Semester.SPRING, "300", 'A',
//                "test course", 3, "Dr. test", "a test course", testDays, LocalTime.of(9,0,0), LocalTime.of(9,50,0));
//        createNewDatabase("team4_project.db");
//        drop();
//        createNewTable();
//        StoreContents users = new StoreContents();
//        User jonah = new User("DybasJW20", "Junior", "testpass", false);
//        Schedule test = new Schedule("newer_test_sched", Semester.SPRING);
//        Schedule test_2 = new Schedule("test_sched2", Semester.SPRING);
//        test.addCourse(test3);
//        test.addCourse(test4);
//        test_2.addCourse(test3);
//        test_2.addCourse(test4);

//        System.out.println(jonah.getSchedules());
//        jonah.saveScheduleToUser(test);
//        users.updateUser(jonah);
//        users.insert(jonah.getUsername(), "Junior", "testpass", jonah.getSchedules());
//        pullUser(jonah.getUsername());
//        updateUser(jonah);
//        if (users.authenticateUser("DybasJW20", "testpass") == true) {
//            System.out.println("Autheticated!");
//        }else{
//            System.out.println("Failed to authenticate");
//        }
    }
}
