/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abenantelutzengestionegara;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
/**
 *
 * @author lucia
 * Classe che gestisce TUTTA la logica dell'applicazione.
 * Le GUI chiamano solo i metodi di questa classe.
 * Nessun riferimento a componenti grafici qui dentro.
 */
public class LogicaGita {

    private GestioneFileGita gestioneGite = new GestioneFileGita();
    private GestioneFileStudenti gestioneStudenti = new GestioneFileStudenti();
    private GestioneFile gestioneIscrizioni = new GestioneFile();
    private Controlli controlli = new Controlli();

    private static final String FILE_GITE = "elencoGite.pdm";
    private static final String FILE_STUDENTI = "elencoStudenti.pdm";
    private static final int DIM_RECORD_GITA = 48;
    private static final int DIM_RECORD_STUDENTE = 88;

    // ── INIZIALIZZAZIONE ─────────────────────────────────────

    /**
     * Da chiamare all'avvio: crea i file se non esistono e carica le iscrizioni.
     */
    public void inizializza() {
        gestioneGite.creaFileGita();
        gestioneStudenti.creaFileStudente();
        gestioneIscrizioni.inizializza();
    }

    // ── GITE ─────────────────────────────────────────────────

    /**
     * Aggiunge una gita dopo aver validato i dati.
     * @return messaggio di esito (stringa da mostrare nella GUI)
     */
    public String aggiungiGita(String idStr, String localita, String durataStr) {
        if (!controlli.controlloNull(idStr) || !controlli.controlloNull(localita) || !controlli.controlloNull(durataStr)) {
            return "ERRORE: Tutti i campi sono obbligatori.";
        }
        if (!controlli.controlloInt(idStr)) {
            return "ERRORE: L'ID deve essere un numero intero.";
        }
        if (!controlli.controlloInt(durataStr)) {
            return "ERRORE: La durata deve essere un numero intero.";
        }
        if (!controlli.controlloString(localita)) {
            return "ERRORE: La località non può essere un numero.";
        }

        int id = Integer.parseInt(idStr);
        int durata = Integer.parseInt(durataStr);

        // Controlla se l'ID esiste già
        ArrayList<Gita> gite = caricaTutteLeGite();
        for (Gita g : gite) {
            if (g.getId() == id) {
                return "ERRORE: Esiste già una gita con ID " + id + ".";
            }
        }

        boolean ok = gestioneGite.inserisciGita(id, localita, durata);
        if (ok) {
            return "OK: Gita aggiunta con successo!";
        } else {
            return "ERRORE: Impossibile salvare la gita sul file.";
        }
    }

    /**
     * Rimuove una gita dal file .pdm.
     * @return messaggio di esito
     */
    public String rimuoviGita(int idGita) {
        boolean ok = gestioneGite.rimuoviGita(idGita);
        if (ok) {
            return "OK: Gita rimossa con successo.";
        } else {
            return "ERRORE: Gita non trovata o impossibile rimuoverla.";
        }
    }

    /**
     * Carica tutte le gite dal file e le restituisce come lista.
     */
    public ArrayList<Gita> caricaTutteLeGite() {
        ArrayList<Gita> lista = new ArrayList<>();
        File file = new File(FILE_GITE);
        if (!file.exists()) return lista;

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long nRecord = raf.length() / DIM_RECORD_GITA;
            for (int i = 0; i < nRecord; i++) {
                raf.seek((long) i * DIM_RECORD_GITA);
                int id = raf.readInt();
                String localita = controlli.leggiStringaDalFile(raf);
                int durata = raf.readInt();
                lista.add(new Gita(id, localita, durata));
            }
        } catch (IOException e) {
            System.out.println("Errore lettura gite: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Controlla se esiste almeno una gita nel sistema.
     */
    public boolean esistonoGite() {
        return !caricaTutteLeGite().isEmpty();
    }

    // ── STUDENTI ─────────────────────────────────────────────

    /**
     * Aggiunge uno studente e lo iscrive alla gita selezionata.
     * @return messaggio di esito
     */
    public String aggiungiStudente(String nomeStr, String cognomeStr, String matricolaStr, String annoStr, int idGita) {
        if (!controlli.controlloNull(nomeStr) || !controlli.controlloNull(cognomeStr)
                || !controlli.controlloNull(matricolaStr) || !controlli.controlloNull(annoStr)) {
            return "ERRORE: Tutti i campi sono obbligatori.";
        }
        if (!controlli.controlloString(nomeStr)) {
            return "ERRORE: Il nome non può essere un numero.";
        }
        if (!controlli.controlloString(cognomeStr)) {
            return "ERRORE: Il cognome non può essere un numero.";
        }
        if (!controlli.controlloInt(matricolaStr)) {
            return "ERRORE: La matricola deve essere un numero intero.";
        }
        if (!controlli.controlloInt(annoStr)) {
            return "ERRORE: L'anno deve essere un numero intero.";
        }

        int matricola = Integer.parseInt(matricolaStr);
        int anno = Integer.parseInt(annoStr);

        // Controlla se la matricola esiste già
        ArrayList<Studente> studenti = caricaTuttiGliStudenti();
        for (Studente s : studenti) {
            if (s.getMatricola() == matricola) {
                // Studente già esiste: aggiungi solo l'iscrizione
                boolean iscOk = gestioneIscrizioni.aggiungiIscrizione(matricola, idGita);
                if (iscOk) {
                    return "OK: Studente già presente, iscritto alla gita con successo!";
                } else {
                    return "ERRORE: Lo studente è già iscritto a questa gita.";
                }
            }
        }

        // Nuovo studente: salva e poi iscrivi
        boolean studenteOk = gestioneStudenti.inserisciStudente(matricola, nomeStr, cognomeStr, anno);
        if (!studenteOk) {
            return "ERRORE: Impossibile salvare lo studente sul file.";
        }

        boolean iscOk = gestioneIscrizioni.aggiungiIscrizione(matricola, idGita);
        if (iscOk) {
            return "OK: Studente aggiunto e iscritto alla gita con successo!";
        } else {
            return "ERRORE: Studente salvato ma impossibile iscriverlo alla gita.";
        }
    }

    /**
     * Rimuove l'iscrizione di uno studente da una gita.
     * @return messaggio di esito
     */
    public String rimuoviStudenteDaGita(int matricola, int idGita) {
        boolean ok = gestioneIscrizioni.rimuoviIscrizione(matricola, idGita);
        if (ok) {
            return "OK: Studente rimosso dalla gita con successo.";
        } else {
            return "ERRORE: Iscrizione non trovata o impossibile rimuoverla.";
        }
    }

    /**
     * Carica tutti gli studenti dal file .pdm
     */
    public ArrayList<Studente> caricaTuttiGliStudenti() {
        ArrayList<Studente> lista = new ArrayList<>();
        File file = new File(FILE_STUDENTI);
        if (!file.exists()) return lista;

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long nRecord = raf.length() / DIM_RECORD_STUDENTE;
            for (int i = 0; i < nRecord; i++) {
                raf.seek((long) i * DIM_RECORD_STUDENTE);
                int matricola = raf.readInt();
                String nome = controlli.leggiStringaDalFile(raf);
                String cognome = controlli.leggiStringaDalFile(raf);
                int anno = raf.readInt();
                lista.add(new Studente(nome, cognome, matricola, anno));
            }
        } catch (IOException e) {
            System.out.println("Errore lettura studenti: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Restituisce gli studenti iscritti a una gita specifica.
     * Incrocia il file studenti con il file iscrizioni.
     */
    public ArrayList<Studente> getStudentiPerGita(int idGita) {
        ArrayList<Studente> risultato = new ArrayList<>();
        HashSet<Integer> matricoleIscritte = gestioneIscrizioni.getStudentiPerGita(idGita);
        ArrayList<Studente> tuttiStudenti = caricaTuttiGliStudenti();

        for (Studente s : tuttiStudenti) {
            if (matricoleIscritte.contains(s.getMatricola())) {
                risultato.add(s);
            }
        }
        return risultato;
    }

    /**
     * Costruisce le stringhe per la JComboBox nel formato "ID - Località"
     */
    public ArrayList<String> getVociComboGite() {
        ArrayList<String> voci = new ArrayList<>();
        for (Gita g : caricaTutteLeGite()) {
            voci.add(g.getId() + " - " + g.getLocalita());
        }
        return voci;
    }

    /**
     * Estrae l'ID numerico da una stringa "ID - Località"
     */
    public int estraiIdDaVoceCombo(String voce) {
        try {
            return Integer.parseInt(voce.split("-")[0].trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
