package de.eldecker.dhbw.spring.passwortgrab.logik;

import static java.time.LocalDateTime.now;

import de.eldecker.dhbw.spring.passwortgrab.db.PasswortEntity;
import de.eldecker.dhbw.spring.passwortgrab.db.PasswortRepo;
import de.eldecker.dhbw.spring.passwortgrab.db.krypto.AesAlgorithmus;
import de.eldecker.dhbw.spring.passwortgrab.model.NutzernamePasswort;

import java.util.List;
import java.time.LocalDateTime;

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

            LOG.info( "Es sind schon {} Passwörter in der Datenbank, deshalb kein Import von Demo-Daten." );

        } else {

            LOG.info( "Noch keine Passwörter in der Datenbank, importiere Demo-Daten." );

            NutzernamePasswort np1 = new NutzernamePasswort( _aes.erzeugeZufaelligenIV(), "support-user", "geheim123" );
            NutzernamePasswort np2 = new NutzernamePasswort( _aes.erzeugeZufaelligenIV(), "test-123"    , "secret789" );

            PasswortEntity p1 = new PasswortEntity( "Testnutzer für Fileserver 123", np1, erzeugeGueltigBis( 10*24 ), "Von Datenimporter angelegt" );
            PasswortEntity p2 = new PasswortEntity( "Testnutzer für Fileserver 234", np2, erzeugeGueltigBis( 48    ), "Von Datenimporter angelegt" );

            List passwortListe = List.of( p1, p2 );
            _passwortRepo.saveAll( passwortListe );

            final long anzahlPasswoerterNeu = _passwortRepo.count();
            LOG.info( "Demo-Daten wurden importiert, es sind jetzt {} Passwörter in der Datenbank.", anzahlPasswoerterNeu );
        }
    }


    /**
     * Zeitpunkt in Zukunft erzeugen.
     *
     * @param stundenInZukunft Anzahl Stunden in der Zukunft
     *
     * @return Gültigkeitszeitpunkt, der {@code stundenInZukunft}
     *         Stunden in der Zukunft liegt.
     */
    private LocalDateTime erzeugeGueltigBis( int stundenInZukunft ) {

        return now().plusHours( stundenInZukunft );
    }

}