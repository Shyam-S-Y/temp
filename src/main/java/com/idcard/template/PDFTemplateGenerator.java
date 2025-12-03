package com.idcard.template;

import com.idcard.model.Student;
import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class PDFTemplateGenerator {

    // ID Card Dimensions in Points (1 mm = 2.83465 points)
    // 85mm x 53mm
    private static final float CARD_WIDTH = 241f;
    private static final float CARD_HEIGHT = 150f;

    public void generateIDCard(Student student, String destPath) throws Exception {
        PdfWriter writer = new PdfWriter(destPath);
        PdfDocument pdf = new PdfDocument(writer);

        // Set page size to ID card dimensions
        PageSize pageSize = new PageSize(CARD_WIDTH, CARD_HEIGHT);
        pdf.setDefaultPageSize(pageSize);

        Document document = new Document(pdf, pageSize);
        document.setMargins(0, 0, 0, 0); // No margins for background

        // Load Font (Helvetica Bold as Arial Bold substitute)
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // --- 1. Background Image (Front) ---
        String templatePath = getClass().getResource("/id_card_template_front.jpg").toExternalForm();
        ImageData templateData = ImageDataFactory.create(templatePath);
        Image templateImage = new Image(templateData);
        templateImage.scaleToFit(CARD_WIDTH, CARD_HEIGHT);
        templateImage.setFixedPosition(0, 0);
        document.add(templateImage);

        // --- 2. Content Overlay ---
        // We use absolute positioning to place text over the image placeholders.

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String issueDate = student.getDateOfIssue().format(formatter);
        String expiryDate = student.getDateOfExpiry().format(formatter);

        // Date of Issue (Top Center-Left)
        addTextAt(document, issueDate, 111.32f, 106.72f, 6.5f, 0.1f, boldFont);

        // Valid Upto (Top Right)
        addTextAt(document, expiryDate, 192.87f, 106.72f, 6.5f, 0.1f, boldFont);

        // Full Name (Center, Large, Title Case)
        String titleCaseName = toTitleCase(student.getFullName());
        Paragraph namePara = new Paragraph(titleCaseName)
                .setFont(boldFont)
                .setFontSize(8)
                .setCharacterSpacing(0.05f)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT)
                .setFixedPosition(65.50f, 81.25f, 160); // x, y, width
        document.add(namePara);

        // ID NO (Middle Left)
        addTextAt(document, student.getIdNumber(), 90.49f, 59.11f, 6.5f, 0.1f, boldFont);

        // Date of Birth (Middle Right)
        addTextAt(document, student.getDateOfBirth().format(formatter), 197.55f, 59.11f, 6.5f, 0.1f, boldFont);

        // Passport No (Below ID)
        addTextAt(document, student.getPassportNumber(), 109.34f, 44.08f, 6.5f, 0.1f, boldFont);

        // Blood Group (Below DOB)
        addTextAt(document, student.getBloodGroup(), 195.39f, 44.08f, 6.5f, 0.1f, boldFont);

        // Nationality (Bottom Left)
        addTextAt(document, student.getNationality(), 104.30f, 28.30f, 6.5f, 0.1f, boldFont);

        // --- 3. Student Photo ---
        // Left box. Approx x=5, y=35, w=50, h=60
        String photoPath = student.getPhotoPath();
        if (photoPath != null && new File(photoPath).exists()) {
            ImageData photoData = ImageDataFactory.create(photoPath);
            Image studentPhoto = new Image(photoData);
            studentPhoto.scaleToFit(50, 60);
            studentPhoto.setFixedPosition(10, 30);
            document.add(studentPhoto);
        }

        // --- 4. Barcode ---
        // Bottom Right box.
        if (student.getIdNumber() != null && !student.getIdNumber().isEmpty()) {
            Barcode128 barcode = new Barcode128(pdf);
            barcode.setCode(student.getIdNumber());
            barcode.setCodeType(Barcode128.CODE128);
            barcode.setFont(null);

            Image barcodeImage = new Image(barcode.createFormXObject(pdf));
            barcodeImage.scaleToFit(100, 20);
            barcodeImage.setFixedPosition(100, 3.12f);
            document.add(barcodeImage);
        }

        // --- 5. Back Side ---
        document.add(new com.itextpdf.layout.element.AreaBreak());

        try {
            String backTemplatePath = getClass().getResource("/id_card_template_back.jpg").toExternalForm();
            ImageData backTemplateData = ImageDataFactory.create(backTemplatePath);
            Image backTemplateImage = new Image(backTemplateData);
            backTemplateImage.scaleToFit(CARD_WIDTH, CARD_HEIGHT);
            backTemplateImage.setFixedPosition(2, 0, 0); // Page 2, x=0, y=0
            document.add(backTemplateImage);
        } catch (Exception e) {
            System.out.println("Back template not found, skipping back side.");
        }

        document.close();
        System.out.println("PDF Created: " + destPath);
    }

    private void addTextAt(Document doc, String text, float x, float y, float fontSize, float charSpacing,
            PdfFont font) {
        if (text == null)
            return;
        Paragraph p = new Paragraph(text)
                .setFont(font)
                .setFontSize(fontSize)
                .setCharacterSpacing(charSpacing)
                .setFixedPosition(x, y, 100); // Width 100
        doc.add(p);
    }

    private String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            } else {
                c = Character.toLowerCase(c);
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }
}
