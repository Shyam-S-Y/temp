package com.idcard.database;

import com.idcard.model.Student;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class DatabaseTest {
    public static void main(String[] args) {
        IDCardDAO dao = new IDCardDAO();

        // Clear table for fresh run
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM students");
            System.out.println("Database cleared for fresh test run.");
        } catch (SQLException e) {
            System.out.println("Error clearing table: " + e.getMessage());
        }

        // Create test students
        Student alice = new Student(
                "Alice Johnson",
                LocalDate.of(2002, 5, 15),
                "P12345678",
                "O+",
                "American",
                "photos/alice.jpg",
                "",  // studentID - will be generated
                "Electronics and Computer Engineering",
                2
        );
        alice.setIdNumber("2025ACPS0001U");

        Student bob = new Student(
                "Bob Smith",
                LocalDate.of(2003, 8, 22),
                "P87654321",
                "A+",
                "British",
                "photos/bob.jpg",
                "",
                "Computer Science",
                1
        );
        bob.setIdNumber("2025A7PS0002U");

        // Add to database
        dao.addStudent(alice);
        dao.addStudent(bob);

        // List all students
        List<Student> allStudents = dao.getAllStudents();
        System.out.println("\nAll Students:");
        allStudents.forEach(s -> System.out.println(s.getIdNumber() + " | " + s.getFullName() + " | " + s.getMajor()));

        // Close connection
        DatabaseConnection.closeConnection();
    }
}