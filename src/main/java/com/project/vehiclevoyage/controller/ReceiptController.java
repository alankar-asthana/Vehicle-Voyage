package com.project.vehiclevoyage.controller;

import com.google.gson.Gson;
import com.project.vehiclevoyage.entities.BookingDetails;
import com.project.vehiclevoyage.service.BookingDetailsService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

//@Controller
//public class ReceiptController {
//
//    @Autowired
//    BookingDetailsService bookingDetailsService;
//
//    @GetMapping("/user/download-receipt/{id}")
//    public ResponseEntity<ByteArrayResource> downloadReceipt(@PathVariable("id") String bookingDetailsId) {
//        // Fetch booking details based on ID (replace this with your actual service call)
//        BookingDetails bookingDetails = bookingDetailsService.getBookingDetailsById(bookingDetailsId);
//
//        // Generate receipt content dynamically based on booking details
//        String receiptContent = generateReceiptContent(bookingDetails);
//
//        // Convert receipt content to byte array
//        byte[] receiptBytes = receiptContent.getBytes(StandardCharsets.UTF_8);
//
//        // Create ByteArrayResource to wrap the byte array
//        ByteArrayResource resource = new ByteArrayResource(receiptBytes);
//
//        // Set headers for the response
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vehicle-receipt.pdf"); // Set the filename to "vehicle-receipt.");
//
//        // Return the response entity with the receipt data
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(resource);
//    }
//
//    // Method to generate receipt content based on booking details
//    private String generateReceiptContent(BookingDetails bookingDetails) {
//        // Implement logic to generate receipt content based on booking details
//        // Example: Concatenate booking details properties to form receipt content
//        StringBuilder receiptContent = new StringBuilder();
//        receiptContent.append("Receipt for Order ID: ").append(bookingDetails.getOrderId()).append("\n");
//        receiptContent.append("Receipt Number:").append(bookingDetails.getReceiptNumber()).append("\n");
//        receiptContent.append("User: ").append(bookingDetails.getUser().getFullName()).append("\n");
//        receiptContent.append("Vehicle: ").append(bookingDetails.getVehicle().getBrandName()).append(" ").append(bookingDetails.getVehicle().getModelName()).append("\n");
//        receiptContent.append("Registration Number: ").append(bookingDetails.getVehicle().getRegistrationNumber()).append("\n");
//        receiptContent.append("Booking Type: ").append(bookingDetails.getBookingType()).append("\n");
//        receiptContent.append("Booking Date: ").append(bookingDetails.getBookingDate()).append("\n");
//        receiptContent.append("Payment Status: ").append(bookingDetails.getPaymentStatus()).append("\n");
//        receiptContent.append("Payment Id: ").append(bookingDetails.getPaymentId()).append("\n");
//        receiptContent.append("Booking Status: ").append(bookingDetails.getBookingStatus()).append("\n");
//        receiptContent.append("Total Cost: ").append(bookingDetails.getTotalCost()).append(" INR\n");
//        // Add more properties as needed
//        return receiptContent.toString();
//    }
//}


@Controller
public class ReceiptController {

    @Autowired
    BookingDetailsService bookingDetailsService;

    @GetMapping("/user/download-receipt/{id}")
    public ResponseEntity<ByteArrayResource> downloadReceipt(@PathVariable("id") String bookingDetailsId) throws IOException {
        // Fetch booking details based on ID (replace this with your actual service call)
        BookingDetails bookingDetails = bookingDetailsService.getBookingDetailsById(bookingDetailsId);

        // Generate receipt content dynamically based on booking details
        byte[] receiptBytes = generateReceiptContent(bookingDetails);

        // Create ByteArrayResource to wrap the byte array
        ByteArrayResource resource = new ByteArrayResource(receiptBytes);

        // Set headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vehicle-receipt.pdf"); // Set the filename to "vehicle-receipt.pdf".

        // Return the response entity with the receipt data
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    // Method to generate receipt content based on booking details
    public byte[] generateReceiptContent(BookingDetails bookingDetails) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Define fonts and colors
                PDType1Font headingFont = PDType1Font.HELVETICA_BOLD;
                PDType1Font subHeadingFont = PDType1Font.HELVETICA_BOLD;
                PDType1Font dataFont = PDType1Font.HELVETICA;

                float textColorBlack = 0; // Black color for text
                float textColorGray = 0.4f; // Gray color for secondary text

                // Add receipt heading with styling
                contentStream.setFont(headingFont, 18);
                contentStream.setNonStrokingColor(textColorBlack);
                contentStream.beginText();
                contentStream.newLineAtOffset(80, 750);
                contentStream.showText("Receipt");
                contentStream.endText();

                // Add receipt details with styling and alignment
                float currentY = 700; // Starting position for details

                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Order Details:", "", currentY);

                currentY -= 30; // Adjust position for next line

                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Order ID:", bookingDetails.getOrderId(), currentY);
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Receipt Number:", bookingDetails.getReceiptNumber(), currentY - 20);
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "User:", bookingDetails.getUser().getFullName(), currentY - 40);
                String vehicleDetails = bookingDetails.getVehicle().getBrandName() + " " + bookingDetails.getVehicle().getModelName();
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Vehicle:", vehicleDetails, currentY - 60);
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Registration Number:", bookingDetails.getVehicle().getRegistrationNumber(), currentY - 80);
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Start Date:", bookingDetails.getStartDate().toString(), currentY - 100);
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "End Date:", bookingDetails.getEndDate().toString(), currentY - 120);
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Booking Type:", bookingDetails.getBookingType(), currentY - 140);
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Booking Status:", bookingDetails.getBookingStatus(), currentY - 160);
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Payment Status:", bookingDetails.getPaymentStatus(), currentY - 180);
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Payment ID:", bookingDetails.getPaymentId(), currentY - 200);
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Booking Date:", bookingDetails.getBookingDate().toString(), currentY - 220);
                addReceiptDetail(contentStream, subHeadingFont, dataFont, textColorGray, textColorBlack, "Total Cost:", bookingDetails.getTotalCost() + " INR", currentY - 240);

                contentStream.close();
            }

            // Save the document to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void addReceiptDetail(PDPageContentStream contentStream, PDType1Font subHeadingFont, PDType1Font dataFont, float textColorGray, float textColorBlack, String subHeading, String data, float yPosition) throws IOException {
        contentStream.setFont(subHeadingFont, 12);
        contentStream.setNonStrokingColor(textColorGray);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, yPosition);
        contentStream.showText(subHeading);
        contentStream.endText();

        contentStream.setFont(dataFont, 12);
        contentStream.setNonStrokingColor(textColorBlack);
        contentStream.beginText();
        contentStream.newLineAtOffset(200, yPosition);
        contentStream.showText(data);
        contentStream.endText();
    }
}