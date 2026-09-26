package controllers;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;
import mg.itu.util.ModelView;
import models.Mouvement;
import org.springframework.context.ApplicationContext;
import repository.MouvementRepository;
import java.util.List;

@Controller
public class HomeController {

    @UrlMapping(value = "/home", method = "GET")
    public ModelView index(ApplicationContext applicationContext) {
        MouvementRepository repo = applicationContext.getBean(MouvementRepository.class);
        List<Mouvement> mouvements = repo.findAll();

        ModelView mv = new ModelView("home");
        mv.addData("mouvements", mouvements);
        return mv;
    }
}
