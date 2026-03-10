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
    private Controlli c = new Controlli();

    private static final int DIM_RECORD = 88;

    /**
     * creazione e/o verfica del file
     *
     * @return gestioni errori
     */
    public boolean creaFileStudente() {
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

    public boolean inserisciStudente(int matricola, String nome, String cognome, int anno) {
        try (RandomAccessFile file = new RandomAccessFile("elencoStudenti.pdm", "rw")) {

            // 1. Sposto il cursore alla fine del file (Append)
            file.seek(file.length());

            // 2. Scrivo la Matricola (writeInt occupa 4 byte)
            file.writeInt(matricola);

            // 3. Scrivo il Nome (deve essere di 20 caratteri per occupare 40 byte)
            file.writeChars(c.aggiustaLunghezzaStringa(nome));

            // 4. Scrivo il Cognome (deve essere di 20 caratteri per occupare 40 byte)
            file.writeChars(c.aggiustaLunghezzaStringa(cognome));

            // 5. Scrivo l'Anno (writeInt occupa 4 byte)
            file.writeInt(anno);

            System.out.println("Studente " + nome + " " + cognome + " salvato con successo!");
            return true;

        } catch (IOException e) {
            System.out.println("Errore durante la scrittura: " + e.getMessage());
            return false;
        }
    }

    public boolean leggiStudente(int posizione) {

        // Modalità "r" (solo lettura) va benissimo qui
        try (RandomAccessFile file = new RandomAccessFile("elencoStudenti.pdm", "r")) {

            // 1. Calcolo dove deve posizionarsi il cursore.
            // Se cerco il 1° record (posizione 1): (1 - 1) * 88 = byte 0
            // Se cerco il 2° record (posizione 2): (2 - 1) * 88 = byte 88
            long posizioneByte = (long) (posizione - 1) * DIM_RECORD;

            // Controllo che la posizione richiesta non sia oltre la fine del file
            if (posizioneByte >= file.length()) {
                System.out.println("Nessun record trovato in questa posizione.");
                return false;
            }

            // 2. Sposto il cursore all'inizio del record scelto
            file.seek(posizioneByte);

            // 3. Leggo i dati ESATTAMENTE nello stesso ordine in cui li ho scritti
            int matricola = file.readInt();
            String nome = c.leggiStringaDalFile(file);
            String cognome = c.leggiStringaDalFile(file);
            int anno = file.readInt();

            // Stampo il risultato 
            System.out.println("--- Studente Trovato ---");
            System.out.println("Matricola: " + matricola);
            System.out.println("Nome: " + nome);
            System.out.println("Cognome: " + cognome);
            System.out.println("Anno: " + anno);

            return true;

        } catch (IOException e) {
            System.out.println("Errore in lettura: " + e.getMessage());
            return false;
        }
    }
}
