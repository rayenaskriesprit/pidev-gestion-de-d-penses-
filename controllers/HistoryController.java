package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.dao.Bookingimplt;
import org.example.dao.UserService;
import org.example.models.Booking;
import org.example.models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class HistoryController {
    @FXML
    private ListView<Booking> bookingsList; // Changed from TableView to ListView

    @FXML
    private TextField searchField; // Search field for hotel name
    @FXML
    private Button searchButton;   // Button to trigger search

    private ObservableList<Booking> originalBookings; // To store the original list of bookings

    private User loggedInUser; // Add field to store the logged-in user

    // Setter method for loggedInUser
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }
    @FXML
    public void initialize() throws SQLException {
        bookingsList.setCellFactory(param -> new ListCell<Booking>() {
            @Override
            protected void updateItem(Booking booking, boolean empty) {
                super.updateItem(booking, empty);
                if (empty || booking == null) {
                    setText(null);
                } else {
                    setText(String.format(
                            "Booking ID: %d | User: %s | Status: %s | Booking Date: %s | Total Price: %.2f\n" +
                                    "| Departure: %s | Airline: %s | Flight Price: %.2f" +
                                    "| Hotel: %s | Location: %s | Price/Night: %.2f | Rating: %.1f\n" +
                                    "Conference: %s  | Price/Day: %.2f | Event: %s | Invites: %d " +
                                    "| Type: %s | Price: %.2f ",
                            booking.getBookingId(), booking.getUserName(), booking.getStatus(), booking.getBookingDate(), booking.getPriceTotal(),
                            booking.getDepartureTime(), booking.getAirlines(), booking.getFlightPrice(),
                            booking.getHotelName(), booking.getHotelLocation(), booking.getHotelPricePerNight(), booking.getHotelRating(),
                            booking.getConferenceName(), booking.getConferencePricePerDay(), booking.getNameEvement(), booking.getNumberOfInvites(),
                            booking.getTransportType(), booking.getTransportPrice()
                    ));
                }
            }
        });

        // Only refresh if loggedInUser is already set (unlikely during initialize)

            refreshTable();

    }

    public void refreshTable() {
        Bookingimplt bookingDAO = new Bookingimplt();
        String username = (loggedInUser != null && loggedInUser.getNom() != null) ? loggedInUser.getNom() : "default_user";
        System.out.println("Filtering bookings for user: " + username); // Debug output
        List<Booking> bookings = bookingDAO.findAll();
        originalBookings = FXCollections.observableArrayList(bookings);
        bookingsList.setItems(originalBookings);
    }

    /**
     * Handles row click events in the list.
     */
    @FXML
    private void handleRowClick() {
        Booking selectedBooking = bookingsList.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            String status = selectedBooking.getStatus(); // Get the status of the selected booking

            // Check the status and show appropriate alerts
            switch (status) {
                case "payment-conformed":
                    Alert paymentConformedAlert = new Alert(Alert.AlertType.INFORMATION);
                    paymentConformedAlert.setTitle("Reservation Status");
                    paymentConformedAlert.setHeaderText("Payment Confirmed");
                    paymentConformedAlert.setContentText(
                            "This reservation has been paid. Please wait for further validation.");
                    paymentConformedAlert.showAndWait();
                    break;

                case "wait":
                    Alert waitingAlert = new Alert(Alert.AlertType.WARNING);
                    waitingAlert.setTitle("Reservation Status");
                    waitingAlert.setHeaderText("Awaiting Confirmation");
                    waitingAlert.setContentText(
                            "This reservation is still pending confirmation. Please be patient.");
                    waitingAlert.showAndWait();
                    break;

                case "vrai":
                    Alert trueAlert = new Alert(Alert.AlertType.INFORMATION);
                    trueAlert.setTitle("Reservation Status");
                    trueAlert.setHeaderText("Already Paid");
                    trueAlert.setContentText(
                            "This reservation has already been paid. No further action is required.");
                    trueAlert.showAndWait();
                    break;

                case "confirmed":
                    try {
                        // Load the payment page for confirmed bookings
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaymentPage.fxml"));
                        Parent root = loader.load();
                        PaymentController paymentController = loader.getController();
                        paymentController.setPaymentDetails(selectedBooking.getBookingId(), selectedBooking.getPriceTotal());
                        Stage stage = new Stage();
                        stage.setTitle("Process Payment");
                        stage.setScene(new Scene(root, 600, 600));
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    Alert unknownStatusAlert = new Alert(Alert.AlertType.ERROR);
                    unknownStatusAlert.setTitle("Unknown Status");
                    unknownStatusAlert.setHeaderText("Invalid Reservation Status");
                    unknownStatusAlert.setContentText(
                            "The status of this reservation is unknown: " + status);
                    unknownStatusAlert.showAndWait();
                    break;
            }
        } else {
            // Show a warning if no row is selected
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("No Selection");
            noSelectionAlert.setHeaderText("No Booking Selected");
            noSelectionAlert.setContentText("Please select a booking from the list.");
            noSelectionAlert.showAndWait();
        }
    }

    /**
     * Handles the search button click event.
     */
    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim(); // Get the search keyword from the text field

        if (keyword.isEmpty()) {
            // If the search field is empty, reset the list to show all bookings
            bookingsList.setItems(originalBookings);
        } else {
            // Filter the original list based on the hotel name
            ObservableList<Booking> filteredBookings = FXCollections.observableArrayList(
                    originalBookings.stream()
                            .filter(booking -> booking.getNameEvement().toLowerCase().contains(keyword.toLowerCase()))
                            .toList()
            );

            // Update the list with filtered results
            bookingsList.setItems(filteredBookings);

            // Show a message if no results are found
            if (filteredBookings.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Search Results");
                alert.setHeaderText(null);
                alert.setContentText("No bookings found for the event: " + keyword);
                alert.showAndWait();
            }
        }
    }

    /**
     * Navigates to another view (e.g., destination.fxml).
     */
    @FXML
    private void navigateToDestination() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/destination.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) bookingsList.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 500));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AcceuilUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) bookingsList.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 500));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
