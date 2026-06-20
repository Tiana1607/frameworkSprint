package controllers;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;

@Controller
public class B {

    @UrlMapping("/blabla")
    public void afficher()
    {
        System.out.println("Bonjour !!!");
    }

    @UrlMapping("/help")
    public void afficherAide()
    {
        System.out.println("HELPPPP !!!");
    }
}
