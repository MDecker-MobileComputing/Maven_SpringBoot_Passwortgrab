package de.eldecker.dhbw.spring.passwortgrab.model;


/**
 * Eigene applikations-spezifische Exception-Klasse (geprüfte Exception).
 */
@SuppressWarnings("serial")
public class PasswortException extends Exception {
    
    public PasswortException( String fehlertext ) {
        
        super( fehlertext );
    }

}
