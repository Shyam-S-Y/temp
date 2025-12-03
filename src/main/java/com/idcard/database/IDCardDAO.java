package com.idcard.database;

import com.idcard.model.Student;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IDCardDAO {
    public IDCardDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_number TEXT UNIQUE, " +
                "full_name TEXT, " +
                "date_of_birth TEXT, " +
                "passport_number TEXT, " +
                "blood_group TEXT, " +
                "nationality TEXT, " +
                "major TEXT, " +
                "year_of_study INTEGER, " +
                "photo_path TEXT, " +
                "date_of_issue TEXT, " +
                "date_of_expiry TEXT" +
                ");";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table ready.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNextSequentialNumber() {
        String sql = "SELECT COUNT(*) AS total FROM students";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total") + 1;
            }
        } catch (SQLException e) {
            System.out.println("Error getting sequential number: " + e.getMessage());
        }
        return 1;
    }

    public void addStudent(Student student) {
        String sql = "INSERT INTO students(id_number, full_name, date_of_birth, passport_number, " +
                "blood_group, nationality, major, year_of_study, photo_path, date_of_issue, date_of_expiry) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getIdNumber());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getDateOfBirth().toString());
            pstmt.setString(4, student.getPassportNumber());
            pstmt.setString(5, student.getBloodGroup());
            pstmt.setString(6, student.getNationality());
            pstmt.setString(7, student.getMajor());
            pstmt.setInt(8, student.getYearOfStudy());
            pstmt.setString(9, student.getPhotoPath());
            pstmt.setString(10, student.getDateOfIssue().toString());
            pstmt.setString(11, student.getDateOfExpiry().toString());
            pstmt.executeUpdate();
            System.out.println("Student added: " + student.getIdNumber());
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student student = new Student(  // Create variable first
                        rs.getString("full_name"),
                        LocalDate.parse(rs.getString("date_of_birth")),
                        rs.getString("passport_number"),
                        rs.getString("blood_group"),
                        rs.getString("nationality"),
                        rs.getString("photo_path"),
                        "", // studentID placeholder
                        rs.getString("major"),
                        rs.getInt("year_of_study")
                );
                student.setIdNumber(rs.getString("id_number"));
                student.setDateOfIssue(LocalDate.parse(rs.getString("date_of_issue")));
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching students: " + e.getMessage());
        }
        return students;
    }

    public Student getStudentByIdNumber(String idNumber) {
        String sql = "SELECT * FROM students WHERE id_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student(
                            rs.getString("full_name"),
                            LocalDate.parse(rs.getString("date_of_birth")),
                            rs.getString("passport_number"),
                            rs.getString("blood_group"),
                            rs.getString("nationality"),
                            rs.getString("photo_path"),
                            "", // studentID - will be set next
                            rs.getString("major"),
                            rs.getInt("year_of_study")
                    );
                    student.setIdNumber(rs.getString("id_number"));
                    student.setDateOfIssue(LocalDate.parse(rs.getString("date_of_issue")));
                    return student;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching students by department: " + e.getMessage());
        }
        return null;
    }
}
