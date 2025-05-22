package de.eldecker.dhbw.spring.passwortgrab.model;


/**
 * Eigene ungeprüfte Exception.
 */
@SuppressWarnings("serial")
public class PasswortRuntimeException extends RuntimeException {

    /**
     * Konstruktor um neue Exception mit Fehlerbeschreibung
     * und ursprünglicher Exception zu erzeugen. 
     * 
     * @param fehlermeldung Beschreibung Fehler
     * 
     * @param ex Ursprüngliche Exception
     */
    public PasswortRuntimeException( String fehlermeldung,
                                     Exception ex ) {
        super( fehlermeldung, ex );
    }

}
