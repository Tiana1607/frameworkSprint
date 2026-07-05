package controllers;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;

@Controller
public class HomeController {

    @UrlMapping(value = "/home", method = "GET")
    public void index() {
        System.out.println("GET /home appelé !");
    }

    @UrlMapping(value = "/home", method = "POST")
    public void submit() {
        System.out.println("POST /home appelé !");
    }

    @UrlMapping(value = "/about", method = "GET")
    public void about() {
        System.out.println("GET /about appelé !");
    }
}
