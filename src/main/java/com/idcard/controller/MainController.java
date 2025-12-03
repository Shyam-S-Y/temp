package com.idcard.controller;

import com.idcard.model.Student;
import com.idcard.database.IDCardDAO;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.idcard.photo.PhotoResizer;
import java.io.File;

public class MainController {

    private ExecutorService executorService;

    public MainController() {
        this.executorService = Executors.newFixedThreadPool(3);
    }

    public void generateIDCard(Student student, String photoPath) {
        executorService.submit(() -> {
            try {
                // Step 1: Validate student data
                validateStudentData(student);

                // Step 2: Process photo (resize, crop)
                String processedPhotoPath = processPhoto(photoPath);
                student.setPhotoPath(processedPhotoPath);

                // Step 3: Get next student sequence number from database
                int sequenceNumber = getNextStudentSequence();

                // Step 4: Generate ID number
                String idNumber = generateIDNumber(student, sequenceNumber);
                student.setIdNumber(idNumber);

                // Step 5: Save to database
                saveToDatabase(student);

                // Step 6: Generate ID card template
                generateTemplate(student);

                System.out.println("ID Card generated successfully for: " + student.getFullName());

            } catch (ValidationException e) {
                System.err.println("Validation Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error generating ID card: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void validateStudentData(Student student) throws ValidationException {
        if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
            throw new ValidationException("Full name is required");
        }
        if (student.getDateOfBirth() == null) {
            throw new ValidationException("Date of birth is required");
        }
        if (student.getPassportNumber() == null || student.getPassportNumber().trim().isEmpty()) {
            throw new ValidationException("Passport number is required");
        }
        if (student.getBloodGroup() == null || student.getBloodGroup().trim().isEmpty()) {
            throw new ValidationException("Blood group is required");
        }
        if (student.getNationality() == null || student.getNationality().trim().isEmpty()) {
            throw new ValidationException("Nationality is required");
        }
    }

    private String getMajorCode(String major) {

        switch (major.toUpperCase()) {
            case "CHEMICAL":
            case "CHEMICAL ENGINEERING":
                return "A1";
            case "CIVIL":
            case "CIVIL ENGINEERING":
                return "A2";
            case "ELECTRICAL AND ELECTRONICS":
            case "ELECTRICAL AND ELECTRONICS ENGINEERING":
            case "EEE":
                return "A3";
            case "MECHANICAL":
            case "MECHANICAL ENGINEERING":
                return "A4";
            case "COMPUTER SCIENCE":
            case "CS":
                return "A7";
            case "ELECTRONICS AND COMMUNICATION":
            case "ECE":
                return "AA";
            case "BIOTECH":
            case "BIOTECHNOLOGY":
                return "AB";
            case "ELECTRONICS AND COMPUTER":
            case "ELECTRONICS AND COMPUTER ENGINEERING":
            case "ECOM":
                return "AC";
            case "MATHEMATICS AND COMPUTING":
            case "MATH AND COMPUTING":
                return "AD";
            case "ARCHITECTURAL ENGINEERING":
            case "ARCHITECTURE":
                return "AE";
            default:
                return "XX";
        }
    }

    private String generateIDNumber(Student student, int studentSequenceNumber) {
        String year = String.valueOf(java.time.Year.now().getValue());
        String majorCode = getMajorCode(student.getMajor());
        String fixedMiddle = "PS";
        String sequenceNumber = String.format("%04d", studentSequenceNumber);
        String fixedSuffix = "U";

        return year + majorCode + fixedMiddle + sequenceNumber + fixedSuffix;
    }

    private int getNextStudentSequence() {
        // Call the database to get the actual sequence number
        IDCardDAO dao = new IDCardDAO();
        return dao.getNextSequentialNumber();
    }

    private String processPhoto(String photoPath) {
        try {
            PhotoResizer resizer = new PhotoResizer();
            File inputPhoto = new File(photoPath);

            // Resize photo to fit ID card dimensions (e.g., 300x400 pixels)
            String outputPath = "processed_photos/" + inputPhoto.getName();
            File processedPhoto = resizer.resizeToFit(inputPhoto, 300, 400, outputPath);

            if (processedPhoto != null) {
                System.out.println("Photo processed successfully: " + outputPath);
                return processedPhoto.getAbsolutePath();
            } else {
                System.out.println("Photo processing failed, using original");
                return photoPath;
            }
        } catch (Exception e) {
            System.out.println("Error processing photo: " + e.getMessage());
            return photoPath; // Return original if processing fails
        }
    }

    private void saveToDatabase(Student student) {
        IDCardDAO dao = new IDCardDAO();
        dao.addStudent(student);
        System.out.println("Saving to database: " + student.getFullName());
    }

    /**
     * Generate ID card template
     */
    private void generateTemplate(Student student) {
        try {
            com.idcard.template.PDFTemplateGenerator generator = new com.idcard.template.PDFTemplateGenerator();

            // Ensure directory exists
            File dir = new File("generated_cards");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = "generated_cards/" + student.getIdNumber() + "_"
                    + student.getFullName().replaceAll("\\s+", "_") + ".pdf";
            generator.generateIDCard(student, filename);

            System.out.println("ID Card PDF generated at: " + filename);
        } catch (Exception e) {
            System.err.println("Error generating PDF template: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}