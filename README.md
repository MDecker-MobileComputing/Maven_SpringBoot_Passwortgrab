# Passwortgrab #

<br>

Einfache Spring-Boot-Anwendung, die einen Passwort-Manager als Web-App mit *Spring Data JPA* realisiert, um die folgenden Programmiertechniken
zu demonstrieren:

* Non-Entity-Klassse `NutzernamePasswort` als Attribut in Entity-Klasse, wird mit `AttributeConverter` für Speicherung in Datenbanktabelle
  auf JSON mit verschlüsselten Werten für "Nutzername" und "Passwort" abgebildet.

* Symmetrische Verschlüsselung mit [AES](https://de.wikipedia.org/wiki/Advanced_Encryption_Standard) im Betriebsmodus 
  ["Cipher Block Chaining" (CBC)](https://de.wikipedia.org/wiki/Cipher_Block_Chaining_Mode), 
  also mit Initialisierungsvektor, der unverschlüsselt in der Tabelle für jeden Datensatz abgelegt wird.
  
* *Spring Data Envers* für Versionierung der Tabelleneinträge.

<br>

----

## License ##

<br>

See the [LICENSE file](LICENSE.md) for license rights and limitations (BSD 3-Clause License).

<br>
