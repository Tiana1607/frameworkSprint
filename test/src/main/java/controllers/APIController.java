package controllers;

import java.util.ArrayList;
import java.util.List;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;
import mg.itu.annotation.webapi.WebAPI;
import models.Mouvement;

@Controller
public class APIController {

    @UrlMapping("/apimouvement")
    @WebAPI
    //@WebAPI(isJSON = false)
    public List<Mouvement> testAPI() {
        List<Mouvement> mouvements = new ArrayList<>();
        mouvements.add(new Mouvement("Salaire", 1500.0));
        mouvements.add(new Mouvement("Loyer", -800.0));
        mouvements.add(new Mouvement("Courses", -150.0));

        return mouvements;
    }
}
