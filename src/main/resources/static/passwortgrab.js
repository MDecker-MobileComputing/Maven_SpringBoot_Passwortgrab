"use strict";


document.addEventListener( "DOMContentLoaded", function() {    

    const toggleButton = document.getElementById( "id-toggleButton" );
    toggleButton.addEventListener( "click", function() {

        const passwordInput = document.getElementById( "id-passwort" );

        if ( passwordInput.type === "password" ) {

            passwordInput.type = "text";

        } else {

            passwordInput.type = "password";
        }
    });

    console.log( "ToggleButton initialisiert" );
});

