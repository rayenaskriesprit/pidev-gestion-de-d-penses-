package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.ReservationImpl;
import org.example.models.Reservation;

import java.util.List;

public class AdminController {

    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private TableColumn<Reservation, Integer> idColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;

    private ReservationImpl reservationService = new ReservationImpl();

    @FXML
    private void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load non-conforming reservations
        List<Reservation> reservations = reservationService.findByStatus("non-conforme");
        ObservableList<Reservation> data = FXCollections.observableArrayList(reservations);
        reservationTable.setItems(data);
    }

    @FXML
    private void approveReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            reservationService.updateStatus(selected.getId(), "conforme");
            reservationTable.getItems().remove(selected);
            showInfoAlert("Success", "Reservation approved successfully.");
        } else {
            showErrorAlert("Error", "Please select a reservation to approve.");
        }
    }

    @FXML
    private void deleteReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            reservationService.deleteById(selected.getId());
            reservationTable.getItems().remove(selected);
            showInfoAlert("Success", "Reservation deleted successfully.");
        } else {
            showErrorAlert("Error", "Please select a reservation to delete.");
        }
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}