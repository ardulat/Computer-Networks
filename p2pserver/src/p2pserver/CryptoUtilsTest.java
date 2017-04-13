/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2pserver;

/**
 *
 * @author bakhytnazirov
 */
import java.io.File;
 
/**
 * A tester for the CryptoUtils class.
 * @author www.codejava.net
 *
 */
public class CryptoUtilsTest {
    public static void main(String[] args) {
        String key = "we need A grades";
        File inputFile = new File("/Users/bakhytnazirov/Desktop/wp.jpg");
        File encryptedFile = new File("/Users/bakhytnazirov/Desktop/doc1");
        File decryptedFile = new File("/Users/bakhytnazirov/Desktop/doc2");
         
        try {
            CryptoUtils.encrypt(key, inputFile, encryptedFile);
            CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}