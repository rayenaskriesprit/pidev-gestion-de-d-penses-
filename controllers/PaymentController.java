package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.dao.Bookingimplt;
import org.example.dao.PaiementDAO;
import org.example.models.Booking;
import org.example.models.Paiement;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import com.stripe.exception.StripeException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.sql.Date;
import java.util.Properties;
import org.example.dao.PaymentService;
import javafx.scene.control.Label;

public class PaymentController {

    @FXML
    private TextField nomTitulaireField;

    @FXML
    private TextField numeroCarteField;

    @FXML
    private TextField expiryMonthField;

    @FXML
    private TextField expiryYearField;

    @FXML
    private TextField cvvField;
    @FXML
    private TextField emailField;

    private HistoryController historyController;
    private int bookingId;
    private double priceTotal;

    // Method to set booking ID and priceTotal
    public void setPaymentDetails(int bookingId, double priceTotal) {
        this.bookingId = bookingId;
        this.priceTotal = priceTotal;
    }

    @FXML
    private void savePayment() {
        String nomTitulaire = nomTitulaireField.getText();
        String numeroCarte = numeroCarteField.getText();
        String expiryMonth = expiryMonthField.getText();
        String expiryYear = expiryYearField.getText();
        String cvv = cvvField.getText();
        String email = emailField.getText();


        if (nomTitulaire.isEmpty() || numeroCarte.isEmpty() || expiryMonth.isEmpty() || expiryYear.isEmpty() || cvv.isEmpty() || email.isEmpty()) {
            showAlert("Error", "Please fill all payment details.", Alert.AlertType.ERROR);
            return;
        }


        int parsedCvv;
        try {
            parsedCvv = Integer.parseInt(cvv);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid CVV. Please enter a numeric value.", Alert.AlertType.ERROR);
            return;
        }

        if (!numeroCarte.equals("4242424242424242") || !expiryMonth.equals("03") || !expiryYear.equals("2026")) {
            showAlert("Error", "Invalid card details. Please check the card number and expiry date.", Alert.AlertType.ERROR);
            return;
        }


        System.out.println("Processing payment for Booking ID: " + bookingId);
        System.out.println("Card Holder: " + nomTitulaire);
        System.out.println("Card Number: " + numeroCarte);

        System.out.println("CVV: " + cvv);


        try {
            String chargeId = PaymentService.processPayment(priceTotal, nomTitulaire, email, "tok_visa");
            System.out.println("Payment successful: " + chargeId);
        } catch (StripeException e) {
            showAlert("Payment Error", "Payment failed: " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        }


        Bookingimplt bookingDAO = new Bookingimplt();
        bookingDAO.updateStatus(bookingId, "payment_confirmed");

        if (historyController != null) {
            historyController.refreshTable();
        } else {
            System.out.println("HistoryController reference is null.");
        }

        sendPaymentConfirmationEmail(nomTitulaire, bookingId, priceTotal, numeroCarte, expiryMonth, expiryYear, parsedCvv, email);

        showAlert("Payment Successful",
                "Your payment has been successfully processed. A receipt has been sent to your email.",
                Alert.AlertType.INFORMATION);

        nomTitulaireField.getScene().getWindow().hide();
    }

    private void sendPaymentConfirmationEmail(String cardHolderName, int bookingId, double amount, String cardNumber, String expiryMonth, String expiryYear, int cvv, String email) {
        String from = "anaskhelifi94@gmail.com";
        String to = email;
        String host = "smtp.gmail.com";
        final String username = "anaskhelifi94@gmail.com";
        final String password = "qvgi gphb niyo xlox";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            String pdfFilePath = generatePaymentReceipt(cardHolderName, bookingId, amount, cardNumber, expiryMonth, expiryYear, cvv);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Payment Confirmation - Booking ID: " + bookingId);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Dear " + cardHolderName + ",\n\n" +
                    "Your payment of $" + amount + " for Booking ID: " + bookingId + " has been successfully processed.\n" +
                    "Please find your receipt attached.\n\n" +
                    "Best regards,\nYour Booking Team");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(pdfFilePath));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Payment confirmation email with PDF sent successfully to " + to);

            new File(pdfFilePath).delete();
        } catch (Exception e) {
            System.out.println("Failed to send email with PDF: " + e.getMessage());
            showAlert("Email Error", "Failed to send payment confirmation email with receipt.", Alert.AlertType.WARNING);
        }
    }


    private String generatePaymentReceipt(String cardHolderName, int bookingId, double amount, String cardNumber, String expiryMonth, String expiryYear, int cvv) throws Exception {
        String filePath = "reçu de paiement" + bookingId + ".pdf";

        // Fetch booking details
        Bookingimplt bookingDAO = new Bookingimplt();
        Booking booking = bookingDAO.findById(bookingId);
        String nameEvenement = (booking != null && booking.getNameEvement() != null) ? booking.getNameEvement() : "N/A";
        double transportPrice = (booking != null && booking.getTransportPrice() != 0.0) ? booking.getTransportPrice() : 0;
        double hotelPrice = (booking != null && booking.getHotelPricePerNight() != 0.0) ? booking.getHotelPricePerNight() : 0;
        double locationPrice = (booking != null && booking.getConferencePricePerDay() != 0.0) ? booking.getConferencePricePerDay() : 0;
        double flightPrice = (booking != null && booking.getFlightPrice() != 0.0) ? booking.getFlightPrice() : 0;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Header
                contentStream.setLineWidth(1f);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 780);
                contentStream.showText("Payment Receipt");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 760);
                contentStream.showText("Booking ID: " + bookingId);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Date: " + new java.util.Date().toString());
                contentStream.endText();

                // Company Info (Right-aligned)
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(350, 780);
                contentStream.showText("Your Booking Team");
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Email: support@bookingteam.com");
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Phone: +1-800-555-1234");
                contentStream.endText();

                // Horizontal Line
                contentStream.moveTo(50, 740);
                contentStream.lineTo(550, 740);
                contentStream.stroke();

                // Customer and Payment Details
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 710);
                contentStream.showText("Customer Information");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 690);
                contentStream.showText("Card Holder: " + cardHolderName);
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(300, 710);
                contentStream.showText("Payment Details");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(300, 690);
                contentStream.showText("Card Number: ****-****-****-" + cardNumber.substring(cardNumber.length() - 4));
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Month: " + expiryMonth);
                contentStream.showText("Year: " + expiryYear);

                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("CVV: " + cvv);
                contentStream.endText();

                // Horizontal Line
                contentStream.moveTo(50, 660);
                contentStream.lineTo(550, 660);
                contentStream.stroke();

                // Booking Details Table
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 630);
                contentStream.showText("Booking Details");
                contentStream.endText();

                // Table Headers
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 610);
                contentStream.showText("Description");
                contentStream.newLineAtOffset(300, 0);
                contentStream.showText("Amount");
                contentStream.endText();

                // Table Border
                contentStream.setLineWidth(0.5f);
                contentStream.moveTo(50, 620);
                contentStream.lineTo(550, 620);
                contentStream.lineTo(550, 500);
                contentStream.lineTo(50, 500);
                contentStream.lineTo(50, 620);
                contentStream.stroke();

                // Vertical Separator
                contentStream.moveTo(350, 620);
                contentStream.lineTo(350, 500);
                contentStream.stroke();

                // Table Content
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                float yPosition = 595;
                contentStream.beginText();
                contentStream.newLineAtOffset(55, yPosition);
                contentStream.showText("Event Name: " + nameEvenement);
                contentStream.newLineAtOffset(295, 0);
                contentStream.showText("$" + amount);
                contentStream.newLineAtOffset(-295, -20);
                contentStream.showText("Flight Price");
                contentStream.newLineAtOffset(295, 0);
                contentStream.showText("$" + flightPrice);
                contentStream.newLineAtOffset(-295, -20);
                contentStream.showText("Hotel Price Per Night");
                contentStream.newLineAtOffset(295, 0);
                contentStream.showText("$" + hotelPrice);
                contentStream.newLineAtOffset(-295, -20);
                contentStream.showText("Conference Price Per Day");
                contentStream.newLineAtOffset(295, 0);
                contentStream.showText("$" + locationPrice);
                contentStream.newLineAtOffset(-295, -20);
                contentStream.showText("Transport Price");
                contentStream.newLineAtOffset(295, 0);
                contentStream.showText("$" + transportPrice);
                contentStream.endText();

                // Total
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(350, 480);
                contentStream.showText("Total Amount: $" + amount);
                contentStream.endText();

                // Footer
                contentStream.setFont(PDType1Font.HELVETICA, 8);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 30);
                contentStream.showText("Thank you for choosing Your Booking Team! For inquiries, contact us at support@bookingteam.com.");
                contentStream.endText();
            }

            document.save(filePath);
        }

        return filePath;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setHistoryController(HistoryController historyController) {
        this.historyController = historyController;
    }
}
