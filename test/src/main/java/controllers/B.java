package controllers;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;

@Controller
public class B {

    @UrlMapping(value = "/blabla", method = "POST")
    public void afficher()
    {
        System.out.println("Bonjour !!!");
    }

    @UrlMapping(value = "/blabla", method = "GET")
    public void afficherAide()
    {
        System.out.println("HELPPPP !!!");
    }
}
