package de.eldecker.dhbw.spring.passwortgrab.db;

import static jakarta.persistence.GenerationType.AUTO;

import de.eldecker.dhbw.spring.passwortgrab.model.NutzernamePasswort;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.time.LocalDateTime;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Audited
@EnableJpaAuditing
@Entity
@Table( name = "PASSWOERTER" )
public class PasswortEntity {

    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;

    /** Anzeigetitel des Passworts, z.B. "Testnutzer für System 123" */
    private String titel;

    /**
     * Dieses Attribut wird auf einen JSON-String abgebildet.
     * Annotation {@code LOB}, damit auf der Datenbank eine
     * CLOB-Spalte angelegt wird.
     */
    @Convert( converter = NutzernamePasswortAttributConverter.class )
    @Lob
    private NutzernamePasswort nutzernamePasswort;

    private LocalDateTime gueltigBis;

    @Lob
    private String kommentar;


    public PasswortEntity() {
    }

    public PasswortEntity( String titel,
                           NutzernamePasswort nutzernamePasswort,
                           LocalDateTime gueltigBis,
                           String kommentar ) {

        this.titel              = titel;
        this.nutzernamePasswort = nutzernamePasswort;
        this.gueltigBis         = gueltigBis;
        this.kommentar          = kommentar;
    }

    public Long getId() {

        return id;
    }

    public void setId( Long id ) {

        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel( String titel ) {

        this.titel = titel;
    }

    public NutzernamePasswort getNutzernamePasswort() {

        return nutzernamePasswort;
    }

    public void setNutzernamePasswort( NutzernamePasswort nutzernamePasswort ) {

        this.nutzernamePasswort = nutzernamePasswort;
    }

    public LocalDateTime getGueltigBis() {

        return gueltigBis;
    }

    public void setGueltigBis( LocalDateTime gueltigBis ) {

        this.gueltigBis = gueltigBis;
    }

    public String getKommentar() {

        return kommentar;
    }

    public void setKommentar( String kommentar ) {

        this.kommentar = kommentar;
    }

    @Override
    public String toString() {

        return "Passwort\"" + titel + "\"";
    }

}