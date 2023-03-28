package edu.gcc.comp350.team4project;


import java.sql.*;
import java.util.ArrayList;

public class DatabaseController {
    //private static ArrayList<String> usernames = new ArrayList<String>(); //why does User class save usernames?
    //private static HashMap<String, String> login = new HashMap<String, String>();
    //private static ArrayList<User> users = new ArrayList<User>();

    public static boolean authenticateUser(String name,String password){
        return true;
    }

    public static boolean checkIfUserExists(String name){
        return false;
    }


    public static User pullUser(String name){
        return new User("DybasJW20", "Junior", "testpass", false);
    }

    public static void updateUser(User user){

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

    private Connection connect() {
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




    public void insertNewUser(String username, String year, String password, ArrayList<Schedule> schedules) {
        String sql = "INSERT INTO Users(username,year,password,schedules) VALUES(?,?,?,?)";
        String stringSchedules = "";
        for (Schedule sched : schedules){
            String temp = sched + "\n\n";
            stringSchedules += temp;
        }

        try (Connection conn = this.connect();
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
//    public static void main(String[] args) throws Exception {
//        ArrayList<DayOfWeek> testDays = new ArrayList<DayOfWeek>();
//        testDays.add(DayOfWeek.MONDAY);
//        testDays.add(DayOfWeek.WEDNESDAY);
//        testDays.add(DayOfWeek.FRIDAY);
//        Course test2 = new Course(2, "COMP", Semester.FALL, "400", 'A',
//                "test course", 3, "Dr. test", "a test course", testDays, LocalTime.of(10,0,0), LocalTime.of(10,50,0));
//        Course test1 = new Course(1, "COMP", Semester.FALL, "300", 'A',
//                "test course", 3, "Dr. test", "a test course", testDays, LocalTime.of(9,0,0), LocalTime.of(9,50,0));
//        Course test3 = new Course(2, "COMP", Semester.SPRING, "400", 'A',
//                "test course", 3, "Dr. test", "a test course", testDays, LocalTime.of(10,0,0), LocalTime.of(10,50,0));
//        Course test4 = new Course(1, "COMP", Semester.SPRING, "300", 'A',
//                "test course", 3, "Dr. test", "a test course", testDays, LocalTime.of(9,0,0), LocalTime.of(9,50,0));
//        createNewDatabase("team4_project.db");
//        drop();
//        createNewTable();
//        StoreContents users = new StoreContents();
//        User jonah = new User("DybasJW20", "Junior", "testpass", false);
//        Schedule test = new Schedule("test_sched", Semester.FALL);
//        Schedule test_2 = new Schedule("test_sched2", Semester.SPRING);
//        test.addCourse(test1);
//        test.addCourse(test2);
//        test_2.addCourse(test3);
//        test_2.addCourse(test4);
//        jonah.saveScheduleToUser(test);
//        jonah.saveScheduleToUser(test_2);
//
//        users.insert("DybasJW20", "Junior", "testpass", jonah.getSchedules());
//
//    }
}
