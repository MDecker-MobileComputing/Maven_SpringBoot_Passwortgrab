"use strict";

let nutzernameInput = null;
let passwortInput = null;

/**
 * Event-Handler-Funktion für Klick auf Button "Sichtbarkeit umschalten".
 */
function togglePasswortSichtbarkeit() {

    if ( passwortInput.type === "password" ) {

        passwortInput.type = "text";

    } else {

        passwortInput.type = "password";
    }
}


/**
 * Event-Handler für Button "In Zwischenablage kopieren" für Nutzername.
 */
function kopierenNutzernameInZwischenablage() {

    nutzernameInput.select();
    navigator.clipboard.writeText( nutzernameInput.value );
    console.log( "Nutzername in Zwischenablage kopiert" );
}


/**
 * Event-Handler für Button "In Zwischenablage kopieren" für Passwort.
 */
function kopierePasswortInZwischenablage() {

    passwortInput.select();
    navigator.clipboard.writeText( passwortInput.value );
    console.log( "Passwort in Zwischenablage kopiert" );
}


/**
 * Event-Handler für Initialisierung nachdem DOM von Seite geladen wurde.
 */
document.addEventListener( "DOMContentLoaded", function() {

    passwortInput = document.getElementById( "id-passwort" );
    if ( !passwortInput ) {

        console.error( "Input-Element mit ID 'id-passwort' nicht gefunden" );
        return;
    }

    const toggleButton = document.getElementById( "id-toggleButton" );
    if ( !toggleButton ) {

        console.error( "Button-Element mit ID 'id-toggleButton' nicht gefunden" );
        return;
    }

    toggleButton.addEventListener( "click", togglePasswortSichtbarkeit );

    const passwortToClipboardButton = document.getElementById( "id-passwort-zu-clipboard" );
    if ( !passwortToClipboardButton ) {

        console.error( "Button-Element mit ID 'id-passwort-zu-clipboard' nicht gefunden" );
        return;
    }
    passwortToClipboardButton.addEventListener( "click", kopierePasswortInZwischenablage );

    nutzernameInput = document.getElementById( "id-nutzername" );
    if ( !nutzernameInput )  {

        console.log( "Input-Element mit ID 'id-nutzername' nicht gefunden" );
        return;
    }

    const nutzernameToClipboardButton = document.getElementById( "id-nutzername-zu-passwort" );
    if ( !nutzernameToClipboardButton ) {

        console.error( "Button-Element mit ID 'id-nutzername-zu-passwort' nicht gefunden" );
        return;
    }
    nutzernameToClipboardButton.addEventListener( "click", kopierenNutzernameInZwischenablage );

    console.log( "JavaScript initialisiert" );
});

