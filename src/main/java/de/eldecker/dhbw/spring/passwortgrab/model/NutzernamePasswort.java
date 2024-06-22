package de.eldecker.dhbw.spring.passwortgrab.model;


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
    private String nutzername;
    
    /** Passwort. */
    private String passwort;

    
    /**
     * Default-Konstruktor
     */
    public NutzernamePasswort() {}
    

    /**
     * Konstruktor um Werte für alle Attribute zu setzen.
     */
    public NutzernamePasswort( String iv, String nutzername, String passwort ) {

        this._iv = iv;
        this.nutzername = nutzername;
        this.passwort = passwort;
    }

    public String getIv() {

        return _iv;
    }

    public void setIv( String iv ) {

        this._iv = iv;
    }

    public String getNutzername() {

        return nutzername;
    }

    public void setNutzername( String nutzername ) {

        this.nutzername = nutzername;
    }

    public String getPasswort() {

        return passwort;
    }

    public void setPasswort( String passwort ) {

        this.passwort = passwort;
    }

    public int berechnePasswortEntropie() {

        if (passwort == null || passwort.isEmpty()) {

            return 0;
        }
    
        long chars = passwort.chars().distinct().count();
        return (int) ( Math.log( chars ) / Math.log( 2 ) * passwort.length() );
    }
    
    @Override
    public String toString() {
        
        return "Passwort mit Entropie von " +  berechnePasswortEntropie() + " Bits";
    }

}