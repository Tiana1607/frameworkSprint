package controllers;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;

@Controller
public class HomeController {

    @UrlMapping(value = "/home", method = "GET")
    public String index() {
       return "GET /home appelé !";
    }

    @UrlMapping(value = "/home", method = "POST")
    public String submit() {
       return "POST /home appelé !";
    }

    @UrlMapping(value = "/about", method = "GET")
    public String about() {
       return "GET /about appelé !";
    }
}
