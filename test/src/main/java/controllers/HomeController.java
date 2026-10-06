package controllers;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;
import mg.itu.annotation.webapi.WebAPI;
import mg.itu.util.ModelView;
import models.Mouvement;

import java.util.ArrayList;
import java.util.List;

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
}
