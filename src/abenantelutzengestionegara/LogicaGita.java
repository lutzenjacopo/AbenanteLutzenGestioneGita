package abenantelutzengestionegara;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Classe che gestisce TUTTA la logica dell'applicazione.
 * Le GUI chiamano solo i metodi di questa classe.
 * Nessun riferimento a componenti grafici qui dentro.
 *
 */
public class LogicaGita {

    private GestioneFileGita gestioneGite = new GestioneFileGita();
    private GestioneFileStudenti gestioneStudenti = new GestioneFileStudenti();
    private GestioneFile gestioneIscrizioni = new GestioneFile();
    private Controlli controlli = new Controlli();

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

        // Controlla duplicato ID usando l'HashSet in memoria invece di rileggere il file
        if (gestioneIscrizioni.getId().esisteIdGita(id)) {
            return "ERRORE: Esiste già una gita con ID " + id + ".";
        }
        // Verifica anche nel file (la gita potrebbe esistere senza iscrizioni)
        for (Gita g : caricaTutteLeGite()) {
            if (g.getId() == id) {
                return "ERRORE: Esiste già una gita con ID " + id + ".";
            }
        }

        boolean ok = gestioneGite.inserisciGita(id, localita, durata);
        return ok ? "OK: Gita aggiunta con successo!" : "ERRORE: Impossibile salvare la gita sul file.";
    }

    /**
     * Rimuove una gita dal file .pdm.
     * @return messaggio di esito
     */
    /**
     * Rimuove una gita e, per ogni studente iscritto:
     * - rimuove la sua iscrizione a questa gita
     * - se lo studente non è iscritto ad altre gite, lo elimina anche dal file studenti
     *
     * @return messaggio di esito
     */
    public String rimuoviGita(int idGita) {
        // 1. Recupera le matricole degli studenti iscritti a questa gita
        HashSet<Integer> matricoleIscritte = gestioneIscrizioni.getStudentiPerGita(idGita);

        // 2. Per ogni studente iscritto a questa gita
        for (int matricola : matricoleIscritte) {
            // Rimuove l'iscrizione studente-gita dal file iscrizioni.txt
            gestioneIscrizioni.rimuoviIscrizione(matricola, idGita);

            // Controlla se lo studente è ancora iscritto ad altre gite
            HashSet<Integer> altreGite = gestioneIscrizioni.getGitePerStudente(matricola);
            if (altreGite.isEmpty()) {
                // Lo studente non appartiene più a nessuna gita: rimuovilo dal file studenti
                gestioneStudenti.rimuoviStudente(matricola);
            }
        }

        // 3. Rimuove la gita dal file elencoGite.pdm
        boolean ok = gestioneGite.rimuoviGita(idGita);
        return ok ? "OK: Gita rimossa con successo." : "ERRORE: Gita non trovata o impossibile rimuoverla.";
    }

    /**
     * Carica tutte le gite — delega a GestioneFileGita (no duplicazione).
     */
    public ArrayList<Gita> caricaTutteLeGite() {
        return gestioneGite.caricaTutteLeGite();
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

        // Controlla duplicato matricola usando l'HashSet in memoria (O(1) invece di O(n))
        if (gestioneIscrizioni.getId().esisteIdStudente(matricola)) {
            boolean iscOk = gestioneIscrizioni.aggiungiIscrizione(matricola, idGita);
            return iscOk
                ? "OK: Studente già presente, iscritto alla gita con successo!"
                : "ERRORE: Lo studente è già iscritto a questa gita.";
        }

        // Studente non ancora in memoria: verifica anche nel file studenti
        for (Studente s : caricaTuttiGliStudenti()) {
            if (s.getMatricola() == matricola) {
                boolean iscOk = gestioneIscrizioni.aggiungiIscrizione(matricola, idGita);
                return iscOk
                    ? "OK: Studente già presente, iscritto alla gita con successo!"
                    : "ERRORE: Lo studente è già iscritto a questa gita.";
            }
        }

        // Nuovo studente: salva e poi iscrivi
        boolean studenteOk = gestioneStudenti.inserisciStudente(matricola, nomeStr, cognomeStr, anno);
        if (!studenteOk) {
            return "ERRORE: Impossibile salvare lo studente sul file.";
        }

        boolean iscOk = gestioneIscrizioni.aggiungiIscrizione(matricola, idGita);
        return iscOk
            ? "OK: Studente aggiunto e iscritto alla gita con successo!"
            : "ERRORE: Studente salvato ma impossibile iscriverlo alla gita.";
    }

    /**
     * Rimuove l'iscrizione di uno studente da una gita.
     * @return messaggio di esito
     */
    public String rimuoviStudenteDaGita(int matricola, int idGita) {
        boolean ok = gestioneIscrizioni.rimuoviIscrizione(matricola, idGita);
        return ok ? "OK: Studente rimosso dalla gita con successo." : "ERRORE: Iscrizione non trovata o impossibile rimuoverla.";
    }

    /**
     * Carica tutti gli studenti — delega a GestioneFileStudenti (no duplicazione).
     */
    public ArrayList<Studente> caricaTuttiGliStudenti() {
        return gestioneStudenti.caricaTuttiGliStudenti();
    }

    /**
     * Restituisce gli studenti iscritti a una gita specifica.
     * Incrocia il file studenti con il file iscrizioni.
     */
    public ArrayList<Studente> getStudentiPerGita(int idGita) {
        ArrayList<Studente> risultato = new ArrayList<>();
        HashSet<Integer> matricoleIscritte = gestioneIscrizioni.getStudentiPerGita(idGita);

        for (Studente s : caricaTuttiGliStudenti()) {
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
