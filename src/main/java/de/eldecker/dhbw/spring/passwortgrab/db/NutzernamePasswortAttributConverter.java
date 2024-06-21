package de.eldecker.dhbw.spring.passwortgrab.db;

import de.eldecker.dhbw.spring.passwortgrab.model.NutzernamePasswort;

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
    public String convertToDatabaseColumn(NutzernamePasswort nutzernamePasswort) {

        String nutzernamePasswortJson = null;

        try {

            nutzernamePasswortJson = objectMapper.writeValueAsString(nutzernamePasswort);

        } catch (final JsonProcessingException e) {

            LOG.error( "Fehler bei der Konvertierung von NutzernamePasswort zu JSON: " + e.getMessage() );
        }

        return nutzernamePasswortJson;
    }

    @Override
    public NutzernamePasswort convertToEntityAttribute(String nutzernamePasswortJson) {

        NutzernamePasswort nutzernamePasswort = null;

        try {

            nutzernamePasswort = objectMapper.readValue(nutzernamePasswortJson, NutzernamePasswort.class);

        } catch (final IOException e) {

            LOG.error( "Fehler bei der Konvertierung von JSON zu NutzernamePasswort: " + e.getMessage() );
        }

        return nutzernamePasswort;
    }

}