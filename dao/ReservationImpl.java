package org.example.dao;

import org.example.models.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationImpl implements IReservation {

    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public List<Reservation> findByStatus(String status) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE status = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public void saveReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (id_voyage, id_event, montant, status, paiement) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservation.getIdVoyage());
            pstmt.setInt(2, reservation.getIdEvent());
            pstmt.setDouble(3, reservation.getMontant());
            pstmt.setString(4, reservation.getStatus());
            pstmt.setBoolean(5, reservation.isPaiement());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM reservation WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStatus(int id, String status) {
        String sql = "UPDATE reservation SET status = ? WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePaiement(int id, boolean paiement) {
        String sql = "UPDATE reservation SET paiement = ? WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, paiement);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update(Reservation reservation) {
        String query = "UPDATE reservations SET status = ? WHERE id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, reservation.getStatus());
            preparedStatement.setInt(2, reservation.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double getMontantById(int idReservation) {
        String query = "SELECT montant FROM reservation WHERE id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idReservation);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("montant");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Return 0.0 if no record is found
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation(
                rs.getInt("id_voyage"),
                rs.getInt("id_event"),
                rs.getDouble("montant")
        );
        reservation.setId(rs.getInt("id"));
        reservation.setStatus(rs.getString("status"));
        reservation.setPaiement(rs.getBoolean("paiement"));
        return reservation;
    }
}
