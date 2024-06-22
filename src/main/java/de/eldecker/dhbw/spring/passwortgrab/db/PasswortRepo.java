package de.eldecker.dhbw.spring.passwortgrab.db;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository für Zugriff auf DB-Tabelle mit Passwörtern.
 *
 * @param <PasswortEntity> Typ der Entitäten in der Tabelle.
 *
 * @param <Long> Typ des Primärschlüssels der Entitäten.
 */
public interface PasswortRepo extends JpaRepository<PasswortEntity, Long> {

}
