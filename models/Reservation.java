package org.example.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reservation {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty idVoyage = new SimpleIntegerProperty();
    private final IntegerProperty idEvent = new SimpleIntegerProperty();
    private final Double montant;
    private final StringProperty status = new SimpleStringProperty("non-conforme");
    private final BooleanProperty paiement = new SimpleBooleanProperty(false);

    // Constructor
    public Reservation(int idVoyage, int idEvent, double montant) {
        this.idVoyage.set(idVoyage);
        this.idEvent.set(idEvent);
        this.montant = montant;
    }

    // Getters for properties
    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public int getIdVoyage() { return idVoyage.get(); }
    public IntegerProperty idVoyageProperty() { return idVoyage; }

    public int getIdEvent() { return idEvent.get(); }
    public IntegerProperty idEventProperty() { return idEvent; }

    public double getMontant() { return montant; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }

    public boolean isPaiement() { return paiement.get(); }
    public BooleanProperty paiementProperty() { return paiement; }

    // Setters for mutable properties
    public void setId(int id) { this.id.set(id); }
    public void setStatus(String status) { this.status.set(status); }
    public void setPaiement(boolean paiement) { this.paiement.set(paiement); }



}