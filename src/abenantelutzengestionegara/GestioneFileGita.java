/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abenantelutzengestionegara;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author lutzen.jacopo
 */
public class GestioneFileGita {

    private final int id = 4; // numero di byte 
    private final int localita = 40; // numero di byte 
    private final int durata = 4; // numero di byte 
    private Controlli c = new Controlli();

    private static final int DIM_RECORD = 48; // 40 + 4 + 4 = 48

    /**
     * creazione e/o verfica del file
     *
     * @return gestioni errori
     */
    public boolean creaFileGita() {
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

   
    /**
     * metodo per scrivere sul file
     * @param id  id della gita
     * @param localita localita della gita
     * @param durata durata della gita
     * @return 
     */
    public boolean inserisciGita(int id, String localita, int durata) {
        try (RandomAccessFile file = new RandomAccessFile("elencoGite.pdm", "rw")) {

            //  Sposta il cursore alla fine del file (Append) per non scrivere tutto su una linea
            file.seek(file.length());

            // Scrive la Matricola (writeInt occupa 4 byte)
            file.writeInt(id);

            // Scrive il Nome (deve essere di 20 caratteri per occupare 40 byte)
            file.writeChars(c.aggiustaLunghezzaStringa(localita));

            // Scrive l'Anno (writeInt occupa 4 byte)
            file.writeInt(durata);

            System.out.println("Gita " + id + " " + localita + " salvato con successo!");
            return true;

        } catch (IOException e) {
            System.out.println("Errore durante la scrittura: " + e.getMessage());
            return false;
        }
    }

    public boolean leggiGita(int posizione) {

        // Modalità "r" (solo lettura) 
        try (RandomAccessFile file = new RandomAccessFile("elencoGite.pdm", "r")) {

            // Calcolo dove deve posizionarsi il cursore.
            // Se cerca il 2° record (posizione 2): (2 - 1) * 48 = byte 48
            long posizioneByte = (long) (posizione - 1) * DIM_RECORD;

            // Controlla che la posizione richiesta non sia oltre la fine del file
            if (posizioneByte >= file.length()) {
                System.out.println("Nessun record trovato in questa posizione.");
                return false;
            }

            // Sposta il cursore all'inizio del record scelto
            file.seek(posizioneByte);

            // Legge i dati esattamente nello stesso ordine in cui li ha scritti
            int id = file.readInt();
            String localita = c.leggiStringaDalFile(file);
            int durata = file.readInt();

            // Stampo il risultato 
            System.out.println("--- Gita trovata ---");
            System.out.println("Id: " + id);
            System.out.println("Localita: " + localita);
            System.out.println("Durata: " + durata);

            return true;

        } catch (IOException e) {
            System.out.println("Errore in lettura: " + e.getMessage());
            return false;
        }
    }
}
