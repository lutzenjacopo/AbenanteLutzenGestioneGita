package abenantelutzengestionegara;

import java.util.*;

/**
 * Gestisce gli ID del sistema usando:
 * - HashSet<Integer>                    → insieme unico delle matricole studenti
 * - HashSet<Integer>                    → insieme unico degli id gite
 * - HashMap<Integer, HashSet<Integer>>  → mappa matricola → set delle gite a cui è iscritto
 */
public class Id {

    // Insieme di tutte le matricole presenti nel sistema
    private HashSet<Integer> matricole = new HashSet<>();

    // Insieme di tutti gli id gite presenti nel sistema
    private HashSet<Integer> idGite = new HashSet<>();

    // Contiene la matricola con tutte le loro gite a cui fanno parte
    private HashMap<Integer, HashSet<Integer>> iscrizioniPerStudente = new HashMap<>();


    // ── STUDENTI ──────────────────────────────────────────────

    /**
     * Aggiunge una matricola al sistema.
     * Se non esiste ancora nella mappa, crea anche il suo HashSet di gite vuoto.
     */
    public boolean aggiungiIdStudente(int matricola) {
        boolean nuovo = matricole.add(matricola); // add restituisce false se già presente
        if (nuovo) {
            iscrizioniPerStudente.put(matricola, new HashSet<>());
        }
        return nuovo;
    }

    /** Rimuove una matricola e tutte le sue iscrizioni dalla mappa */
    public boolean rimuoviIdStudente(int matricola) {
        iscrizioniPerStudente.remove(matricola);
        return matricole.remove(matricola);
    }

    /** Controlla se una matricola esiste */
    public boolean esisteIdStudente(int matricola) {
        return matricole.contains(matricola);
    }

    /** Restituisce l'insieme di tutte le matricole */
    public HashSet<Integer> getMatricole() {
        return matricole;
    }


    /**
     * Aggiunge un id gita al sistema.
     */
    public boolean aggiungiIdGita(int idGita) {
        return idGite.add(idGita); // add restituisce false se già presente
    }

    /** Rimuove un id gita */
    public boolean rimuoviIdGita(int idGita) {
        return idGite.remove(idGita);
    }

    /** Controlla se un id gita esiste */
    public boolean esisteIdGita(int idGita) {
        return idGite.contains(idGita);
    }

    /** Restituisce l'insieme di tutti gli id gite */
    public HashSet<Integer> getIdGite() {
        return idGite;
    }

    /**
     * Associa uno studente a una gita nella mappa.
     * Se lo studente non esiste ancora, lo aggiunge automaticamente.
     *
     * @param matricola matricola dello studente
     * @param idGita    id della gita
     * @return true se l'iscrizione è nuova, false se già esisteva
     */
    public boolean aggiungiIscrizione(int matricola, int idGita) {
        aggiungiIdStudente(matricola); // crea l'entry nella mappa se non c'è
        aggiungiIdGita(idGita);
        return iscrizioniPerStudente.get(matricola).add(idGita);
    }

    /**
     * Rimuove l'associazione studente-gita dalla mappa.
     */
    public boolean rimuoviIscrizione(int matricola, int idGita) {
        if (!iscrizioniPerStudente.containsKey(matricola)) {
            return false;
        }
        return iscrizioniPerStudente.get(matricola).remove(idGita);
    }

    /**
     * Controlla se uno studente è iscritto a una gita specifica.
     */
    public boolean esisteIscrizione(int matricola, int idGita) {
        if (!iscrizioniPerStudente.containsKey(matricola)) {
            return false;
        }
        return iscrizioniPerStudente.get(matricola).contains(idGita);
    }

    /**
     * Restituisce il set delle gite a cui è iscritto uno studente.
     *
     * @param matricola matricola dello studente
     * @return HashSet con gli id gite, oppure HashSet vuoto se non trovato
     */
    public HashSet<Integer> getGitePerStudente(int matricola) {
        return iscrizioniPerStudente.getOrDefault(matricola, new HashSet<>());
    }

    /**
     * Restituisce tutta la mappa iscrizioni (matricola → set gite).
     */
    public HashMap<Integer, HashSet<Integer>> getIscrizioniPerStudente() {
        return iscrizioniPerStudente;
    }
}
