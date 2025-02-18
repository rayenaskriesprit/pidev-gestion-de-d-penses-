package org.example.dao;

import org.example.models.Reservation;

import java.util.List;

public interface IReservation {
    /**
     * Fetch all reservations from the database.
     *
     * @return A list of all reservations.
     */
    List<Reservation> findAll();

    /**
     * Fetch reservations with a specific status.
     *
     * @param status The status to filter by (e.g., "non-conforme", "conforme").
     * @return A list of reservations matching the specified status.
     */
    List<Reservation> findByStatus(String status);

    /**
     * Save a new reservation to the database.
     *
     * @param reservation The reservation object to save.
     */
    void saveReservation(Reservation reservation);

    /**
     * Delete a reservation by its ID.
     *
     * @param id The ID of the reservation to delete.
     */
    void deleteById(int id);

    /**
     * Update the status of a reservation.
     *
     * @param id     The ID of the reservation to update.
     * @param status The new status to set.
     */
    void updateStatus(int id, String status);

    /**
     * Update the payment status of a reservation.
     *
     * @param id       The ID of the reservation to update.
     * @param paiement The new payment status (true = paid, false = unpaid).
     */
    void updatePaiement(int id, boolean paiement);
}