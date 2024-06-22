package de.eldecker.dhbw.spring.passwortgrab.web;

import static java.lang.String.format;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.eldecker.dhbw.spring.passwortgrab.db.PasswortEntity;
import de.eldecker.dhbw.spring.passwortgrab.db.PasswortRepo;
import de.eldecker.dhbw.spring.passwortgrab.db.PasswortRevisionsRepo;
import de.eldecker.dhbw.spring.passwortgrab.helferlein.DatumUtils;
import de.eldecker.dhbw.spring.passwortgrab.logik.PasswortService;
import de.eldecker.dhbw.spring.passwortgrab.model.NutzernamePasswort;
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
    
    @Autowired
    private PasswortRevisionsRepo _revisionsRepo;
    
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
     * Webseite mit Editor für neuen Passwortdatensatz anzeigen. 
     * 
     * @param model Objekt für Platzhalterwerte in Template
     * 
     * @return Name von Template-Datei {@code passwort-editor.html}
     *         ohne Datei-Endung
     */
    @GetMapping( "/passwort-neu")
    public String editorPasswortNeu( Model model ) {
                
        model.addAttribute( "ueberschrift", "Neues Passwort anlegen" );
        
        model.addAttribute( "id"        , "-1" );
        model.addAttribute( "titel"     , ""   );
        model.addAttribute( "nutzername", ""   );
        model.addAttribute( "passwort1" , ""   );
        model.addAttribute( "passwort2" , ""   );
        model.addAttribute( "kommentar" , ""   );
        
        
        model.addAttribute( "gueltigBis", _datumUtils.datumMorgenISO8610() );
        
        return "passwort-editor";
    }
    
    
    /**
     * Webseite mit Editor für Änderung von Passwort anzeigen.
     * 
     * @param model Objekt für Platzhalterwerte in Template
     * 
     * @param id ID von zu änderndem Passwort
     * 
     * @return Name von Template-Datei {@code passwort-editor.html}
     *         ohne Datei-Endung; {code passwort-fehler.html}, 
     *         wenn kein Passwort mit {@code id} gefunden wird.
     */
    @GetMapping( "/passwort-aendern/{id}")
    public String editorPasswortNeu( Model model,
                                     @PathVariable Long id ) {
        
        Optional<PasswortEntity> passwortOptional = _passwortRepo.findById( id );
        if ( passwortOptional.isEmpty() ) {
            
            String fehlerText = format( "Kein Passwort mit ID=%d gefunden", id );       
            LOG.error( fehlerText );
            model.addAttribute( "fehlermeldung", fehlerText );            
            return "passwort-fehler";
        }
                            
        model.addAttribute( "ueberschrift", "Passwort ändern" );
        
        PasswortEntity passwort = passwortOptional.get();        
        
        model.addAttribute( "id"        , id                       );
        model.addAttribute( "titel"     , passwort.getTitel()      );
        model.addAttribute( "kommentar" , passwort.getKommentar()  );
        model.addAttribute( "gueltigBis", passwort.getGueltigBis() );
        
        NutzernamePasswort nutzerPasswort = passwort.getNutzernamePasswort();
        
        model.addAttribute( "nutzername", nutzerPasswort.getNutzername() );
        model.addAttribute( "passwort1" , nutzerPasswort.getPasswort()   );
        model.addAttribute( "passwort2" , nutzerPasswort.getPasswort()   );
                 
        return "passwort-editor";
    }    
    
    
    /**
     * Controller, der vom Formular in Template "passwort-editor" gesendete
     * Werte entgegennimmt für neues oder geändertes Passwort.
     * 
     * @param model Objekt für Platzhalterwerte in Template
     * 
     * @param id ID des zu ändernden Passworts oder {@code -1} für neues Passwort  
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
                              @RequestParam( "id"         ) long      id        ,
                              @RequestParam( "titel"      ) String    titel     ,
                              @RequestParam( "nutzername" ) String    nutzername,
                              @RequestParam( "passwort1"  ) String    passwort1 ,
                              @RequestParam( "passwort2"  ) String    passwort2 ,
                              @RequestParam( "gueltigBis" ) LocalDate gueltigBis,
                              @RequestParam( "kommentar"  ) String    kommentar
                            ) {
        
        String erfolgsText = "";
        
        if ( id > 0 ) { // Passwort ändern
            
            try {

                _passwortService.aendern( id,
                                          titel,
                                          nutzername, 
                                          passwort1, passwort2, 
                                          gueltigBis, 
                                          kommentar );
                
                erfolgsText = format( "Passwort mit ID=%d geändert: %d", id );
            }
            catch ( PasswortException ex ) {
                
                String fehlerText = "Fehler beim Ändern von Passwort: " + ex.getMessage();             
                LOG.error( fehlerText, ex );
                model.addAttribute( "fehlermeldung", fehlerText );                
                return "passwort-fehler";                
            }
                                      
        } else { // neues Passwort

            try {

                long idNeu = _passwortService.neu( titel, 
                                                   nutzername, 
                                                   passwort1, passwort2, 
                                                   gueltigBis, 
                                                   kommentar );

                erfolgsText = format( "Neues Passwort mit ID=%d angelegt.", idNeu );
            }
            catch ( PasswortException ex ) {
                
                String fehlerText = "Fehler beim Anlegen von neuem Passwort: " + ex.getMessage();             
                LOG.error( fehlerText, ex );
                model.addAttribute( "fehlermeldung", fehlerText );                
                return "passwort-fehler";
            }                
        }
        
        LOG.info( erfolgsText );
        model.addAttribute( "meldung", erfolgsText );                                 
        
        return "passwort-erfolg";            
    }
    
    
    /**
     * Revisionen für ein Passwort anzeigen.
     * 
     * @param model Objekt für Platzhalterwerte in Template
     * 
     * @param id Primärschlüssel des Passworts
     * 
     * @return Name von Template-Datei {@code passwort-historie} ohne Datei-Endung.
     */
    @GetMapping( "/passwort-historie/{id}" )
    public String getPasswortHistorie( Model model,
                                       @PathVariable Long id ) {
    
        Revisions<Integer, PasswortEntity> revisions = _revisionsRepo.findRevisions( id );
                        
        if ( revisions.isEmpty() ) {
            
            String fehlerText = format( "Historie für ungültige Passwort-ID %d abgefragt.", id );
            LOG.warn( fehlerText );
            model.addAttribute( "fehlermeldung", fehlerText );
            return "passwort-fehler";
        }
        
        model.addAttribute( "anzahlRevisionen", revisions.getContent().size() );
        
        return "passwort-historie";
    }
    
}
