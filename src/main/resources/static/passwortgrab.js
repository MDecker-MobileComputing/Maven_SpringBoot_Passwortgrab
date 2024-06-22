"use strict";


let passwordInput = null;

/**
 * Event-Handler-Funktion für Klick auf Button "Sichtbarkeit umschalten".
 */
function togglePasswortSichtbarkeit() {

    if (! passwortInput ) {

        console.error( "Input-Element mit ID 'id-passwort' nicht gefunden" );
        return;
    }

    if ( passwordInput.type === "password" ) {

        passwordInput.type = "text";

    } else {

        passwordInput.type = "password";
    }
}


/**
 * Event-Handler für Initialisierung nachdem DOM von Seite geladen wurde.
 */
document.addEventListener( "DOMContentLoaded", function() {    

    passwordInput = document.getElementById( "id-passwort" );
    if ( !passwordInput ) {

        console.error( "Input-Element mit ID 'id-passwort' nicht gefunden" );
        return;
    }

    const toggleButton = document.getElementById( "id-toggleButton" );
    if ( !toggleButton ) {
            
        console.error( "Button-Element mit ID 'id-toggleButton' nicht gefunden" );
        return;
    }

    toggleButton.addEventListener( "click", togglePasswortSichtbarkeit );

    console.log( "JavaScript initialisiert" );
});

