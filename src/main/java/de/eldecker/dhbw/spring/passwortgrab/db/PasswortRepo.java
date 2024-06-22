package de.eldecker.dhbw.spring.passwortgrab.db;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository für Zugriff auf DB-Tabelle mit Passwörtern.
 */
public interface PasswortRepo extends JpaRepository<PasswortEntity, Long> {

}
