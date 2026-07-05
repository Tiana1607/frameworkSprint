package controllers;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;

@Controller
public class B {

    @UrlMapping(value = "/blabla", method = "POST")
    public String afficher()
    {
        return "Bonjour !!!";
    }

    @UrlMapping(value = "/blabla", method = "GET")
    public String afficherAide()
    {
        return "HELPPPP !!!";
    }
}
