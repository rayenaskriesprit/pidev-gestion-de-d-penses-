package org.example.models;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Booking {
    private int bookingId;
    private int flightId;
    private int hotelId;
    private int transportId;
    private int conferenceLocationId;
    private String userName;
    private Date bookingDate;
    private String status;
    private String airlines;
    private Time departureTime;
    private double flightPrice;
    private String hotelName;
    private String hotelLocation;
    private double hotelPricePerNight;
    private double hotelRating;
    private String conferenceName;
    private double conferencePricePerDay;
    private String transportType;
    private double transportPrice;
    private String transportDescription;
    private double priceTotal;
    private String nameEvement;
    private int numberOfInvites;
    private Timestamp startEvement;
    private Timestamp endEvement;
    private int idEvement;
    private String specialRequests; // New field for Special Requests

    // Default Constructor
    public Booking() {}

    // Parameterized Constructor
    public Booking(int bookingId, int flightId, int hotelId, int transportId, int conferenceLocationId,
                   String userName, Date bookingDate, String status, String airlines, Time departureTime,
                   double flightPrice, String hotelName, String hotelLocation, double hotelPricePerNight,
                   double hotelRating, String conferenceName, double conferencePricePerDay, String transportType,
                   double transportPrice, String transportDescription, double priceTotal, String nameEvement,
                   int numberOfInvites, Timestamp startEvement, Timestamp endEvement, int idEvement, String specialRequests) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.hotelId = hotelId;
        this.transportId = transportId;
        this.conferenceLocationId = conferenceLocationId;
        this.userName = userName;
        this.bookingDate = bookingDate;
        this.status = status;
        this.airlines = airlines;
        this.departureTime = departureTime;
        this.flightPrice = flightPrice;
        this.hotelName = hotelName;
        this.hotelLocation = hotelLocation;
        this.hotelPricePerNight = hotelPricePerNight;
        this.hotelRating = hotelRating;
        this.conferenceName = conferenceName;
        this.conferencePricePerDay = conferencePricePerDay;
        this.transportType = transportType;
        this.transportPrice = transportPrice;
        this.transportDescription = transportDescription;
        this.priceTotal = priceTotal;
        this.nameEvement = nameEvement;
        this.numberOfInvites = numberOfInvites;
        this.startEvement = startEvement;
        this.endEvement = endEvement;
        this.idEvement = idEvement;
        this.specialRequests = specialRequests; // Initialize special requests
    }

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getFlightId() { return flightId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }

    public int getHotelId() { return hotelId; }
    public void setHotelId(int hotelId) { this.hotelId = hotelId; }

    public int getTransportId() { return transportId; }
    public void setTransportId(int transportId) { this.transportId = transportId; }

    public int getConferenceLocationId() { return conferenceLocationId; }
    public void setConferenceLocationId(int conferenceLocationId) { this.conferenceLocationId = conferenceLocationId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAirlines() { return airlines; }
    public void setAirlines(String airlines) { this.airlines = airlines; }

    public Time getDepartureTime() { return departureTime; }
    public void setDepartureTime(Time departureTime) { this.departureTime = departureTime; }

    public double getFlightPrice() { return flightPrice; }
    public void setFlightPrice(double flightPrice) { this.flightPrice = flightPrice; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public String getHotelLocation() { return hotelLocation; }
    public void setHotelLocation(String hotelLocation) { this.hotelLocation = hotelLocation; }

    public double getHotelPricePerNight() { return hotelPricePerNight; }
    public void setHotelPricePerNight(double hotelPricePerNight) { this.hotelPricePerNight = hotelPricePerNight; }

    public double getHotelRating() { return hotelRating; }
    public void setHotelRating(double hotelRating) { this.hotelRating = hotelRating; }

    public String getConferenceName() { return conferenceName; }
    public void setConferenceName(String conferenceName) { this.conferenceName = conferenceName; }

    public double getConferencePricePerDay() { return conferencePricePerDay; }
    public void setConferencePricePerDay(double conferencePricePerDay) { this.conferencePricePerDay = conferencePricePerDay; }

    public String getTransportType() { return transportType; }
    public void setTransportType(String transportType) { this.transportType = transportType; }

    public double getTransportPrice() { return transportPrice; }
    public void setTransportPrice(double transportPrice) { this.transportPrice = transportPrice; }

    public String getTransportDescription() { return transportDescription; }
    public void setTransportDescription(String transportDescription) { this.transportDescription = transportDescription; }

    public double getPriceTotal() { return priceTotal; }
    public void setPriceTotal(double priceTotal) { this.priceTotal = priceTotal; }

    public String getNameEvement() { return nameEvement; }
    public void setNameEvement(String nameEvement) { this.nameEvement = nameEvement; }

    public int getNumberOfInvites() { return numberOfInvites; }
    public void setNumberOfInvites(int numberOfInvites) { this.numberOfInvites = numberOfInvites; }

    public Timestamp getStartEvement() { return startEvement; }
    public void setStartEvement(Timestamp startEvement) { this.startEvement = startEvement; }

    public Timestamp getEndEvement() { return endEvement; }
    public void setEndEvement(Timestamp endEvement) { this.endEvement = endEvement; }

    public int getIdEvement() { return idEvement; }
    public void setIdEvement(int idEvement) { this.idEvement = idEvement; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}
