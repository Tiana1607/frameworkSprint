package controllers;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;

@Controller
public class C {

    @UrlMapping("/blabla2")
    public void afficher()
    {
        System.out.println("Bonjour !!!");
    }
}
