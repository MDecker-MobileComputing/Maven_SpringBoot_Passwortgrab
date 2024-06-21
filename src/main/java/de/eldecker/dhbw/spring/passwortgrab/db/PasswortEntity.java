package de.eldecker.dhbw.spring.passwortgrab.db;

import static jakarta.persistence.GenerationType.AUTO;

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

    private String titel;

    private String nutzername;

    private String passwort;

    private LocalDateTime gueltigBis;

    @Lob
    private String kommentar;
    
    

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