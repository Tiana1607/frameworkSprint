package controllers;

import java.io.File;
import java.io.FileOutputStream;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;
import mg.itu.util.MapFile;
import mg.itu.util.ModelView;
import models.Fichier;

import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;
import mg.itu.util.MapFile;
import mg.itu.util.ModelView;
import models.Fichier;
import org.springframework.context.ApplicationContext;
import repository.FichierRepository;

@Controller
public class FormController {

    // @UrlMapping(value = "/upload", method = "POST")
    // public ModelView upload(MapFile fichier) {
    // try {
    // // Dossier de stockage
    // String uploadDir =
    // "/home/rovatians/Documents/ITU/S4/web_dynamique/framework/sprint/uploads";

    // // Crée le dossier si absent
    // new File(uploadDir).mkdirs();

    // // Écrit le fichier sur le disque
    // File fichierDestination = new File(uploadDir, fichier.getFilename());
    // try (FileOutputStream fos = new FileOutputStream(fichierDestination)) {
    // fos.write(fichier.getBytes());
    // }

    // ModelView mv = new ModelView("home");
    // mv.addData("message", "Fichier uploadé : " + fichier.getFilename());
    // return mv;

    // } catch (Exception e) {
    // ModelView mv = new ModelView("home");
    // mv.addData("message", "Erreur : " + e.getMessage());
    // return mv;
    // }
    // }

    @UrlMapping(value = "/upload", method = "POST")
    public ModelView upload(MapFile fichier, ApplicationContext applicationContext) {
        FichierRepository fichierRepository = applicationContext.getBean(FichierRepository.class);

        Fichier entity = new Fichier();
        entity.setFilename(fichier.getFilename());
        entity.setExtension(fichier.getExtension());
        entity.setSize(fichier.getSize());
        entity.setContent(fichier.getBytes());
        fichierRepository.save(entity);

        ModelView mv = new ModelView("home");
        mv.addData("message", "Fichier sauvegardé en BDD !");
        return mv;
    }
}
