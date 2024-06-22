package de.eldecker.dhbw.spring.passwortgrab.db.krypto;

import de.eldecker.dhbw.spring.passwortgrab.model.NutzernamePasswort;
import de.eldecker.dhbw.spring.passwortgrab.model.PasswortRuntimeException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NutzernamePasswortAttributConverter implements AttributeConverter<NutzernamePasswort, String> {

    private final static Logger LOG = LoggerFactory.getLogger( NutzernamePasswortAttributConverter.class );

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn( NutzernamePasswort nutzernamePasswort ) {

        try {

            return objectMapper.writeValueAsString( nutzernamePasswort );

        } catch ( final JsonProcessingException ex ) {

            final String fehlerText = "Fehler bei der Konvertierung von NutzernamePasswort zu JSON: " +
                                      ex.getMessage();
            LOG.error( fehlerText );
            throw new PasswortRuntimeException( fehlerText, ex );
        }
    }

    @Override
    public NutzernamePasswort convertToEntityAttribute( String nutzernamePasswortJson ) {

        try {

            return objectMapper.readValue( nutzernamePasswortJson,
                                           NutzernamePasswort.class );

        } catch ( final IOException ex ) {

            final String fehlerText = "Fehler bei der Konvertierung von JSON zu NutzernamePasswort: " +
                                       ex.getMessage();
            LOG.error( fehlerText, ex );
            throw new PasswortRuntimeException( fehlerText, ex );
        }
    }

}