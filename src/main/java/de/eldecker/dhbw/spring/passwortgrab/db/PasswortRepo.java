package de.eldecker.dhbw.spring.passwortgrab.db;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository für Zugriff auf DB-Tabelle mit {@link PasswortEntity}-Objekten.
 */
public interface PasswortRepo extends JpaRepository<PasswortEntity, Long> {

}
