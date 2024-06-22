package de.eldecker.dhbw.spring.passwortgrab.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * Repository für Zugriff auf DB-Tabelle mit {@link PasswortEntity}-Objekten.
 */
public interface PasswortRepo extends JpaRepository<PasswortEntity, Long> {

    /**
     * Liefert alle Passwörter sortiert nach dem Titel ohne Unterscheidung
     * von Groß-/Kleinbuchstaben.
     * 
     * @return Liste aller Passwörter, sortiert aufsteigend nach Titel.
     */
    @Query("SELECT p FROM PasswortEntity p ORDER BY LOWER(p.titel) ASC")
    List<PasswortEntity> findAllSortedByTitle();

}
