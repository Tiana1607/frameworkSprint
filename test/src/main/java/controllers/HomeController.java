package controllers;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;
import mg.itu.annotation.webapi.WebAPI;
import mg.itu.util.ModelView;
import models.Mouvement;

import java.util.ArrayList;
import java.util.List;

import models.User;

@Controller
public class HomeController {

    @UrlMapping(value = "/home", method = "GET")
    public ModelView index() {
        List<Mouvement> mouvements = new ArrayList<>();
        mouvements.add(new Mouvement("Salaire", 1500.0));
        mouvements.add(new Mouvement("Loyer", -800.0));
        mouvements.add(new Mouvement("Courses", -150.0));

        ModelView mv = new ModelView("home");
        mv.addData("mouvements", mouvements);
        return mv;
    }

    @UrlMapping(value = "/ajouter", method = "POST")
    public ModelView ajouter(String libelle, double montant) {
        System.out.println("Ajout : " + libelle + " / " + montant);

        ModelView mv = new ModelView("home");
        mv.addData("message", "Ajouté : " + libelle + " - " + montant);
        return mv;
    }

    @UrlMapping(value = "/ajouterObjet", method = "POST")
    public ModelView ajouterObjet(Mouvement mouvement, User u) {
        System.out.println(mouvement.getLibelle());
        System.out.println(mouvement.getMontant());
        System.out.println(u.getNom());
        System.out.println(u.getAge());

        ModelView mv = new ModelView("home");
        mv.addData(
                "message",
                "Mouvement " + mouvement.getNom() + " ajouté : " + mouvement.getLibelle()
                + " - " + mouvement.getMontant() +
                " par : " + u.getNom() + " d'âge : " +
                u.getAge()
        );
        return mv;
    }
}
