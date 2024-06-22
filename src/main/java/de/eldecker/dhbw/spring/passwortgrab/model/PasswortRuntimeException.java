package de.eldecker.dhbw.spring.passwortgrab.model;


/**
 * Eigene ungeprüfte Exception.
 */
public class PasswortRuntimeException extends RuntimeException {

    public PasswortRuntimeException( String fehlermeldung,
                                     Exception ex ) {
        super( fehlermeldung, ex );
    }

}
