package org.example.utils;

import org.example.models.Student;

public class Session {

    private static Student currentStudent; // can store the logged-in student object

    // Set the currently logged-in student
    public static void setCurrentStudent(Student student) {
        currentStudent = student;
    }

    // Get the currently logged-in student
    public static Student getCurrentStudent() {
        return currentStudent;
    }

    // Get the currently logged-in student ID
    public static int getCurrentStudentId() {
        if (currentStudent != null) {
            return currentStudent.getId();
        } else {
            throw new IllegalStateException("No student is currently logged in!");
        }
    }

    // Optional: clear the session on logout
    public static void clear() {
        currentStudent = null;
    }

    // Helper method to check if someone is logged in
    public static boolean isLoggedIn() {
        return currentStudent != null;
    }
}
