package com.idcard.model;

import java.time.LocalDate;

public abstract class Person {

    private String fullName;
    private LocalDate dateOfBirth;
    private String passportNumber;
    private String bloodGroup;
    private String nationality;

    private String idNumber; // Will be generated
    private LocalDate dateOfIssue;
    private LocalDate dateOfExpiry; // Issue date + 4 years

    private String photoPath;

    public Person(String fullName, LocalDate dateOfBirth, String passportNumber,
            String bloodGroup, String nationality, String photoPath) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.passportNumber = passportNumber;
        this.bloodGroup = bloodGroup;
        this.nationality = nationality;
        this.photoPath = photoPath;
        this.dateOfIssue = LocalDate.now();
        this.dateOfExpiry = this.dateOfIssue.plusYears(4).minusDays(1); // Auto-calculate expiry
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public LocalDate getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(LocalDate dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
        this.dateOfExpiry = dateOfIssue.plusYears(4).minusDays(1); // Auto-update expiry
    }

    public LocalDate getDateOfExpiry() {
        return dateOfExpiry;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public abstract String getCardType();
}