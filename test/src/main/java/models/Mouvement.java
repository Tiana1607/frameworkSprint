package models;

public class Mouvement {

    private String libelle;
    private double montant;
    private String nom;

    public Mouvement() {
    }

    public Mouvement(String libelle, double montant) {
        this.libelle = libelle;
        this.montant = montant;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
