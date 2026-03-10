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
    private int matricola=4,nome=40,cognome=40,anno=4;
    private static final int DIM_RECORD =88;
    public boolean creaFileStudente(){
        try {
            RandomAccessFile file = new RandomAccessFile("elencoStudenti.pdm", "r");
            //calcolo la dimensione del file per capire quanti record ci sono. 
             int nRecord = (int) (file.length() / DIM_RECORD);

        } catch (FileNotFoundException ex) {
            System.out.println("File non trovato");
            return false;
        } catch (IOException e) {
            System.out.println("Problema in lettura-scrittura file");
            return false;
        }
        return true;
    }
    
    
}

