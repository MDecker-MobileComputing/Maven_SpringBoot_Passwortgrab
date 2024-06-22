package de.eldecker.dhbw.spring.passwortgrab.model;


/**
 * Ein Objekt dieser Klasse enthält einen Nutzernamen und ein Passwort sowie
 * einen Initialisierungsvektor (IV) für die Verschlüsselung mit dem AES-Algorithmus
 * in der Betriebsart "Cipher Block Chaining (CBC)".
 *
 * Diese Klasse ist keine Entity-Klasse, es wird also ein {@code AttributeConverter}
 * benötigt, um ein Objekt dieser Klasse in eine Datenbank-Spalte zu schreiben und
 * wieder auszulesen.
 */
public class NutzernamePasswort {

    private String iv;
    private String nutzername;
    private String passwort;

    // Default constructor
    public NutzernamePasswort() {

    }

    // Constructor with all parameters
    public NutzernamePasswort( String iv, String nutzername, String passwort ) {

        this.iv = iv;
        this.nutzername = nutzername;
        this.passwort = passwort;
    }

    public String getIv() {

        return iv;
    }

    public void setIv( String iv ) {

        this.iv = iv;
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

}