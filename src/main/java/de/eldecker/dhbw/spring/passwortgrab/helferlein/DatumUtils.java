package de.eldecker.dhbw.spring.passwortgrab.helferlein;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.LocalDate.now;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;


/**
 * Diese Bean-Klasse enthält Hilfsfunktionen zur Arbeit mit Datumswerten
 * (für Gültigskeitsdatum von Passwörtern). 
 */
@Component
public class DatumUtils {

    /** Formatierer, um Datum nach <i>ISO 8601</i> zu formatieren, z.B. {@code 2024-01-31}. */
    private final static DateTimeFormatter FORMATIERER_ISO8601 = ofPattern( "yyyy-MM-dd" );
     
    
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
    public LocalDate erzeugeGueltigBis( int tageInZukunft ) {

        return now().plusDays( tageInZukunft );
    }
    
    
    /**
     * Datum formatieren nach <i>ISO 8601</i>.
     * 
     * @param datum Zu formatierendes Datum
     * 
     * @return Datum im Format {@code YYYY-MM-DD}, z.B. {@code 2024-01-31}
     */
    public String formatiereDatum( LocalDate datum ) {
        
        return datum.format( FORMATIERER_ISO8601 );
    }
    
    
    /**
     * Morgiges Datum nach <i>ISO 8601</i> formatiert.
     * 
     * @return Morgiges Datum im Format {@code YYYY-MM-DD}, z.B. {@code 2024-01-31}
     */
    public String datumMorgenISO8610() {
        
        LocalDate morgen = erzeugeGueltigBis( 1 );
        return formatiereDatum( morgen );
    }
    
    
    /**
     * Überprüfen, ob {@code datum} in der Vergangenheit liegt.
     * 
     * @param datum Datum, das zu überprüfen ist
     * 
     * @return {@code true} gdw. {@code datum} vor dem heutigen
     *         Datum liegt
     */
    public boolean istInVergangenheit( LocalDate datum ) {

        return now().isAfter( datum );
    }
    
}
