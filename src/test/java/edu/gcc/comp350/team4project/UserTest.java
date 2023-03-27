package edu.gcc.comp350.team4project;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testConstructor() throws Exception {
        User user = new User("testuser", "2023", "testpassword", false);
        assertEquals("testuser", user.getUsername());
        assertEquals("2023", user.getYear());
        assertEquals("testpassword", user.getPassword());
        assertFalse(user.isGuest());
    }

    @Test
    public void testDuplicateUsername() throws Exception {
        User user1 = new User("testuser", "2023", "testpassword", false);
        assertThrows(Exception.class, () -> new User("testuser", "2024", "testpassword2", false));
    }

    @Test
    public void testAuthenticate() throws Exception {
        assertFalse(User.authenticate("nonexistentuser", "testpassword"));
        User user = new User("testuser", "2023", "testpassword", false);
        assertTrue(User.authenticate("testuser", "testpassword"));
        assertFalse(User.authenticate("testuser", "wrongpassword"));
    }

    // Test case for addSchedule method with FALL semester
//    @Test
//    public void testAddSchedule_Fall() throws Exception {
//        User user = new User("username", "year", "password", false);
//        user.addSchedule("Schedule 1", Semester.FALL);
//        assertEquals(1, user.getSchedules().size());
//        assertEquals("Schedule 1", user.getSchedules().get(0).getScheduleName());
//        assertEquals(Semester.FALL, user.getSchedules().get(0).getSemester());
//    }
//
//    // Test case for addSchedule method with SPRING semester
//    @Test
//    public void testAddSchedule_Spring() throws Exception {
//        User user = new User("username", "year", "password", false);
//        user.addSchedule("Schedule 2", Semester.SPRING);
//        assertEquals(1, user.getSchedules().size());
//        assertEquals("Schedule 2", user.getSchedules().get(0).getScheduleName());
//        assertEquals(Semester.SPRING, user.getSchedules().get(0).getSemester());
//    }
//
//    // Test case for removeSchedule method with FALL semester
//    @Test
//    public void testRemoveSchedule_Fall() throws Exception {
//        User user = new User("username", "year", "password", false);
//        user.addSchedule("Schedule 1", Semester.FALL);
//        user.addSchedule("Schedule 2", Semester.FALL);
//        user.removeSchedule(1);
//        assertEquals(1, user.getSchedules().size());
//        assertEquals("Schedule 1", user.getSchedules().get(0).getScheduleName());
//        assertEquals(Semester.FALL, user.getSchedules().get(0).getSemester());
//    }
//
//    // Test case for removeSchedule method with SPRING semester
//    @Test
//    public void testRemoveSchedule_Spring() throws Exception {
//        User user = new User("username", "year", "password", false);
//        user.addSchedule("Schedule 1", Semester.SPRING);
//        user.addSchedule("Schedule 2", Semester.SPRING);
//        user.removeSchedule(0);
//        assertEquals(1, user.getSchedules().size());
//        assertEquals("Schedule 2", user.getSchedules().get(0).getScheduleName());
//        assertEquals(Semester.SPRING, user.getSchedules().get(0).getSemester());
//    }

}