package de.eldecker.dhbw.spring.passwortgrab.db.krypto;

import de.eldecker.dhbw.spring.passwortgrab.db.PasswortEntity;
import de.eldecker.dhbw.spring.passwortgrab.model.NutzernamePasswort;
import de.eldecker.dhbw.spring.passwortgrab.model.PasswortRuntimeException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Ein Objekt dieser Klasse verschlüsselt ein {@link NutzernamePasswort}-Objekt, welches ein
 * Attribut eines {@link PasswortEntity}-Objekts ist, vor der Speichern auf der Datenbank, 
 * und entschlüsselt es beim Lesen von der Datenbank wieder.
 * <br><br>
 * 
 * Für die Speicherung von verschlüsseltem Nutzername und Passwort sowie unverschlüsseltem
 * Initialisierungsvektor (IV) auf der Datenbank wird JSON verwendet.<br> 
 * Beispiel:
 * <code>
 *  {"iv":"SH1vJgeeGiaI+X+JTp0amg==","passwort":"/WqHRJD0shhzlBmSzJcyNA==","nutzername":"5vSTu5aYS1eBl73Xc4tiZA=="}
 * </code>
 */
public class NutzernamePasswortAttributConverter implements AttributeConverter<NutzernamePasswort, String> {

    private final static Logger LOG = LoggerFactory.getLogger( NutzernamePasswortAttributConverter.class );

    /** Objekt für Umwandlung von Objekt in Json oder umgekehrt. */
    private final ObjectMapper _objectMapper = new ObjectMapper();
    
    /** Bean für symmetrische Ver-/Entschlüsselung mit AES-Algorithmus im "CBC"-Modus. */
    @Autowired
    private AesAlgorithmus _aes;

    
    /**
     * Nutzername und Passwort aus {@code nutzernamePasswort} (aber nicht IV) verschlüsseln 
     * und dann als JSON auf Datenbank speichern.
     * 
     * @param nutzernamePasswort Objekt, dessen Werte für Nutzername und Passwort 
     *                           verschlüsselt werden sollen (IV wird nicht veschlüsselt).
     *                           
     * @return JSON-String, der in Datenbanktabelle gespeichert werden soll        
     * 
     * @throws PasswortRuntimeException Ungeprüfte Exception 
     */
    @Override
    public String convertToDatabaseColumn( NutzernamePasswort nutzernamePasswort ) {

        NutzernamePasswort nutzernamePasswortVeschluesselt = null;
        
        try {
                        
            String iv         = nutzernamePasswort.getIv();
            String nutzername = nutzernamePasswort.getNutzername();
            String passwort   = nutzernamePasswort.getPasswort();
            
            String nutzernameKrypt = _aes.verschluesseln( nutzername, iv );
            String passwortKrypt   = _aes.verschluesseln( passwort  , iv );
            
            nutzernamePasswortVeschluesselt = 
                    new NutzernamePasswort( iv, nutzernameKrypt, passwortKrypt ); 
                                                             
            return _objectMapper.writeValueAsString( nutzernamePasswortVeschluesselt );
            
        } catch ( JsonProcessingException | GeneralSecurityException ex ) {

            final String fehlerText = "Fehler bei Verschlüsselung von Spaltenwert: " + 
                                      ex.getMessage();                                      
            LOG.error( fehlerText );
            throw new PasswortRuntimeException( fehlerText, ex );
        }
    }

    
    /**
     * Nutzername und Passwort aus JSON-String von Datenbank entschlüsseln. 
     * 
     * @param nutzernamePasswortJson JSON mit Nutzername und Passwort in verschlüsselter Form;
     *                               IV ist nicht verschlüsselt, kann einfach in Ergebnis
     *                               kopiert werden
     * 
     * @return Objekt mit entschlüsseltem Nutzername und Passwort
     * 
     * @throws PasswortRuntimeException Ungeprüfte Exception
     */
    @Override
    public NutzernamePasswort convertToEntityAttribute( String nutzernamePasswortJson ) {

        NutzernamePasswort nutzernamePasswortVerschluesselt = null;
        
        try {

            nutzernamePasswortVerschluesselt =  _objectMapper.readValue( nutzernamePasswortJson,
                                                                         NutzernamePasswort.class );
            
            String iv              = nutzernamePasswortVerschluesselt.getIv();
            String nutzernameKrypt = nutzernamePasswortVerschluesselt.getNutzername();
            String passwortKrypt   = nutzernamePasswortVerschluesselt.getPasswort();
            
            String nutzernamePlain = _aes.entschluesseln( nutzernameKrypt, iv );
            String passwortPlain   = _aes.entschluesseln( passwortKrypt  , iv );
            
            return new NutzernamePasswort( iv, nutzernamePlain, passwortPlain );

        } catch ( IOException | GeneralSecurityException ex ) {

            final String fehlerText = "Fehler bei Entschlüsselung von Spaltenwert: " +
                                       ex.getMessage();
            LOG.error( fehlerText, ex );
            throw new PasswortRuntimeException( fehlerText, ex );
        }
    }

}