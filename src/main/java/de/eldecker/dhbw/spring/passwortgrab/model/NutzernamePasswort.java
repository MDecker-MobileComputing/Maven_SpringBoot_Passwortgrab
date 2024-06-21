package de.eldecker.dhbw.spring.passwortgrab.model;

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