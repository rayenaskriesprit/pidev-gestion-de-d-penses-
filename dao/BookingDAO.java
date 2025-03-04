package org.example.dao;

import org.example.models.Booking;

import java.util.List;

public interface BookingDAO {
    List<Booking> findAll();
    Booking findById(int id);
    void save(Booking booking);
    void deleteById(int id);
    void updateStatus(int bookingId, String status); // New method to update status
    void updateBooking(Booking booking); // Added updateBooking method to interface
}
