/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abenantelutzengestionegara;

/**
 *
 * @author lutzen.jacopo
 */
public class Studente {

    private String nome;
    private String cognome;
    private int matricola;
    private int anno;
    
    
/**
 * costruttore 
 * @param nome nome dello studente 
 * @param cognome cognome dello studente 
 * @param matricola matricola dello studente 
 * @param anno anno scolastico dello studente 
 */
    public Studente(String nome, String cognome, int matricola, int anno) {
        this.nome = nome;
        this.cognome = cognome;
        this.matricola = matricola;
        this.anno = anno;

    }

    
    
 // Get & Set
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setMatricola(int matricola) {
        this.matricola = matricola;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public int getMatricola() {
        return matricola;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.matricola;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Studente other = (Studente) obj;
        return this.matricola == other.matricola;
    }

    @Override
    public String toString() {
        return " " + nome + " " + cognome + " con matricola " + matricola;
    }

}
