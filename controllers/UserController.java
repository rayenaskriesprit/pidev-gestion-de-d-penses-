package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.dao.ReservationImpl;
import org.example.models.Reservation;

import java.io.IOException;
import java.util.List;

public class UserController {
    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private TableColumn<Reservation, Integer> idColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;
    @FXML
    private Button paymentButton;
    @FXML
    private Button confirmPaymentButton;

    private final ReservationImpl reservationService = new ReservationImpl();

    @FXML
    private void initialize() {
        // Associer les colonnes aux propriétés de l'objet Reservation
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        List<Reservation> reservations = reservationService.findByStatus("conforme");
        ObservableList<Reservation> data = FXCollections.observableArrayList(reservations);
        reservationTable.setItems(data);
        // Charger les réservations conformes
        loadReservations();
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationService.findAll(); // Fetch all reservations
        reservationTable.getItems().setAll(reservations);
    }


    @FXML

    private void navigateToPayment() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();

        // Check if a reservation is selected
        if (selected == null) {
            showWarningDialog("Sélection requise", "Veuillez sélectionner une réservation.");
            return;
        }

        // Check if the reservation is already paid
        if ("vrai".equals(selected.getStatus())) {
            showWarningDialog("Paiement existant", "Cette réservation est déjà payée.");
            return;
        }

        try {
            // Load the PaymentPage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaymentPage.fxml"));
            Parent root = loader.load();
            PaymentController controller = loader.getController();

            // Pass the reservation ID to the PaymentController
            controller.setIdReservation(selected.getId());

            // Create and display the payment stage
            Stage stage = new Stage();
            stage.setTitle("Payment Page");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();
        } catch (IOException e) {
            // Handle IOException and show an error dialog
            showErrorDialog("Erreur de chargement", "Impossible d'ouvrir la page de paiement.");
            e.printStackTrace();
        }
    }

    @FXML
    private void confirmPayment() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            selected.setStatus("vrai");
            reservationService.update(selected); // Mise à jour dans la base de données
            loadReservations(); // Recharger les données après modification
            showInfoDialog("Paiement confirmé", "Le statut de paiement a été mis à jour avec succès !");
        } else {
            showWarningDialog("Sélection requise", "Veuillez sélectionner une réservation.");
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
