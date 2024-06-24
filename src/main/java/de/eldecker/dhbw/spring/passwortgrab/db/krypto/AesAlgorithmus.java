package de.eldecker.dhbw.spring.passwortgrab.db.krypto;

import static jakarta.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;


/**
 * Diese Klasse enthält die Methoden für die Verschlüsselung und Entschlüsselung
 * mit dem symmetrischen Verschlüsselungsalgorithmus "AES" im Betriebsmodus
 * "CBC" (Chiper Block Chaining).
 */
@Service
public class AesAlgorithmus {
    
    private final static Logger LOG = LoggerFactory.getLogger( AesAlgorithmus.class );
    
    /**
     * Genauer Bezeichner Verschlüsselungsalgorithmus:
     * <ul>
     * <li>AES: Symmetrischer Verschlüsselungsalgorithmus, Block-Chiffre</li>
     * <li>CBC: Betriebsart "Chiper Block Chaining", braucht aber Initialisierungsvektor (IV).</li>
     * <li>PKCS5Padding: Algo für Füll-Bytes, um Block bei Bedarf aufzufüllen.</li>
     * </ul>
     */
    private static final String KRYPTO_ALGO_NAME = "AES/CBC/PKCS5Padding";    
    
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
    
    /** Objekt für Ver-/Entschlüsselung */
    private Cipher _aesCipher = null;

    /** Objekt mit symmetrischem Schlüssel */
    private SecretKeySpec _secretKey = null;
    
    
    /**
     * Diese Methode wird unmittelbar nach Erzeugung der Bean aufgerufen,
     * aber erst, wenn der Konstruktor abgearbeitet wurde, so dass der
     * aus der Datei {@code application.properties} eingelesene symmetrische
     * Schlüssel zur Verfügung steht.
     * <br><br>
     *
     * Wenn diese Methode eine Exception wirft, dann bricht das Programm ab.
     * Das ist beabsichtigt, weil die Anwendung nicht sinnvoll funktionieren
     * kann wenn der Zugriff auf die verschlüsselten Datenbankspalten nicht
     * möglich ist.
     *
     * @throws GeneralSecurityException Fehler bei Ver- oder Entschlüsselung
     */
    @PostConstruct
    private void initialisierung() throws GeneralSecurityException {
        
        Pattern hexRegexpPattern = Pattern.compile( "^([0-9a-fA-F]{32})$" );
        Matcher matcher          = hexRegexpPattern.matcher( _schluesselHex );
        if ( !matcher.matches() ) {
            
            throw new GeneralSecurityException( "Der AES-Schlüssel besteht nicht aus 32 Hex-Ziffern." );
        }        
        
        final byte[] keyBytes = parseHexBinary( _schluesselHex ); // throws IllegalArgumentException (wenn keine gültige Hex-Zahl)
        _secretKey = new SecretKeySpec( keyBytes, "AES" );
        
        _aesCipher = Cipher.getInstance( KRYPTO_ALGO_NAME );
        
        testVerEntschluesselung(); // throws GeneralSecurityException
        
        LOG.info( "Verschlüsselungs-Algo initialisiert: {}", _aesCipher  );
    }
    
    
    /**
     * Test: Verschlüsselt einen String und entschlüsselt ihn wieder.
     *
     * @throws GeneralSecurityException Fehler bei Ver- oder Entschlüsselung, oder wenn
     *                                  Entschlüsselungsergebnis nicht dem Ausgangstext
     *                                  entspricht.
     */
    private void testVerEntschluesselung() throws GeneralSecurityException {

        final String testString = "Lorem Ipsum ?!*";
        
        final String initVektor = erzeugeZufaelligenIV();

        final String testStringVerschluesselt = verschluesseln( testString, initVektor );
        LOG.info( "Test-String verschlüsselt: \"{}\"", testStringVerschluesselt );

        final String testStringEntschluesselt = entschluesseln( testStringVerschluesselt, initVektor );
        LOG.info( "Test-String entschlüsselt: \"{}\" ", testStringEntschluesselt );

        if ( testString.equals( testStringEntschluesselt ) == false ) {

            throw new GeneralSecurityException( "Test für Ver- und Entschlüsselung fehlgeschlagen" );
        }
    }    


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

    private IvParameterSpec decodeInitVektor( String iv ) {
        
        byte[] byteArray = _base64Decoder.decode( iv );
        return new IvParameterSpec( byteArray );
    }

    /**
     * Klartext verschlüsseln.
     * 
     * @param stringKlartext String, der verschlüsselt werden soll
     * 
     * @param ivStr Initialisierungsvektor für Betriebsmodus "CBC"
     * 
     * @return Chiffre als Base64-String, wird in Datenbank gespeichert
     * 
     * @throws GeneralSecurityException Fehler beim Verschlüsseln
     */
    public String verschluesseln( String stringKlartext, String ivStr ) throws GeneralSecurityException {
        
        IvParameterSpec ivParameterSpec = decodeInitVektor( ivStr );

        _aesCipher.init( ENCRYPT_MODE, _secretKey, ivParameterSpec ); // throws InvalidKeyException

        byte[] klartextBytes = stringKlartext.getBytes();

        byte[] encryptedBytes = _aesCipher.doFinal( klartextBytes ); // throws IllegalBlockSizeException, BadPaddingException
        
        return  _base64Encoder.encodeToString( encryptedBytes );
    }


    /**
     * Chiffre entschlüsseln.
     * 
     * @param stringChiffre Chiffre als Base64-String (von Datenbank), die entschlüsselt werden soll
     * 
     * @param ivStr Initialisierungsvektor für Betriebsmodus "CBC" 
     * 
     * @return Klartext
     * 
     * @throws GeneralSecurityException Fehler beim Entschlüsseln
     */
    public String entschluesseln( String stringChiffre, String ivStr ) throws GeneralSecurityException {
        
        IvParameterSpec ivParameterSpec = decodeInitVektor( ivStr );

        _aesCipher.init( DECRYPT_MODE, _secretKey, ivParameterSpec ); // throws InvalidKeyException

        byte[] chiffreBytes = _base64Decoder.decode( stringChiffre );

        byte[] decryptedBytes = _aesCipher.doFinal( chiffreBytes ); // throws IllegalBlockSizeException, BadPaddingException

        return new String( decryptedBytes );
    }

}
