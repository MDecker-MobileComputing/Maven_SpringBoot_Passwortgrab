package de.eldecker.dhbw.spring.passwortgrab.logik;

import static java.time.LocalDate.now;

import de.eldecker.dhbw.spring.passwortgrab.db.PasswortEntity;
import de.eldecker.dhbw.spring.passwortgrab.db.PasswortRepo;
import de.eldecker.dhbw.spring.passwortgrab.db.krypto.AesAlgorithmus;
import de.eldecker.dhbw.spring.passwortgrab.model.NutzernamePasswort;

import java.util.List;
import java.time.LocalDate;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Diese Bean importiert bei Bedarf Demo-Daten in die Datenbank.
 */
@Component
public class DemoDatenImporter implements ApplicationRunner {

    private final static Logger LOG = LoggerFactory.getLogger( DemoDatenImporter.class );

    /** Zähler für Anzahl der erzeugten Passwörter. */
    private static int PASSWORT_ZAEHLER = 1; 
   
    /** Repo-Bean für Zugriff auf Tabelle mit Passwörtern. */
    @Autowired
    private PasswortRepo _passwortRepo;

    /** Bean mit Methoden für Ver- und Entschlüsselung mit AES. */
    @Autowired
    private AesAlgorithmus _aes;


    /**
     * Diese Methode wird unmittelbar nach Start der Anwendung aufgerufen. Wenn
     * die Tabelle mit den Passwörtern noch ganz leer ist, dann werden Demo-Daten
     * importiert.
     */
    @Override
    public void run( ApplicationArguments args ) throws Exception {

        final long anzahlPasswoerterAlt = _passwortRepo.count();
        if ( anzahlPasswoerterAlt > 0 ) {

            LOG.info( "Es sind schon {} Passwörter in der Datenbank, deshalb kein Import von Demo-Daten.",
                      anzahlPasswoerterAlt );
        } else {

            LOG.info( "Noch keine Passwörter in der Datenbank, importiere Demo-Daten." );
                        
            NutzernamePasswort np1 = erzeugePasswortNutzername( "admin"        , "geheim-123"    );
            NutzernamePasswort np2 = erzeugePasswortNutzername( "admin"        , "geheim-123"    ); // selber Nutzer + Passwort, aber anderer IV
            NutzernamePasswort np3 = erzeugePasswortNutzername( "test-nutzer-1", "secret-789"    );
            NutzernamePasswort np4 = erzeugePasswortNutzername( "herbert"      , "geheim+secret" );

            PasswortEntity p1 = erzeugePasswortEntity( "Admin für Datei-Server F1"      , np1, 10 ); 
            PasswortEntity p2 = erzeugePasswortEntity( "Admin für Datei-Server F2"      , np2, 10 ); // selber Nutzername + Passwort wie p1         
            PasswortEntity p3 = erzeugePasswortEntity( "Testnutzer für Web-Server TEST3", np3,  0 ); 
            PasswortEntity p4 = erzeugePasswortEntity( "Mail-Server"                    , np4, -3 ); // schon abgelaufen

            List<PasswortEntity> passwortListe = List.of( p1, p2, p3, p4 );
            _passwortRepo.saveAll( passwortListe );

            final long anzahlPasswoerterNeu = _passwortRepo.count();
            LOG.info( "Demo-Daten wurden importiert, es sind jetzt {} Passwörter in der Datenbank.", anzahlPasswoerterNeu );
        }
    }
    
    
    /**
     * Hilfsmethode, um {@link NutzernamePasswort} mit zufällig erzeugtem Initialsierungsvektor
     * zu erzeugen.
     * 
     * @param nutzername Nutzername
     * 
     * @param passwort Passwort
     * 
     * @return Objekt befüllt mit {@code nutzername} und {@code passwort} sowie zufällig
     *         erzeugtem Initialisierungsvektor
     */
    private NutzernamePasswort erzeugePasswortNutzername( String nutzername, String passwort ) {
        
        String iv = _aes.erzeugeZufaelligenIV();
        
        return new NutzernamePasswort( iv, nutzername, passwort );
    }

    
    /**
     * Hilfsmethode zum Erzeugen neues {@link PasswortEntity}-Objekt.
     * 
     * @param titel Titel des Datensatz
     * 
     * @param np Nutzername und Passwort
     * 
     * @param anzahlTage Anzahl Tage in Zukunft oder (bei negativem Vorzeichen)
     *                   in Vergangenheit.
     * 
     * @return Objekt mit allen Feldern bis auf ID gefüllt
     */
    private PasswortEntity erzeugePasswortEntity( String titel, NutzernamePasswort np, int anzahlTage ) {
        
        LocalDate letzterGueltigkeitsTag = erzeugeGueltigBis( anzahlTage );
        
        String kommentar = "Von Datenimporter angelegt, Nr. " + PASSWORT_ZAEHLER;
        PASSWORT_ZAEHLER++;
        
        return new PasswortEntity( titel, np, letzterGueltigkeitsTag, kommentar );
    }

    
    /**
     * Zeitpunkt in Zukunft oder Vergangenheit erzeugen.
     *
     * @param tageInZukunft Anzahl Tage in der Zukunft; wenn negativ,
     *                      dann Datum in der Vergangenheit
     *
     * @return Gültigkeitszeitpunkt, der {@code stundenInZukunft}
     *         Stunden in der Zukunft liegt; auf volle Sekunden
     *         gerundet.
     */
    private LocalDate erzeugeGueltigBis( int tageInZukunft ) {

        return now().plusDays( tageInZukunft );
    }

}