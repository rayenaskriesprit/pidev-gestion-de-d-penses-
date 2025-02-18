package org.example.dao;

import org.example.models.Paiement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAO {

    // CREATE: Insert a new payment record
    public void createPaiement(String nomTitulaire, int idReservation, String numeroCarte, Date dateExpiration, int cvv, double montant) {
        String sql = "INSERT INTO paiment (nom_titulaire, id_reservation, numero_carte, date_expiration, cvv, montant) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomTitulaire);
            pstmt.setInt(2, idReservation);
            pstmt.setString(3, numeroCarte);
            pstmt.setDate(4, dateExpiration);
            pstmt.setInt(5, cvv);
            pstmt.setDouble(6, montant);
            pstmt.executeUpdate();
            System.out.println("Payment created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Paiement> getPaiementsByNomTitulaire(String nomTitulaire) {
        // Implement a database query to filter payments by nomTitulaire
        // Example SQL: SELECT * FROM paiement WHERE nom_titulaire LIKE '%?%'
        return getAllPaiements().stream()
                .filter(p -> p.getNomTitulaire().toLowerCase().contains(nomTitulaire.toLowerCase()))
                .toList();
    }
    // READ: Retrieve all payment records
    public List<Paiement> getAllPaiements() {
        String sql = "SELECT * FROM paiment";
        List<Paiement> paiements = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Paiement paiement = new Paiement(
                        rs.getInt("id"),
                        rs.getString("nom_titulaire"),
                        rs.getInt("id_reservation"),
                        rs.getString("numero_carte"),
                        rs.getDate("date_expiration"),
                        rs.getInt("cvv"),
                        rs.getDouble("montant")
                );
                paiements.add(paiement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    // UPDATE: Update an existing payment record
    public void updatePaiement(int id, String nomTitulaire, int idReservation, String numeroCarte, Date dateExpiration, int cvv, double montant) {
        String sql = "UPDATE paiment SET nom_titulaire = ?, id_reservation = ?, numero_carte = ?, date_expiration = ?, cvv = ?, montant = ? WHERE id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomTitulaire);
            pstmt.setInt(2, idReservation);
            pstmt.setString(3, numeroCarte);
            pstmt.setDate(4, dateExpiration);
            pstmt.setInt(5, cvv);
            pstmt.setDouble(6, montant);
            pstmt.setInt(7, id);
            pstmt.executeUpdate();
            System.out.println("Payment updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE: Delete a payment record by ID
    public void deletePaiement(int id) {
        String sql = "DELETE FROM paiment WHERE id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Payment deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}