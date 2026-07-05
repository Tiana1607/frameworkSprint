package controllers;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;

@Controller
public class C {

    @UrlMapping(value = "/help", method = "GET")
    public void afficher()
    {
        System.out.println("Bonjour !!!");
    }
}
