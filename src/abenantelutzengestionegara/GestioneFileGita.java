package abenantelutzengestionegara;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Gestisce lettura e scrittura del file binario "elencoGite.pdm".
 * Record: [int id (4)] [String localita (40)] [int durata (4)] = 48 byte
 */
public class GestioneFileGita {

    private static final String FILE_GITE = "elencoGite.pdm";
    private final int dimId = 4;
    private final int dimLocalita = 40;
    private final int dimDurata = 4;
    private static final int DIM_RECORD = 48;
    private Controlli c = new Controlli();

    /**
     * Crea/apre il file gite.
     * BUGFIX: prima apriva erroneamente "elencoStudenti.pdm"
     */
    public boolean creaFileGita() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_GITE, "rw")) {
            int nRecord = (int) (file.length() / DIM_RECORD);
            System.out.println("File gite creato/aperto con successo.");
            System.out.println("Record attualmente presenti: " + nRecord);
            return true;
        } catch (IOException e) {
            System.out.println("Problema in creazione/lettura file gite: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserisce una nuova gita in fondo al file.
     */
    public boolean inserisciGita(int id, String localita, int durata) {
        try (RandomAccessFile file = new RandomAccessFile(FILE_GITE, "rw")) {
            file.seek(file.length());
            file.writeInt(id);
            file.writeChars(c.aggiustaLunghezzaStringa(localita));
            file.writeInt(durata);
            System.out.println("Gita " + id + " - " + localita + " salvata con successo!");
            return true;
        } catch (IOException e) {
            System.out.println("Errore durante la scrittura: " + e.getMessage());
            return false;
        }
    }

    /**
     * Legge e stampa una gita in base alla posizione (1-based).
     */
    public boolean leggiGita(int posizione) {
        try (RandomAccessFile file = new RandomAccessFile(FILE_GITE, "r")) {
            long posizioneByte = (long) (posizione - 1) * DIM_RECORD;
            if (posizioneByte >= file.length()) {
                System.out.println("Nessun record trovato in questa posizione.");
                return false;
            }
            file.seek(posizioneByte);
            int id = file.readInt();
            String localita = c.leggiStringaDalFile(file);
            int durata = file.readInt();
            System.out.println("--- Gita trovata ---");
            System.out.println("Id: " + id + " | Localita: " + localita + " | Durata: " + durata);
            return true;
        } catch (IOException e) {
            System.out.println("Errore in lettura: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carica tutte le gite dal file e le restituisce come ArrayList.
     * Centralizza la lettura: LogicaGita usa questo metodo invece di rileggere il file da zero.
     */
    public ArrayList<Gita> caricaTutteLeGite() {
        ArrayList<Gita> lista = new ArrayList<>();
        java.io.File file = new java.io.File(FILE_GITE);
        if (!file.exists()) return lista;

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long nRecord = raf.length() / DIM_RECORD;
            for (int i = 0; i < nRecord; i++) {
                raf.seek((long) i * DIM_RECORD);
                int id = raf.readInt();
                String localita = c.leggiStringaDalFile(raf);
                int durata = raf.readInt();
                lista.add(new Gita(id, localita, durata));
            }
        } catch (IOException e) {
            System.out.println("Errore lettura gite: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Rimuove una gita dal file tramite file temporaneo.
     */
    public boolean rimuoviGita(int idDaRimuovere) {
        java.io.File fileOriginale = new java.io.File(FILE_GITE);
        java.io.File fileTemp = new java.io.File("tempGite.pdm");
        boolean trovato = false;

        try (RandomAccessFile raf = new RandomAccessFile(fileOriginale, "rw");
             RandomAccessFile tempRaf = new RandomAccessFile(fileTemp, "rw")) {

            long nRecord = raf.length() / DIM_RECORD;
            for (int i = 0; i < nRecord; i++) {
                raf.seek((long) i * DIM_RECORD);
                int currentId = raf.readInt();
                String localita = c.leggiStringaDalFile(raf);
                int durata = raf.readInt();

                if (currentId != idDaRimuovere) {
                    tempRaf.writeInt(currentId);
                    tempRaf.writeChars(c.aggiustaLunghezzaStringa(localita));
                    tempRaf.writeInt(durata);
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
                System.out.println("Gita " + idDaRimuovere + " rimossa con successo.");
                return true;
            } else {
                System.out.println("Impossibile eliminare il file originale.");
            }
        } else {
            fileTemp.delete();
            System.out.println("Gita con ID " + idDaRimuovere + " non trovata.");
        }
        return false;
    }
}
