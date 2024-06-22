package de.eldecker.dhbw.spring.passwortgrab.web;

import static java.lang.String.format;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.eldecker.dhbw.spring.passwortgrab.db.PasswortEntity;
import de.eldecker.dhbw.spring.passwortgrab.db.PasswortRepo;
import de.eldecker.dhbw.spring.passwortgrab.helferlein.DatumUtils;
import de.eldecker.dhbw.spring.passwortgrab.logik.PasswortService;
import de.eldecker.dhbw.spring.passwortgrab.model.PasswortException;


/**
 * Controller für Thymeleaf-Template-Engine. 
 */
@Controller
@RequestMapping( "/app" )
public class ThymeleafController {
    
    private final static Logger LOG = LoggerFactory.getLogger( ThymeleafController.class );

    /** Repo-Bean für Zugriff auf Datenbanktabelle mit Passwörtern. */
    @Autowired
    private PasswortRepo _passwortRepo;
    
    /** Hilfs-Bean für Datumsberechnungen. */
    @Autowired
    private DatumUtils _datumUtils;
    
    /** Bean mit Geschäftslogik zum Anlegen/Ändern von Passwörtern. */
    @Autowired
    private PasswortService _passwortService;
    
    
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
     *         oder {@code passwort-fehler.html} ohne Datei-Endung
     */
    @GetMapping( "/passwort-details/{id}" )
    public String getPasswortDetails( Model model, 
                                      @PathVariable Long id ) {
             
        Optional<PasswortEntity> passwortOptional = _passwortRepo.findById( id );        
        if ( passwortOptional.isEmpty() ) {
            
            String fehlermeldung = format( "Kein Passwort mit ID=%d gefunden.", id );
            LOG.warn( fehlermeldung );
            model.addAttribute( "fehlermeldung", fehlermeldung );
            
            return "passwort-fehler";
            
        } else {
            
            PasswortEntity passwortEntity = passwortOptional.get();
            model.addAttribute( "passwortObjekt", passwortEntity );
        }
        
        return "passwort-details";
    }
    
    
    /**
     * Neues Passwort anlegen.
     * 
     * @param model Objekt für Platzhalterwerte in Template
     * 
     * @return Name von Template-Datei {@code passwort-editor.html}
     *         ohne Datei-Endung
     */
    @GetMapping( "/passwort-neu")
    public String editorPasswortNeu( Model model ) {
                
        model.addAttribute( "ueberschrift", "Neues Passwort anlegen" );
        
        model.addAttribute( "titel"     , "" );
        model.addAttribute( "nutzername", "" );
        model.addAttribute( "passwort1" , "" );
        model.addAttribute( "passwort2" , "" );
        model.addAttribute( "kommentar" , "" );
        
        model.addAttribute( "gueltigBis", _datumUtils.datumMorgenISO8610() );
        
        return "passwort-editor";
    }
    
    
    /**
     * Controller, der vom Formular in Template "passwort-editor" gesendete
     * Werte entgegennimmt für neues oder geändertes Passwort.
     * 
     * @param model Objekt für Platzhalterwerte in Template
     * 
     * @param titel Titel des Passwortdatensatzes, z.B. "Testnutzer für Server 123"
     * 
     * @param nutzername Nutzername für Login
     * 
     * @param passwort1 Passwort
     * 
     * @param passwort2 Wiederholung Passwort für Vergleich mit {@code passwort1}
     * 
     * @param gueltigBis Datum von letztem Tag, an dem das Passwort gültig ist
     * 
     * @param kommentar Kommentar (optional)
     * 
     * @return Name von Template-Datei {@code passwort-erfolg} ohne Datei-Endung;
     *         im Fehlerfall {@code passwort-fehler.html}
     */
    @PostMapping( "/neu_aendern")
    public String editorPost( Model model,
                              @RequestParam( "titel"      ) String    titel     ,
                              @RequestParam( "nutzername" ) String    nutzername,
                              @RequestParam( "passwort1"  ) String    passwort1 ,
                              @RequestParam( "passwort2"  ) String    passwort2 ,
                              @RequestParam( "gueltigBis" ) LocalDate gueltigBis,
                              @RequestParam( "kommentar"  ) String    kommentar
                            ) {
        try {
        
            long id = _passwortService.neuesPasswort( titel, 
                                                      nutzername, 
                                                      passwort1, passwort2, 
                                                      gueltigBis, 
                                                      kommentar ); // throws PasswortException
                        
            String erfolgsText = format( "Neues Password mit ID=%d angelegt.", id );
            LOG.info( erfolgsText );
            model.addAttribute( "meldung", erfolgsText );
            
            return "passwort-erfolg"; 
        }
        catch ( PasswortException ex ) {
            
            String fehlerText = "Fehler beim Anlegen von neuem Passwort: " + ex.getMessage();             
            LOG.error( fehlerText, ex );
            model.addAttribute( "fehlermeldung", fehlerText );
            
            return "passwort-fehler";
        }
    }
    
}
