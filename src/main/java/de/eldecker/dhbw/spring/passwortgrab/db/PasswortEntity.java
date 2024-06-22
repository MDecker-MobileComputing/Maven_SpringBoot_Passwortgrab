package de.eldecker.dhbw.spring.passwortgrab.db;

import static jakarta.persistence.GenerationType.AUTO;
import static java.time.LocalDate.now;

import java.time.LocalDate;

import de.eldecker.dhbw.spring.passwortgrab.db.krypto.NutzernamePasswortAttributConverter;
import de.eldecker.dhbw.spring.passwortgrab.model.NutzernamePasswort;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * Ein Objekt dieser Klasse Repräsentiert einen Datensatz in der
 * Tabelle mit den Passwörtern.
 */
@Audited
@EnableJpaAuditing
@Entity
@Table( name = "PASSWOERTER" )
public class PasswortEntity {

    /** Primärschlüssel. */
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

    /** Letzter Tag, an dem das Passwort noch gültig ist. */
    private LocalDate gueltigBis;

    /** Kommentar (optional) */
    @Lob
    private String kommentar;

    
    /**
     * Default-Konstruktor für JPA.
     */
    public PasswortEntity() {}
    

    /**
     * Konstruktor für die Erzeugung eines neuen Passworts
     * mit Definierung aller Attribute (bis auf ID).
     */
    public PasswortEntity( String             titel,
                           NutzernamePasswort nutzernamePasswort,
                           LocalDate          gueltigBis,
                           String             kommentar ) {

        this.titel              = titel;
        this.nutzernamePasswort = nutzernamePasswort;
        this.gueltigBis         = gueltigBis;
        this.kommentar          = kommentar;
    }

    public Long getId() {

        return id;
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

    public LocalDate getGueltigBis() {

        return gueltigBis;
    }

    public void setGueltigBis( LocalDate gueltigBis ) {

        this.gueltigBis = gueltigBis;
    }

    public String getKommentar() {

        return kommentar;
    }

    public void setKommentar( String kommentar ) {

        this.kommentar = kommentar;
    }

    
    /**
     * Hilfsmethode: Gibt zurück, ob das Passwort abgelaufen ist,
     * also ob {@code gueltigBis} ein Datum ist, das in der
     * Vergangenheit liegt 
     * 
     * @return {@code true} wenn das Passwort abgelaufen ist, 
     *         sonst {@code false}.
     */
    public boolean istAbgelaufen() {

        return now().isAfter( gueltigBis );
    }

    
    /**
     * String-Repräsentation des Objekts.
     * 
     * @return String mit Titel des Eintrags.
     */
    @Override
    public String toString() {

        return "Passwort mit Titel \"" + titel + "\".";
    }

}