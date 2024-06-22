package de.eldecker.dhbw.spring.passwortgrab.web;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import de.eldecker.dhbw.spring.passwortgrab.db.PasswortEntity;
import de.eldecker.dhbw.spring.passwortgrab.db.PasswortRepo;


/**
 * Controller für Thymeleaf-Template-Engine. 
 */
@Controller
@RequestMapping( "/app" )
public class ThymeleafController {
    
    private final static Logger LOG = LoggerFactory.getLogger( ThymeleafController.class );

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
    public String getPasswortListe( Model model ) {
        
        List<PasswortEntity>  passwortListe = _passwortRepo.findAllSortedByTitle();
        
        model.addAttribute( "passwortListe", passwortListe );
        
        return "passwort-liste";
    }
    
    
    /**
     * Details für ein Passwort-Datensatz anzeigen.
     * 
     * @param model Objekt für Platzhalterwerte in Template
     * 
     * @param id Pfadparameter mit ID des Datensatzes, der angezeigt werden soll 
     * 
     * @return Name von Template-Datei {@code passwort-details.html} 
     *         doer {@code passwort-fehler.html} ohne Datei-Endung
     */
    @GetMapping( "/passwort-details/{id}" )
    public String getPasswortDetails( Model model, 
                                      @PathVariable Long id ) {
             
        Optional<PasswortEntity> passwortOptional = _passwortRepo.findById( id );        
        if ( passwortOptional.isEmpty() ) {
            
            String fehlermeldung = String.format( "Kein Passwort mit ID=%d gefunden.", id );
            LOG.warn( fehlermeldung );
            model.addAttribute( "fehlermeldung", fehlermeldung );
            
            return "passwort-fehler";
            
        } else {
            
            PasswortEntity passwortEntity = passwortOptional.get();
            model.addAttribute( "passwortObjekt", passwortEntity );
        }
        
        return "passwort-details";
    }
    
}
