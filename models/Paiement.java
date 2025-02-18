package org.example.models;



import java.sql.Date;

public class Paiement {
    private int id;
    private String nomTitulaire;
    private int idReservation;
    private String numeroCarte;
    private Date dateExpiration;
    private int cvv;
    private double montant;

    // Constructor
    public Paiement(int id, String nomTitulaire, int idReservation, String numeroCarte, Date dateExpiration, int cvv, double montant) {
        this.id = id;
        this.nomTitulaire = nomTitulaire;
        this.idReservation = idReservation;
        this.numeroCarte = numeroCarte;
        this.dateExpiration = dateExpiration;
        this.cvv = cvv;
        this.montant = montant;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomTitulaire() {
        return nomTitulaire;
    }

    public void setNomTitulaire(String nomTitulaire) {
        this.nomTitulaire = nomTitulaire;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public String getNumeroCarte() {
        return numeroCarte;
    }

    public void setNumeroCarte(String numeroCarte) {
        this.numeroCarte = numeroCarte;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "id=" + id +
                ", nomTitulaire='" + nomTitulaire + '\'' +
                ", idReservation=" + idReservation +
                ", numeroCarte='" + numeroCarte + '\'' +
                ", dateExpiration=" + dateExpiration +
                ", cvv=" + cvv +
                ", montant=" + montant +
                '}';
    }
}