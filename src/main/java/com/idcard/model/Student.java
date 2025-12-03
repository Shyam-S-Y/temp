package com.idcard.model;

import java.time.LocalDate;

public class Student extends Person {

    private String studentID;
    private String major;
    private int yearOfStudy;

    public Student(String fullName, LocalDate dateOfBirth, String passportNumber,
                   String bloodGroup, String nationality, String photoPath,
                   String studentID, String major, int yearOfStudy) {

        super(fullName, dateOfBirth, passportNumber, bloodGroup, nationality, photoPath);

        this.studentID = studentID;
        this.major = major;
        this.yearOfStudy = yearOfStudy;
    }

    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    @Override
    public String getCardType() {
        return "STUDENT";
    }
}