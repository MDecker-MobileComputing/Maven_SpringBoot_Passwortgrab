package de.eldecker.dhbw.spring.passwortgrab.db.krypto;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Diese Klasse enthält die Methoden für die Verschlüsselung und Entschlüsselung
 * mit dem symmetrischen Verschlüsselungsalgorithmus "AES".
 */
@Service
public class AesAlgorithmus {
    
    /**
     * Symmetrischer Schlüssel (128 Bit) als Hexadezimalziffer mit 32 Buchstaben,
     * aus Datei {@code application.properties}. Da es sich bei AES um ein
     * symmetrisches Verfahren handelt, wird dieses Schlüssel sowohl für die
     * Ver- als auch die Entschlüsselung benötigt.
     * Default-Wert ist ein leerer String.
     * <br><br>
     * 
     * Es könnte auch die folgende Validierungsannotation mit 
     * {@code jakarta.validation.constraints.Pattern} vorgenommen werden;
     * wenn aber diese Überprüfung das Programm beim Hochfahren abbrechen
     * lässt, dann findet man die Fehlermeldung nicht im Exception-Trace,
     * weil die Anzahl der ausgegebenen Einträge vom Exception-Stack
     * beschränkt ist.<br>
     * 
     * <code> 
     * @Pattern( regexp = "^[0-9a-fA-F]{32}$",
     *           message = "Der Schlüssel muss aus 32 Hex-Ziffern bestehen" )
     * </code>
     */
    @Value( "${de.eldecker.passwortgrab.krypto.schluessel:}" )                  
    private String _schluesselHex;

    /** Objekt für die Umwandlung von {@code byte[]} nach String mit Base64-Kodierung */
    private Encoder _base64Encoder = Base64.getEncoder();

    /** Objekt für die (Rück-)Umwandlung von einem String nach {@code byte[]} mit Base64-Kodierung */
    private Decoder _base64Decoder = Base64.getDecoder();

    /** Sicherer Zufallsgenerator. */
    private SecureRandom _secureRandom = new SecureRandom();


    /**
     * Erzeugt zufälligen Initialisierungsvektor (IV), der für einige
     * Betriebmodi von Blockchiffren benötigt wird, z.B. für den
     * "Chiper Block Mode" (CBC). Der Länge des IVs sollte der
     * Blockgröße des symmetrischen Verschlüsselungsalgorithmus
     * entsprechen, also 128 Bit für AES.
     *
     * @return Zufälliger Initialisierungsvektor (128 Bit) in
     *         Base64-Codierung.<br>
     *         Beispielwert: {@code 2W2o7iRS7+DnsP2Lb8O0ZA==}
     */
    public String erzeugeZufaelligenIV() {

        byte[] zufallsByteArray = new byte[ 16 ]; // 16Byte * 8 Bit/Byte = 128 Bit

        _secureRandom.nextBytes( zufallsByteArray );

        return _base64Encoder.encodeToString( zufallsByteArray );
    }

}
