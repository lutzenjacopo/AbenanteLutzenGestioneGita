package abenantelutzengestionegara;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Gestisce lettura, scrittura e logica delle iscrizioni studenti-gite.
 * File: "iscrizioni.txt" — ogni riga: matricola,idGita
 * Usa Id (HashMap + HashSet) come struttura dati in memoria.
 */
public class GestioneFile {

    private static final String FILE_ISCRIZIONI = "iscrizioni.txt";
    private Id id = new Id();

    // ── INIZIALIZZAZIONE ──────────────────────────────────────

    /**
     * Carica tutte le iscrizioni dal file nell'oggetto Id.
     * Da chiamare all'avvio del programma.
     */
    public void inizializza() {
        caricaIscrizioni();
        System.out.println("Matricole: " + id.getMatricole());
        System.out.println("ID Gite:   " + id.getIdGite());
    }

    // ── AGGIUNGI / RIMUOVI ────────────────────────────────────

    /**
     * Aggiunge un'iscrizione: controlla duplicati in memoria,
     * scrive sul file e aggiorna Id.
     */
    public boolean aggiungiIscrizione(int matricola, int idGita) {
        if (id.esisteIscrizione(matricola, idGita)) {
            System.out.println("Iscrizione già esistente: " + matricola + " -> " + idGita);
            return false;
        }

        boolean ok = scriviRiga(matricola, idGita);
        if (ok) {
            id.aggiungiIscrizione(matricola, idGita);
        }
        return ok;
    }

    /**
     * Rimuove un'iscrizione dal file e aggiorna Id in memoria.
     */
    public boolean rimuoviIscrizione(int matricola, int idGita) {
        boolean ok = eliminaRiga(matricola, idGita);
        if (ok) {
            id.rimuoviIscrizione(matricola, idGita);
        }
        return ok;
    }

    // ── LETTURA ───────────────────────────────────────────────

    /**
     * Restituisce il set delle gite a cui è iscritto uno studente.
     * Legge direttamente dalla HashMap in memoria.
     */
    public HashSet<Integer> getGitePerStudente(int matricola) {
        return id.getGitePerStudente(matricola);
    }

    /**
     * Restituisce il set delle matricole iscritte a una gita.
     * Scorre la HashMap per trovare tutti gli studenti che contengono idGita.
     */
    public HashSet<Integer> getStudentiPerGita(int idGita) {
        HashSet<Integer> studenti = new HashSet<>();
        for (int matricola : id.getMatricole()) {
            if (id.getGitePerStudente(matricola).contains(idGita)) {
                studenti.add(matricola);
            }
        }
        return studenti;
    }

    /** Espone l'oggetto Id */
    public Id getId() {
        return id;
    }

    /**
     * Stampa tutte le righe del file (utile per debug).
     */
    public void stampaIscrizioni() {
        ArrayList<String> righe = leggiTutteLeRighe();

        if (righe.isEmpty()) {
            System.out.println("Nessuna iscrizione nel file.");
            return;
        }

        System.out.println("--- Iscrizioni (" + righe.size() + ") ---");
        for (int i = 0; i < righe.size(); i++) {
            String[] parti = righe.get(i).split(",");
            System.out.println("  [" + (i + 1) + "] Matricola: " + parti[0] + " -> Gita ID: " + parti[1]);
        }
    }

    // ── METODI PRIVATI ────────────────────────────────────────

    /** Scrive una riga "matricola,idGita" in fondo al file. */
    private boolean scriviRiga(int matricola, int idGita) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_ISCRIZIONI, true))) {
            bw.write(matricola + "," + idGita);
            bw.newLine();
            System.out.println("Scritto: " + matricola + "," + idGita);
            return true;
        } catch (IOException e) {
            System.out.println("Errore scrittura: " + e.getMessage());
            return false;
        }
    }

    /** Elimina la riga corrispondente a (matricola, idGita) e riscrive il file. */
    private boolean eliminaRiga(int matricola, int idGita) {
        ArrayList<String> righe = leggiTutteLeRighe();
        boolean trovata = righe.remove(matricola + "," + idGita);

        if (!trovata) {
            System.out.println("Record non trovato: " + matricola + "," + idGita);
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_ISCRIZIONI, false))) {
            for (String riga : righe) {
                bw.write(riga);
                bw.newLine();
            }
            System.out.println("Eliminato: " + matricola + "," + idGita);
            return true;
        } catch (IOException e) {
            System.out.println("Errore eliminazione: " + e.getMessage());
            return false;
        }
    }

    /** Carica il file in Id (usato da inizializza). */
    private void caricaIscrizioni() {
        for (String riga : leggiTutteLeRighe()) {
            String[] parti = riga.split(",");
            if (parti.length == 2) {
                try {
                    int matricola = Integer.parseInt(parti[0].trim());
                    int idGita    = Integer.parseInt(parti[1].trim());
                    id.aggiungiIscrizione(matricola, idGita);
                } catch (NumberFormatException e) {
                    System.out.println("Riga non valida ignorata: " + riga);
                }
            }
        }
    }

    /** Legge tutte le righe non vuote dal file. */
    private ArrayList<String> leggiTutteLeRighe() {
        ArrayList<String> righe = new ArrayList<>();
        File file = new File(FILE_ISCRIZIONI);
        if (!file.exists()) return righe;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String riga;
            while ((riga = br.readLine()) != null) {
                if (!riga.trim().isEmpty()) righe.add(riga.trim());
            }
        } catch (IOException e) {
            System.out.println("Errore lettura file: " + e.getMessage());
        }

        return righe;
    }
}
