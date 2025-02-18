/*package org.example.controllers;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.dao.PaiementDAO;
import org.example.models.Paiement;

public class PaymentController {

    @FXML
    private TextField nomTitulaireField;
    @FXML
    private TextField numeroCarteField;
    @FXML
    private TextField dateExpirationField;
    @FXML
    private TextField cvvField;
    @FXML
    private TextField montantField;

    private int idReservation; // To store the reservation ID passed from UserController

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    @FXML
    private void savePayment() {
        // Retrieve input values
        String nomTitulaire = nomTitulaireField.getText().trim();
        String numeroCarte = numeroCarteField.getText().trim();
        String dateExpirationStr = dateExpirationField.getText().trim();
        String cvvStr = cvvField.getText().trim();
        String montantStr = montantField.getText().trim();

        // Validate inputs
        if (nomTitulaire.isEmpty() || numeroCarte.isEmpty() || dateExpirationStr.isEmpty() || cvvStr.isEmpty() || montantStr.isEmpty()) {
            showWarningDialog("Champs requis", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            LocalDate dateExpiration = LocalDate.parse(dateExpirationStr);
            int cvv = Integer.parseInt(cvvStr);
            double montant = Double.parseDouble(montantStr);

            // Create Paiement object
            Paiement paiement = new Paiement(
                    0, // ID is auto-generated
                    nomTitulaire,
                    idReservation,
                    numeroCarte,
                    Date.valueOf(dateExpiration),
                    cvv,
                    montant
            );

            // Save payment to database
            PaiementDAO paiementDAO = new PaiementDAO();
            paiementDAO.createPaiement(
                    paiement.getNomTitulaire(),
                    paiement.getIdReservation(),
                    paiement.getNumeroCarte(),
                    paiement.getDateExpiration(),
                    paiement.getCvv(),
                    paiement.getMontant()
            );

            showInfoDialog("Succès", "Le paiement a été enregistré avec succès !");
        } catch (Exception e) {
            showErrorDialog("Erreur", "Une erreur s'est produite lors de l'enregistrement du paiement.");
            e.printStackTrace();
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

}*/
package org.example.controllers;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.dao.ReservationImpl;
import org.example.dao.PaiementDAO;
import org.example.models.Paiement;

public class PaymentController {

    @FXML
    private TextField nomTitulaireField;
    @FXML
    private TextField numeroCarteField;
    @FXML
    private TextField dateExpirationField;
    @FXML
    private TextField cvvField;

    private int idReservation; // To store the reservation ID passed from UserController

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    @FXML
    private void savePayment() {
        // Retrieve input values
        String nomTitulaire = nomTitulaireField.getText().trim();
        String numeroCarte = numeroCarteField.getText().trim();
        String dateExpirationStr = dateExpirationField.getText().trim();
        String cvvStr = cvvField.getText().trim();

        // Validate inputs
        if (nomTitulaire.isEmpty() || numeroCarte.isEmpty() || dateExpirationStr.isEmpty() || cvvStr.isEmpty()) {
            showWarningDialog("Champs requis", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Parse expiration date and CVV
            LocalDate dateExpiration = LocalDate.parse(dateExpirationStr);
            int cvv = Integer.parseInt(cvvStr);

            // Retrieve montant from the reservation table
            ReservationImpl reservationDAO = new ReservationImpl();
            double montant = reservationDAO.getMontantById(idReservation);

            if (montant <= 0) {
                showWarningDialog("Montant invalide", "Le montant pour cette réservation est incorrect ou manquant.");
                return;
            }

            // Create Paiement object
            Paiement paiement = new Paiement(
                    0, // ID is auto-generated
                    nomTitulaire,
                    idReservation,
                    numeroCarte,
                    Date.valueOf(dateExpiration),
                    cvv,
                    montant
            );

            // Save payment to database
            PaiementDAO paiementDAO = new PaiementDAO();
            paiementDAO.createPaiement(
                    paiement.getNomTitulaire(),
                    paiement.getIdReservation(),
                    paiement.getNumeroCarte(),
                    paiement.getDateExpiration(),
                    paiement.getCvv(),
                    paiement.getMontant()
            );

            // Update reservation status and paiement flag


            showInfoDialog("Succès", "Le paiement a été enregistré avec succès !");
        } catch (Exception e) {
            showErrorDialog("Erreur", "Une erreur s'est produite lors de l'enregistrement du paiement.");
            e.printStackTrace();
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