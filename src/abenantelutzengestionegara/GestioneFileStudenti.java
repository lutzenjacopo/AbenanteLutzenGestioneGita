package abenantelutzengestionegara;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Gestisce lettura e scrittura del file binario "elencoStudenti.pdm".
 * Record: [int matricola (4)] [String nome (40)] [String cognome (40)] [int anno (4)] = 88 byte
 */
public class GestioneFileStudenti {

    private static final String FILE_STUDENTI = "elencoStudenti.pdm";
    private final int dimMatricola = 4;
    private final int dimNome = 40;
    private final int dimCognome = 40;
    private final int dimAnno = 4;
    private static final int DIM_RECORD = 88;
    private Controlli c = new Controlli();

    /**
     * Crea/apre il file studenti.
     */
    public boolean creaFileStudente() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_STUDENTI, "rw")) {
            int nRecord = (int) (file.length() / DIM_RECORD);
            System.out.println("File studenti creato/aperto con successo.");
            System.out.println("Record attualmente presenti: " + nRecord);
            return true;
        } catch (IOException e) {
            System.out.println("Problema in creazione/lettura file studenti: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserisce un nuovo studente in fondo al file.
     */
    public boolean inserisciStudente(int matricola, String nome, String cognome, int anno) {
        try (RandomAccessFile file = new RandomAccessFile(FILE_STUDENTI, "rw")) {
            file.seek(file.length());
            file.writeInt(matricola);
            file.writeChars(c.aggiustaLunghezzaStringa(nome));
            file.writeChars(c.aggiustaLunghezzaStringa(cognome));
            file.writeInt(anno);
            System.out.println("Studente " + nome + " " + cognome + " salvato con successo!");
            return true;
        } catch (IOException e) {
            System.out.println("Errore durante la scrittura: " + e.getMessage());
            return false;
        }
    }

    /**
     * Legge e stampa uno studente in base alla posizione (1-based).
     */
    public boolean leggiStudente(int posizione) {
        try (RandomAccessFile file = new RandomAccessFile(FILE_STUDENTI, "r")) {
            long posizioneByte = (long) (posizione - 1) * DIM_RECORD;
            if (posizioneByte >= file.length()) {
                System.out.println("Nessun record trovato in questa posizione.");
                return false;
            }
            file.seek(posizioneByte);
            int matricola = file.readInt();
            String nome = c.leggiStringaDalFile(file);
            String cognome = c.leggiStringaDalFile(file);
            int anno = file.readInt();
            System.out.println("--- Studente trovato ---");
            System.out.println("Matricola: " + matricola + " | Nome: " + nome + " | Cognome: " + cognome + " | Anno: " + anno);
            return true;
        } catch (IOException e) {
            System.out.println("Errore in lettura: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carica tutti gli studenti dal file e li restituisce come ArrayList.
     * Centralizza la lettura: LogicaGita usa questo metodo invece di rileggere da zero.
     */
    public ArrayList<Studente> caricaTuttiGliStudenti() {
        ArrayList<Studente> lista = new ArrayList<>();
        java.io.File file = new java.io.File(FILE_STUDENTI);
        if (!file.exists()) return lista;

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long nRecord = raf.length() / DIM_RECORD;
            for (int i = 0; i < nRecord; i++) {
                raf.seek((long) i * DIM_RECORD);
                int matricola = raf.readInt();
                String nome = c.leggiStringaDalFile(raf);
                String cognome = c.leggiStringaDalFile(raf);
                int anno = raf.readInt();
                lista.add(new Studente(nome, cognome, matricola, anno));
            }
        } catch (IOException e) {
            System.out.println("Errore lettura studenti: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Rimuove uno studente dal file tramite file temporaneo.
     * NUOVO: metodo mancante nella versione precedente.
     */
    public boolean rimuoviStudente(int matricolaDaRimuovere) {
        java.io.File fileOriginale = new java.io.File(FILE_STUDENTI);
        java.io.File fileTemp = new java.io.File("tempStudenti.pdm");
        boolean trovato = false;

        try (RandomAccessFile raf = new RandomAccessFile(fileOriginale, "rw");
             RandomAccessFile tempRaf = new RandomAccessFile(fileTemp, "rw")) {

            long nRecord = raf.length() / DIM_RECORD;
            for (int i = 0; i < nRecord; i++) {
                raf.seek((long) i * DIM_RECORD);
                int mat = raf.readInt();
                String nome = c.leggiStringaDalFile(raf);
                String cognome = c.leggiStringaDalFile(raf);
                int anno = raf.readInt();

                if (mat != matricolaDaRimuovere) {
                    tempRaf.writeInt(mat);
                    tempRaf.writeChars(c.aggiustaLunghezzaStringa(nome));
                    tempRaf.writeChars(c.aggiustaLunghezzaStringa(cognome));
                    tempRaf.writeInt(anno);
                } else {
                    trovato = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante l'eliminazione: " + e.getMessage());
            return false;
        }

        if (trovato) {
            if (fileOriginale.delete()) {
                fileTemp.renameTo(fileOriginale);
                System.out.println("Studente " + matricolaDaRimuovere + " rimosso con successo.");
                return true;
            } else {
                System.out.println("Impossibile eliminare il file originale.");
            }
        } else {
            fileTemp.delete();
            System.out.println("Studente con matricola " + matricolaDaRimuovere + " non trovato.");
        }
        return false;
    }
}
