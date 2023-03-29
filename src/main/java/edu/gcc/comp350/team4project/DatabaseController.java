package edu.gcc.comp350.team4project;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.time.*;

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

    public static boolean checkIfUserInDB(String name){
        String sql = "SELECT username, password from Users WHERE username = ?";
        try (Connection conn = DatabaseController.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();

            String username = "";
            //String pass = "";
            while (rs.next()) {
                username = rs.getString("username");
                //pass = rs.getString("password");
            }

            if (username.equals(name)) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;

    }

    public static User pullUser(String name){


        String sql = "SELECT * FROM Users WHERE username = ?";
        String username = "", year = "", password = "", schedules = "";
        String schedName = "";
        User pulledUser = null;

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
            pulledUser = new User(username, year, password, false);
            Scanner sc = new Scanner(schedules);
            boolean newS = false;
            while(sc.hasNext()){
                Schedule tempS = null;
                if(sc.nextLine().equals("SCHEDULE")){
                    newS = true;
                    Scanner sSc = new Scanner(sc.nextLine());
                    sSc.useDelimiter(",");
                    String scheduleName = sSc.next();
                    Semester scheduleSemester = Semester.valueOf(sSc.next());
                    tempS = new Schedule(scheduleName,scheduleSemester);
                }
                if (sc.nextLine().equals("COURSES")){
                    while(true) {
                        Scanner cSc = new Scanner(sc.nextLine());
                        //cSc.useDelimiter("(,\\s*|\\s+)");
                        cSc.useDelimiter(",");
                        //int refNum, String department, Semester semester, String courseLevel, char section, String name, int credits, String professor, String description, ArrayList<DayOfWeek> days, LocalTime startTime, LocalTime endTime
                        int refNum = Integer.parseInt(cSc.next());
                        String department = cSc.next();
                        Semester semester = Semester.valueOf(cSc.next());
                        String courseLevel = cSc.next();
                        char section = cSc.next().charAt(0);
                        String courseName = cSc.next();
                        int credits = Integer.parseInt(cSc.next());
                        String professor = cSc.next();
                        String description = cSc.next();
                        LocalTime startTime = LocalTime.parse(cSc.next());
                        LocalTime endTime = LocalTime.parse(cSc.next());
                        ArrayList<DayOfWeek> days = new ArrayList<>();
                        while (cSc.hasNext()) {
                            days.add(DayOfWeek.valueOf(cSc.next()));
                        }

                        Course tempC = new Course(refNum, department, semester, courseLevel, section, courseName, credits, professor, description, days, startTime, endTime);
                        assert tempS != null;
                        tempS.addCourse(tempC);
                        if (sc.nextLine().equals("+")){
                            break;
                        }
                    }
                }
                pulledUser.saveScheduleToUser(tempS);
            }





        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return pulledUser;
    }

    public static void updateUser(User user){
        String sql = "UPDATE Users SET schedules = ? WHERE username = ?";

        String stringSchedules = "";
        for (Schedule sched : user.getSchedules()) {
//            String temp = sched + "\n\n";
//            stringSchedules += temp;
            StringBuilder sb = new StringBuilder();
            sb.append("SCHEDULE");
            sb.append("\n");
            sb.append(sched.getScheduleName());
            sb.append(",");
            sb.append(sched.getSemester());
            sb.append("\n");
            //public Course(int refNum, String department, Semester semester, String courseLevel, char section, String name, int credits, String professor, String description, ArrayList<DayOfWeek> days, LocalTime startTime, LocalTime endTime) {
            for (Course course : sched.getCourses()) {
                sb.append("COURSES");
                sb.append("\n");
                sb.append(course.getRefNum());
                sb.append(",");
                sb.append(course.getDepartmentInfo().department());
                sb.append(",");
                sb.append(course.getSemester());
                sb.append(",");
                sb.append(course.getDepartmentInfo().courseLevel());
                sb.append(",");
                sb.append(course.getDepartmentInfo().section());
                sb.append(",");
                sb.append(course.getName());
                sb.append(",");
                sb.append(course.getCredits());
                sb.append(",");
                sb.append(course.getProfessor());
                sb.append(",");
                sb.append(course.getDescription());
                sb.append(",");
                sb.append((course.getTimeInfo().startTime()));
                sb.append(",");
                sb.append(course.getTimeInfo().endTime());
                for (DayOfWeek d : course.getTimeInfo().days()) {
                    sb.append(",");
                    sb.append(d);
                }
                sb.append("\n");
            }
            sb.append("+\n");
            stringSchedules += sb.toString();
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
//                System.out.println("The driver name is " + meta.getDriverName());
//                System.out.println("A new database has been created.");
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




    public static void insert(User user) {
        String username = user.getUsername();
        String year = user.getYear();
        String password = user.getPassword();
        ArrayList<Schedule> schedules = user.getSchedules();
        String sql = "INSERT INTO Users(username,year,password,schedules) VALUES(?,?,?,?)";
        String stringSchedules = "";
        for (Schedule sched : schedules){
//            String temp = sched + "\n\n";
//            stringSchedules += temp;
            StringBuilder sb = new StringBuilder();
            sb.append("SCHEDULE");
            sb.append("\n");
            sb.append(sched.getScheduleName());
            sb.append(",");
            sb.append(sched.getSemester());
            sb.append("\n");
            //public Course(int refNum, String department, Semester semester, String courseLevel, char section, String name, int credits, String professor, String description, ArrayList<DayOfWeek> days, LocalTime startTime, LocalTime endTime) {
            for(Course course : sched.getCourses()){
                sb.append("COURSES");
                sb.append("\n");
                sb.append(course.getRefNum());
                sb.append(",");
                sb.append(course.getDepartmentInfo().department());
                sb.append(",");
                sb.append(course.getSemester());
                sb.append(",");
                sb.append(course.getDepartmentInfo().courseLevel());
                sb.append(",");
                sb.append(course.getDepartmentInfo().section());
                sb.append(",");
                sb.append(course.getName());
                sb.append(",");
                sb.append(course.getCredits());
                sb.append(",");
                sb.append(course.getProfessor());
                sb.append(",");
                sb.append(course.getDescription());
                sb.append(",");
                sb.append((course.getTimeInfo().startTime()));
                sb.append(",");
                sb.append(course.getTimeInfo().endTime());
                for(DayOfWeek d : course.getTimeInfo().days()){
                    sb.append(",");
                    sb.append(d);
                }
                sb.append("\n");
            }
            sb.append("+\n");
            stringSchedules+=sb.toString();
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

    public static void printAllUsers(){
        String sql = "SELECT * from Users";
        try (Connection conn = DatabaseController.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            String username = "";
            String pass = "";
            while (rs.next()) {
                username = rs.getString("username");
                pass = rs.getString("password");
                System.out.println("USERNAME: " + username + " PASSWORD: " + pass);

            }
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
//        User jonah = new User("DybasJW20", "Junior", "testpass", false);
//        Schedule test = new Schedule("newer test sched", Semester.FALL);
//        Schedule test_2 = new Schedule("test_sched2", Semester.SPRING);
//        test.addCourse(test1);
//        test.addCourse(test2);
//        test_2.addCourse(test3);
//        test_2.addCourse(test4);
//
//        jonah.saveScheduleToUser(test);
//        jonah.saveScheduleToUser(test_2);
//
//
////        DatabaseController.updateUser(jonah);
//        DatabaseController.insert(jonah);
//
//        if (DatabaseController.authenticateUser("DybasJW20", "testpass")) {
//            System.out.println("Autheticated!");
//        }else{
//            System.out.println("Failed to authenticate");
//        }
//
//        System.out.println(jonah);
//        User returnUser = pullUser("DybasJW20");
//        System.out.println("-----------------------------------------");
//        System.out.println(returnUser);
//        System.out.println("-----------------------------------------");
//        returnUser.removeSchedule(1);
//        Schedule test_3 = new Schedule("test_sched3", Semester.FALL);
//        test_3.addCourse(test1);
//        test_3.addCourse(test2);
//        returnUser.saveScheduleToUser(test_3);
//        updateUser(returnUser);
//        System.out.println(pullUser("DybasJW20"));
//
//





    }
}
