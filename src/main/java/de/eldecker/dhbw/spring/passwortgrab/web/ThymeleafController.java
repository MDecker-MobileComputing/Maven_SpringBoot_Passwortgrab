package de.eldecker.dhbw.spring.passwortgrab.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.eldecker.dhbw.spring.passwortgrab.db.PasswortEntity;
import de.eldecker.dhbw.spring.passwortgrab.db.PasswortRepo;



/**
 * Controller für Thymeleaf-Template-Engine. 
 */
@Controller
@RequestMapping( "/app" )
public class ThymeleafController {

    /** Repo-Bean für Zugriff auf Datenbanktabelle mit Passwörtern.. */
    @Autowired
    private PasswortRepo _passwortRepo;
    
    
    /**
     * Liste der Passwörter (nur Titel) zurückgeben.
     * 
     * @param model Objekt für Platzhalterwerte in Template
     * 
     * @return Name von Template-Datei {@code passwort-liste.html} ohne Datei-Endung 
     */
    @GetMapping( "/passwort-liste" )
    public String abfrageKfzKennzeichen( Model model ) {
        
        List<PasswortEntity>  passwortListe = _passwortRepo.findAllSortedByTitle();
        
        model.addAttribute( "passwortListe", passwortListe );
        
        return "passwort-liste";
    }
    
}
