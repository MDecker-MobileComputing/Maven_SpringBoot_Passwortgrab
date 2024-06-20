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

    private String name;

    private String nutzername;

    private String passwort;

    private LocalDateTime gueltigBis;

    @Lob
    private String kommentar;

}