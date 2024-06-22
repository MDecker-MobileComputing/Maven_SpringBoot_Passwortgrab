package de.eldecker.dhbw.spring.passwortgrab.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Ein Objekt dieser Klasse enthält einen Nutzernamen und ein Passwort sowie
 * einen Initialisierungsvektor (IV) für die Verschlüsselung mit dem AES-Algorithmus
 * in der Betriebsart "Cipher Block Chaining (CBC)".
 * <br>
 *
 * Diese Klasse ist keine Entity-Klasse, es wird also ein {@code AttributeConverter}
 * benötigt, um ein Objekt dieser Klasse in eine Datenbank-Spalte zu schreiben und
 * wieder auszulesen.
 */
public class NutzernamePasswort {

    /** 
     * Initialisierungsvektor (128 Bit) für AES-Verschlüsselung im CBC-Modus.
     * Muss zufällig sein, muss aber nicht geheim gehalten werden. 
     */
    private String _iv;

    /** 
     * Nutzername für Anmeldung an einem System oder Web-Dienst; kann
     * auch eine Email-Adresse sein.
     */
    private String _nutzername;
    
    /** Passwort. */
    private String _passwort;

    
    /**
     * Default-Konstruktor
     */
    public NutzernamePasswort() {}
    

    /**
     * Konstruktor um Werte für alle Attribute zu setzen.
     */
    public NutzernamePasswort( String iv, String nutzername, String passwort ) {

        _iv         = iv;
        _nutzername = nutzername;
        _passwort   = passwort;
    }

    public String getIv() {

        return _iv;
    }

    public void setIv( String iv ) {

        _iv = iv;
    }

    public String getNutzername() {

        return _nutzername;
    }

    public void setNutzername( String nutzername ) {

        _nutzername = nutzername;
    }

    public String getPasswort() {

        return _passwort;
    }

    public void setPasswort( String passwort ) {

        _passwort = passwort;
    }

    /**
     * Methode zur Berechnung Informationsgehalt von Passwort.
     * <br><br>
     * 
     * siehe auch: https://onlinetexttools.com/calculate-text-entropy
     * 
     * @return Entropie in Anzahl Bits, auf ganze Zahl gerundet
     */
    public int berechnePasswortEntropie() {
        
        if ( _passwort == null || _passwort.isEmpty() ) {
            
            return 0;
        }

        Map<Character, Integer> zeichenZuFrequenz = new HashMap<>( _passwort.length() );
        for ( char c : _passwort.toCharArray() ) {
            
            zeichenZuFrequenz.put( c, zeichenZuFrequenz.getOrDefault( c, 0 ) + 1 );
        }

        double entropie = 0;
        for ( int frequenz : zeichenZuFrequenz.values() ) {
            
            double wahrscheinlichkeit = (double) frequenz / _passwort.length();
            entropie -= wahrscheinlichkeit * Math.log( wahrscheinlichkeit ) / Math.log( 2 );
        }

        return (int) entropie;
    }
    
    
    /**
     * String-Repräsentation des Objekts.
     * 
     * @return String enthält nicht Nutzername + Passwort (klar!), sondern
     *         Entropie des Passworts
     */
    @Override
    public String toString() {
        
        return "Passwort mit Entropie von " +  berechnePasswortEntropie() + " Bits";
    }

}