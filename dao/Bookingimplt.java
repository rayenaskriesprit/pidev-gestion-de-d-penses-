package org.example.dao;

import org.example.dao.BookingDAO;
import org.example.dao.DBconnection;
import org.example.models.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Bookingimplt implements BookingDAO {

    // Helper method to map ResultSet to Booking object
    private Booking mapToBooking(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getInt("booking_id"),
                rs.getInt("flight_id"),
                rs.getInt("hotel_id"),
                rs.getInt("transport_id"),
                rs.getInt("conference_location_id"),
                rs.getString("user_name"),
                rs.getDate("booking_date"),
                rs.getString("status"),
                rs.getString("airlines"),
                rs.getTime("departure_time"),
                rs.getDouble("flight_price"),
                rs.getString("hotel_name"),
                rs.getString("hotel_location"),
                rs.getDouble("hotel_price_per_night"),
                rs.getDouble("hotel_rating"),
                rs.getString("conference_name"),
                rs.getDouble("conference_price_per_day"),
                rs.getString("transport_type"),
                rs.getDouble("transport_price"),
                rs.getString("transport_description"),
                rs.getDouble("priceTotal"),
                rs.getString("name_evement"),
                rs.getInt("numberof_invites"),
                rs.getTimestamp("start_evement"),
                rs.getTimestamp("end_evement"),
                rs.getInt("id_evement"),
                rs.getString("special_requests") // Added special_requests
        );
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(mapToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger here
        }
        return bookings;
    }

    @Override
    public Booking findById(int id) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToBooking(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger here
        }
        return null;
    }

    @Override
    public void save(Booking booking) {
        String sql = "INSERT INTO bookings (" +
                "flight_id, hotel_id, transport_id, conference_location_id, user_name, " +
                "booking_date, status, airlines, departure_time, flight_price, " +
                "hotel_name, hotel_location, hotel_price_per_night, hotel_rating, " +
                "conference_name, conference_price_per_day, transport_type, " +
                "transport_price, transport_description, priceTotal, name_evement, " +
                "numberof_invites, start_evement, end_evement, id_evement, special_requests" + // Added special_requests
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // Added one more placeholder
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getFlightId());
            stmt.setInt(2, booking.getHotelId());
            stmt.setInt(3, booking.getTransportId());
            stmt.setInt(4, booking.getConferenceLocationId());
            stmt.setString(5, booking.getUserName());
            stmt.setDate(6, booking.getBookingDate());
            stmt.setString(7, booking.getStatus());
            stmt.setString(8, booking.getAirlines());
            stmt.setTime(9, booking.getDepartureTime());
            stmt.setDouble(10, booking.getFlightPrice());
            stmt.setString(11, booking.getHotelName());
            stmt.setString(12, booking.getHotelLocation());
            stmt.setDouble(13, booking.getHotelPricePerNight());
            stmt.setDouble(14, booking.getHotelRating());
            stmt.setString(15, booking.getConferenceName());
            stmt.setDouble(16, booking.getConferencePricePerDay());
            stmt.setString(17, booking.getTransportType());
            stmt.setDouble(18, booking.getTransportPrice());
            stmt.setString(19, booking.getTransportDescription());
            stmt.setDouble(20, booking.getPriceTotal());
            stmt.setString(21, booking.getNameEvement());
            stmt.setInt(22, booking.getNumberOfInvites());
            stmt.setTimestamp(23, booking.getStartEvement());
            stmt.setTimestamp(24, booking.getEndEvement());
            stmt.setInt(25, booking.getIdEvement());
            stmt.setString(26, booking.getSpecialRequests()); // Set special_requests
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger here
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking ID " + id + " deleted successfully.");
            } else {
                System.out.println("No booking found with ID " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger here
        }
    }

    @Override
    public void updateStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking ID " + bookingId + " status updated to: " + status);
            } else {
                System.out.println("No booking found with ID " + bookingId);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger here
        }
    }


    public List<Booking> findByStatus(String status) {
        List<Booking> filtered = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE status = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                filtered.add(mapToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger here
        }
        return filtered;
    }


    public List<Booking> findByUser(String user_name) {
        List<Booking> filtered = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_name = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user_name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                filtered.add(mapToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger here
        }
        return filtered;
    }
    @Override
    public void updateBooking(Booking booking) {
        if (booking.getFlightId() <= 0 || booking.getHotelId() <= 0 ||
                booking.getTransportId() <= 0 || booking.getConferenceLocationId() <= 0 ||
                booking.getBookingId() <= 0) {
            throw new IllegalArgumentException("All booking component IDs must be provided");
        }

        // Removed event-related fields from the SQL update
        String sql = "UPDATE bookings SET " +
                "flight_id = ?, hotel_id = ?, transport_id = ?, conference_location_id = ?, " +
                "airlines = ?, departure_time = ?, flight_price = ?, " +
                "hotel_name = ?, hotel_location = ?, hotel_price_per_night = ?, hotel_rating = ?, " +
                "conference_name = ?, conference_price_per_day = ?, " +
                "transport_type = ?, transport_price = ?, transport_description = ?, " +
                "priceTotal = ?, special_requests = ? " +
                "WHERE booking_id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, booking.getFlightId());
            stmt.setInt(2, booking.getHotelId());
            stmt.setInt(3, booking.getTransportId());
            stmt.setInt(4, booking.getConferenceLocationId());
            stmt.setString(5, booking.getAirlines() != null ? booking.getAirlines() : "");
            stmt.setTime(6, booking.getDepartureTime());
            stmt.setDouble(7, booking.getFlightPrice());
            stmt.setString(8, booking.getHotelName() != null ? booking.getHotelName() : "");
            stmt.setString(9, booking.getHotelLocation() != null ? booking.getHotelLocation() : "");
            stmt.setDouble(10, booking.getHotelPricePerNight());
            stmt.setDouble(11, booking.getHotelRating());
            stmt.setString(12, booking.getConferenceName() != null ? booking.getConferenceName() : "");
            stmt.setDouble(13, booking.getConferencePricePerDay());
            stmt.setString(14, booking.getTransportType() != null ? booking.getTransportType() : "");
            stmt.setDouble(15, booking.getTransportPrice());
            stmt.setString(16, booking.getTransportDescription() != null ?
                    booking.getTransportDescription() : "");
            stmt.setDouble(17, booking.getPriceTotal());
            stmt.setString(18, booking.getSpecialRequests() != null ?
                    booking.getSpecialRequests() : "");
            stmt.setInt(19, booking.getBookingId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No booking found with ID: " + booking.getBookingId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update booking: " + e.getMessage(), e);
        }
    }
}
