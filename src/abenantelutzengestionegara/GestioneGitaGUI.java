package abenantelutzengestionegara;

import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 * Finestra principale di gestione gite.
 * La GUI richiama solo metodi di LogicaGita — nessuna logica qui.
 *
 * MIGLIORAMENTO: rimossi i campi gF, gG, gS che erano istanziati
 * ma mai usati (codice morto) — LogicaGita gestisce tutto.
 */
public class GestioneGitaGUI extends javax.swing.JFrame {

    private LogicaGita l = new LogicaGita();

    public GestioneGitaGUI() {
        initComponents();
        l.inizializza();
        aggiornaComboGite();
        cbxGite.addActionListener(e -> aggiornaTabella());
    }

    /**
     * Ricarica la JComboBox con tutte le gite dal file.
     */
    public void aggiornaComboGite() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String voce : l.getVociComboGite()) {
            model.addElement(voce);
        }
        cbxGite.setModel(model);
        aggiornaTabella();
    }

    /**
     * Aggiorna la JTable mostrando gli studenti della gita selezionata.
     */
    public void aggiornaTabella() {
        DefaultTableModel model = (DefaultTableModel) JtblStudenti.getModel();
        model.setRowCount(0);

        if (cbxGite.getSelectedItem() == null) return;

        int idGita = l.estraiIdDaVoceCombo(cbxGite.getSelectedItem().toString());
        if (idGita == -1) return;

        ArrayList<Studente> studenti = l.getStudentiPerGita(idGita);
        for (Studente s : studenti) {
            model.addRow(new Object[]{s.getMatricola(), s.getNome(), s.getCognome(), s.getAnno()});
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btn_RimuoviGita = new javax.swing.JButton();
        lbl_GestioneGita = new javax.swing.JLabel();
        btn_RimuoviStudente = new javax.swing.JButton();
        btn_AggiungiGita = new javax.swing.JButton();
        btn_AggiungiStudente = new javax.swing.JButton();
        cbxGite = new javax.swing.JComboBox<>();
        lbl_GestioneGita1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JtblStudenti = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 0, 153));

        jPanel2.setBackground(new java.awt.Color(204, 153, 255));
        jPanel2.setLayout(null);

        btn_RimuoviGita.setFont(new java.awt.Font("MV Boli", 0, 18));
        btn_RimuoviGita.setText("Rimuovi Gita");
        btn_RimuoviGita.addActionListener(evt -> btn_RimuoviGitaActionPerformed(evt));
        jPanel2.add(btn_RimuoviGita);
        btn_RimuoviGita.setBounds(10, 150, 160, 60);

        lbl_GestioneGita.setFont(new java.awt.Font("MV Boli", 0, 24));
        lbl_GestioneGita.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_GestioneGita.setText("Gite");
        jPanel2.add(lbl_GestioneGita);
        lbl_GestioneGita.setBounds(10, 220, 330, 40);

        btn_RimuoviStudente.setFont(new java.awt.Font("MV Boli", 0, 14));
        btn_RimuoviStudente.setText("Rimuovi Studente");
        btn_RimuoviStudente.addActionListener(evt -> btn_RimuoviStudenteActionPerformed(evt));
        jPanel2.add(btn_RimuoviStudente);
        btn_RimuoviStudente.setBounds(180, 150, 160, 60);

        btn_AggiungiGita.setFont(new java.awt.Font("MV Boli", 0, 18));
        btn_AggiungiGita.setText("Aggiungi Gita");
        btn_AggiungiGita.addActionListener(evt -> btn_AggiungiGitaActionPerformed(evt));
        jPanel2.add(btn_AggiungiGita);
        btn_AggiungiGita.setBounds(10, 80, 160, 60);

        btn_AggiungiStudente.setFont(new java.awt.Font("MV Boli", 0, 14));
        btn_AggiungiStudente.setText("Aggiungi Studente");
        btn_AggiungiStudente.addActionListener(evt -> btn_AggiungiStudenteActionPerformed(evt));
        jPanel2.add(btn_AggiungiStudente);
        btn_AggiungiStudente.setBounds(180, 80, 160, 60);

        cbxGite.setFont(new java.awt.Font("MV Boli", 0, 14));
        jPanel2.add(cbxGite);
        cbxGite.setBounds(10, 270, 330, 30);

        lbl_GestioneGita1.setFont(new java.awt.Font("MV Boli", 0, 24));
        lbl_GestioneGita1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_GestioneGita1.setText("Gestione Gita");
        jPanel2.add(lbl_GestioneGita1);
        lbl_GestioneGita1.setBounds(10, 10, 330, 40);

        JtblStudenti.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {{null, null, null, null}},
            new String[]{"Matricola", "Nome", "Cognome", "Anno"}
        ));
        jScrollPane1.setViewportView(JtblStudenti);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_RimuoviGitaActionPerformed(java.awt.event.ActionEvent evt) {
        if (cbxGite.getSelectedItem() == null) { mostraMessaggio("Nessuna gita selezionata."); return; }
        int idGita = l.estraiIdDaVoceCombo(cbxGite.getSelectedItem().toString());
        String esito = l.rimuoviGita(idGita);
        mostraMessaggio(esito.replace("OK: ", "").replace("ERRORE: ", ""));
        if (esito.startsWith("OK")) aggiornaComboGite();
    }

    private void btn_RimuoviStudenteActionPerformed(java.awt.event.ActionEvent evt) {
        int rigaSelezionata = JtblStudenti.getSelectedRow();
        if (rigaSelezionata == -1) { mostraMessaggio("Seleziona uno studente dalla tabella."); return; }
        if (cbxGite.getSelectedItem() == null) { mostraMessaggio("Seleziona prima una gita."); return; }

        int matricola = Integer.parseInt(JtblStudenti.getValueAt(rigaSelezionata, 0).toString());
        int idGita = l.estraiIdDaVoceCombo(cbxGite.getSelectedItem().toString());

        String esito = l.rimuoviStudenteDaGita(matricola, idGita);
        mostraMessaggio(esito.replace("OK: ", "").replace("ERRORE: ", ""));
        if (esito.startsWith("OK")) aggiornaTabella();
    }

    private void btn_AggiungiGitaActionPerformed(java.awt.event.ActionEvent evt) {
        new AggiungiGitaGUI(l, this).setVisible(true);
    }

    private void btn_AggiungiStudenteActionPerformed(java.awt.event.ActionEvent evt) {
        if (!l.esistonoGite()) { mostraMessaggio("Non ci sono gite disponibili. Aggiungi prima una gita."); return; }
        new AggiungiStudenteGUI(l, this).setVisible(true);
    }

    private void mostraMessaggio(String msg) {
        javax.swing.JOptionPane.showMessageDialog(this, msg);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestioneGitaGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new GestioneGitaGUI().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable JtblStudenti;
    private javax.swing.JButton btn_AggiungiGita;
    private javax.swing.JButton btn_AggiungiStudente;
    private javax.swing.JButton btn_RimuoviGita;
    private javax.swing.JButton btn_RimuoviStudente;
    private javax.swing.JComboBox<String> cbxGite;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_GestioneGita;
    private javax.swing.JLabel lbl_GestioneGita1;
    // End of variables declaration//GEN-END:variables
}
