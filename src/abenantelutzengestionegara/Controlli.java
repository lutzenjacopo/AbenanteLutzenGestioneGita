/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abenantelutzengestionegara;

import java.io.*;

/**
 *
 * @author lutzen.jacopo
 */
public class Controlli {
    
    /**
     * inseriesce gli * per lo spazio che rimane dai 20 char 
     * @param s stringa da controllare
     * @return la stringa modificata 
     */
    public String aggiustaLunghezzaStringa(String s) {
        String aggiustata=s;
        if (s.length() < 20) {
            for (int i = 0; i < (20 - s.length()); i++) {
                aggiustata += "*";
            }
            return aggiustata;
        } else if (s.length() > 20) {
            aggiustata = s.substring(0, 19);
            return aggiustata;
        }
        return s;
    }
    
    /**
     * controlla se una variabile String è un intero
     *
     * @param n parametro da controllare
     * @return
     */
    public boolean controlloInt(String n) {
        try {
            //provo a convertire il parametro in un int
            int numero = Integer.parseInt(n);
        } catch (NumberFormatException e) {
            // n è una Stringa
            System.out.println("Errore: Il dato inserito non è un numero valido!");
            return false;
        }
        // n è un intero
        return true;
    }

    /**
     * controlla se una variabile String è una String
     *
     * @param n parametro da controllare
     * @return
     */
    public boolean controlloString(String n) {
        //provo a convertire il parametro in un int
        try {
            int numero = Integer.parseInt(n);
        } catch (NumberFormatException e) {
            // n è una Stringa
            return true;
        }
        // n è un intero
        System.out.println("Errore: Il dato inserito è un numero !");
        return false;
    }

    /**
     * controlla se una variabile String è nulla
     *
     * @param n parametro da controllare
     * @return
     */
    public boolean controlloNull(String n) {
        if (!n.isEmpty()) {
            return true;
        }
        return false;
    }
    
    /**
     * gli passi il file e legge la stringa leva gli *  e lo spazio
     * @param file
     * @return
     * @throws IOException 
     */
    public String leggiStringaDalFile(RandomAccessFile file) throws IOException {
        StringBuilder sb = new StringBuilder();
        
        // 1. Legge esattamente 'lunghezza' caratteri dal file
        for (int i = 0; i < 20; i++) {
            sb.append(file.readChar());
        }
        
        // 2. Converte in Stringa
        String letta = sb.toString();
        
        // 3. Rimuove tutti gli asterischi che avevamo aggiunto in fase di scrittura
        // Usiamo replace("*", "") per sostituire gli asterischi con "niente"
        return letta.replace("*", "").trim(); 
    }
}
