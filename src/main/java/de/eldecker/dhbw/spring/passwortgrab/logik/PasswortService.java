package de.eldecker.dhbw.spring.passwortgrab.logik;

import static java.lang.String.format;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.eldecker.dhbw.spring.passwortgrab.db.PasswortEntity;
import de.eldecker.dhbw.spring.passwortgrab.db.PasswortRepo;
import de.eldecker.dhbw.spring.passwortgrab.db.krypto.AesAlgorithmus;
import de.eldecker.dhbw.spring.passwortgrab.helferlein.DatumUtils;
import de.eldecker.dhbw.spring.passwortgrab.model.NutzernamePasswort;
import de.eldecker.dhbw.spring.passwortgrab.model.PasswortException;


/**
 * Klasse mit Geschäftslogik zum Anlegen/Ändern von Passwort-Records.
 */
@Service
public class PasswortService {
    
    /** Repo-Bean für Zugriff auf DB-Tabelle mit Passwörtern. */
    @Autowired
    private PasswortRepo _passwortRepo;
    
    /** Hilfs-Bean mit Funktionen zur Datumsberechnung. */
    @Autowired
    private DatumUtils _datumUtils;
    
    /** Beam für AES-Verschlüsselung, wird hier für Erzeugung Init-Vektor benötigt. */ 
    @Autowired
    private AesAlgorithmus _aesAlgo;

    
    /**
     * Neues Passwort auf Datenbank anlegen.
     * 
     * @param titel Titel des Passworts
     * 
     * @param nutzername Nutzername (wird verschlüsselt)
     * 
     * @param passwort1 Passwort (wird verschlüsselt)
     * 
     * @param passwort2 Passwort (Wiederholung, zum Vergleich mit {@code passwort1}
     * 
     * @param gueltigBis Letzter Tag, an dem das Passwort gültig ist
     * 
     * @param kommentar Kommentar (optional)
     * 
     * @return ID von neuem Passwort
     * 
     * @throws PasswortException Ungültige Daten übergeben
     */
    public long neu( String    titel     , 
                     String    nutzername,
                     String    passwort1 , 
                     String    passwort2 ,
                     LocalDate gueltigBis, 
                     String    kommentar  ) throws PasswortException {                                           
        
        titel      = titel.trim();
        nutzername = nutzername.trim();
        passwort1  = passwort1.trim();
        passwort2  = passwort2.trim();
        kommentar  = kommentar.trim();
        
        checkeAttribute( titel, nutzername, passwort1, passwort2, gueltigBis, kommentar ); // throws PasswortException
        
        String iv = _aesAlgo.erzeugeZufaelligenIV();
        
        NutzernamePasswort nutzerPasswort = new NutzernamePasswort( iv, nutzername, passwort1 ); 
        
        PasswortEntity passwort = new PasswortEntity( titel, nutzerPasswort, gueltigBis, kommentar ); 
        
        passwort = _passwortRepo.save( passwort );
        
        return passwort.getId();
    }
    
    
    /**
     * Passwort ändern.
     * 
     * @param id Primärschlüssel des Passworts
     * 
     * @param titel Titel des Passworts
     * 
     * @param nutzername Nutzername (wird verschlüsselt)
     * 
     * @param passwort1 Passwort (wird verschlüsselt)
     * 
     * @param passwort2 Passwort (Wiederholung, zum Vergleich mit {@code passwort1}
     * 
     * @param gueltigBis Letzter Tag, an dem das Passwort gültig ist
     * 
     * @param kommentar Kommentar (optional)
     * 
     * @throws PasswortException Ungültige Daten übergeben oder kein Passwort mit 
     *                           {@code id} gefunden.
     */
    public void aendern( long      id        ,
                         String    titel     , 
                         String    nutzername,
                         String    passwort1 , 
                         String    passwort2 ,
                         LocalDate gueltigBis, 
                         String    kommentar  ) throws PasswortException {     
    
        checkeAttribute( titel, nutzername, passwort1, passwort2, gueltigBis, kommentar ); // throws PasswortException
        
        Optional<PasswortEntity> passwortOptional = _passwortRepo.findById( id );
        if ( passwortOptional.isEmpty() ) {
            
            String fehlertext = format( "Kein Passwort mit ID=%d gefunden", id );
            throw new PasswortException( fehlertext );
        }
        
        PasswortEntity passwort = passwortOptional.get();
        
        passwort.setTitel(      titel      );
        passwort.setGueltigBis( gueltigBis );
        passwort.setKommentar(  kommentar  );
        
        String iv = _aesAlgo.erzeugeZufaelligenIV();
        
        NutzernamePasswort np = new NutzernamePasswort( iv, nutzername, passwort1 ); 
        passwort.setNutzernamePasswort( np );
        
        _passwortRepo.save( passwort );
    }
    
    
    /**
     * Methode überprüft Attribute für neues Passwort oder geändertes Passwort.
     * Wenn etwas nicht in Ordnung ist, dann wird eine {@link PasswortException}
     * geworfen.
     */
    private void checkeAttribute( String    titel, 
                                  String    nutzername, 
                                  String    passwort1, 
                                  String    passwort2, 
                                  LocalDate gueltigBis, 
                                  String    kommentar ) throws PasswortException {

        if ( titel.isBlank() ) { 
            
            throw new PasswortException( "Leerer Titel für neues Passwort" ); 
        }        
        if ( nutzername.isBlank() ) {
        
            throw new PasswortException( "Nutzername is leer" );
        }
        if ( passwort1.isBlank() ) {
            
            throw new PasswortException( "Passwort 1 ist leer" );
        }
        if ( passwort2.isBlank() ) {
            
            throw new PasswortException( "Passwort 2 ist leer" );
        }
        if ( passwort1.equals( passwort2 ) == false ) {
            
            throw new PasswortException( "Passwort und Wiederholung stimmen nicht überein" );
        }
        if ( _datumUtils.istInVergangenheit( gueltigBis )) {
            
            throw new PasswortException( "Gültigkeitsdatum darf nicht in Vergangenheit liegen." );
        }
    }
    
}
