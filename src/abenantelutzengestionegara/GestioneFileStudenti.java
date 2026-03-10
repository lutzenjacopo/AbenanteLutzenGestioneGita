/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abenantelutzengestionegara;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author lutzen.jacopo
 */
public class GestioneFileStudenti {
    private final int dimMatricola = 4; // byte occupati da un int
    private final int dimNome = 40;     // byte occupati da 20 caratteri (2 byte ciascuno)
    private final int dimCognome = 40;  // byte occupati da 20 caratteri (2 byte ciascuno)
    private final int dimAnno = 4;      // byte occupati da un int
    
    private static final int DIM_RECORD = 88;

    public boolean creaFileStudente() {
        // ATTENZIONE QUI: "rw" permette a Java di creare il file se non lo trova
        try (RandomAccessFile file = new RandomAccessFile("elencoStudenti.pdm", "rw")) {
            
            // Calcolo la dimensione del file per capire quanti record ci sono
            int nRecord = (int) (file.length() / DIM_RECORD);
            System.out.println("File creato/aperto con successo.");
            System.out.println("Record attualmente presenti: " + nRecord);
            
            return true;

        } catch (IOException e) {
            // Unico catch per IOException (che include anche eventuali FileNotFoundException in casi estremi come permessi negati)
            System.out.println("Problema in creazione/lettura file: " + e.getMessage());
            return false;
        }
    }
}

