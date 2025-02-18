package org.example.controllers;

import java.sql.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.PaiementDAO;
import org.example.dao.ReservationImpl;
import org.example.models.Paiement;
import org.example.models.Reservation;

public class BacController {

    @FXML
    private TableView<Paiement> paymentTable;
    @FXML
    private TableColumn<Paiement, Integer> idColumn;
    @FXML
    private TableColumn<Paiement, String> nomTitulaireColumn;
    @FXML
    private TableColumn<Paiement, Integer> idReservationColumn;
    @FXML
    private TableColumn<Paiement, String> numeroCarteColumn;
    @FXML
    private TableColumn<Paiement, Date> dateExpirationColumn;
    @FXML
    private TableColumn<Paiement, Integer> cvvColumn;
    @FXML
    private TableColumn<Paiement, Double> montantColumn;

    private final PaiementDAO paiementDAO = new PaiementDAO();
    private final ReservationImpl reservationService = new ReservationImpl();

    @FXML

    private TextField searchField;

    @FXML
    private void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomTitulaireColumn.setCellValueFactory(new PropertyValueFactory<>("nomTitulaire"));
        idReservationColumn.setCellValueFactory(new PropertyValueFactory<>("idReservation"));
        numeroCarteColumn.setCellValueFactory(new PropertyValueFactory<>("numeroCarte"));
        dateExpirationColumn.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        cvvColumn.setCellValueFactory(new PropertyValueFactory<>("cvv"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));

        // Load payment data
        loadPaymentData();

        // Add search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterPayments(newValue);
        });
    }

    private void filterPayments(String query) {
        if (query == null || query.isEmpty()) {
            loadPaymentData(); // Reset the table to show all payments
        } else {
            List<Paiement> filteredPayments = paiementDAO.getPaiementsByNomTitulaire(query);
            ObservableList<Paiement> filteredObservableList = FXCollections.observableArrayList(filteredPayments);
            paymentTable.setItems(filteredObservableList);
        }
    }

    private void loadPaymentData() {
        List<Paiement> payments = paiementDAO.getAllPaiements();
        ObservableList<Paiement> paymentObservableList = FXCollections.observableArrayList(payments);
        paymentTable.setItems(paymentObservableList);
    }

    @FXML
    private void confirmPayment() {
        Paiement selected = paymentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            int reservationId = selected.getIdReservation();

            // Confirm with the user
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirmer le paiement pour cette réservation ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    // Update the reservation's paiement field to true
                    reservationService.updatePaiement(reservationId, true);

                    // Optionally, update the status of the reservation
                    reservationService.updateStatus(reservationId, "vrai");

                    // Refresh the payment table
                    loadPaymentData();

                    showInfoDialog("Succès", "Le statut de paiement a été mis à jour avec succès !");
                }
            });
        } else {
            showWarningDialog("Sélection requise", "Veuillez sélectionner un enregistrement de paiement.");
        }
    }

    private void showWarningDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}