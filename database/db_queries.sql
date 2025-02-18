DROP DATABASE IF EXISTS reservation;
CREATE DATABASE reservation;

USE reservation;
CREATE TABLE reservation (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             nom_user VARCHAR(50),
                             id_voyage INT,
                             id_event INT,
                             montant REAL,
                             status VARCHAR(50) DEFAULT 'non-conforme',
                             paiement BOOLEAN DEFAULT FALSE
);
CREATE TABLE paiment (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nom_titulaire VARCHAR(100) NOT NULL,
                         id_reservation INT NOT NULL,
                         numero_carte VARCHAR(16) NOT NULL,
                         date_expiration DATE NOT NULL,
                         cvv INT NOT NULL,
                         montant DECIMAL(10, 2) NOT NULL,
                         FOREIGN KEY (id_reservation) REFERENCES reservation(id)
);



