package de.eldecker.dhbw.spring.passwortgrab.db;


import org.springframework.data.repository.history.RevisionRepository;


/**
 * Repo um Revisionen von {@link PasswortEntity}, die mit <i>Spring Data Envers</i>
 * aufgezeichnet wurden, abzufragen.
 * <br><br>
 * 
 * Siehe
 * <a href="https://docs.spring.io/spring-data/envers/docs/current/reference/html/#reference">hier</a>
 * für die offizielle Dokumentation zu <i>Spring Data Envers</i>.
 */
public interface PasswortRevisionsRepo 
                 extends RevisionRepository<PasswortEntity, Long, Integer>{
                     
}
