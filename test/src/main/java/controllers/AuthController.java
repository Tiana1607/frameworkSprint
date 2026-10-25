package controllers;

import mg.itu.annotation.auth.Authenticated;
import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;
import mg.itu.util.ModelView;
import mg.itu.util.MySession;

@Controller
public class AuthController {

    @UrlMapping(value = "/login", method = "POST")
    public ModelView login(String nom, MySession session) {
        session.set("user", nom);
        ModelView mv = new ModelView("profil");
        mv.addData("message", "Connecté : " + nom);
        return mv;
    }

    @UrlMapping(value = "/profil", method = "GET")
    public ModelView profil(MySession session) {
        ModelView mv = new ModelView("profil");
        if (session.has("user")) {
            mv.addData("message", "Bonjour " + session.get("user"));
        } else {
            mv.addData("message", "Personne n'est connecté");
        }
        return mv;
    }

    @UrlMapping(value = "/logout", method = "GET")
    public ModelView logout(MySession session) {
        session.invalidate();
        ModelView mv = new ModelView("profil");
        mv.addData("message", "Déconnecté");
        return mv;
    }

    @UrlMapping(value = "/secret", method = "GET")
    @Authenticated
    public ModelView secret(MySession session) {
        ModelView mv = new ModelView("profil");
        mv.addData("message", "Zone secrète, bienvenue " + session.get("user"));
        return mv;
    }
}