// test/src/main/java/models/Mouvement.java
package models;

public class Mouvement {

    private String libelle;
    private double montant;

    public Mouvement(String libelle, double montant) {
        this.libelle = libelle;
        this.montant = montant;
    }

    public String getLibelle() {
        return libelle;
    }

    public double getMontant() {
        return montant;
    }
}
