/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abenantelutzengestionegara;

/**
 *
 * @author lutzen.jacopo
 */
public class Gita {
    
    private int id;
    private String localita;
    private int durata;
/**
 * costruttore 
 * @param id id della gita
 * @param localita località della gita 
 * @param durata durata in giorni della gita 
 */
    public Gita(int id, String localita, int durata) {
        this.id = id;
        this.localita = localita;
        this.durata = durata;
    }

    
    // Get & Set
    public int getId() {
        return id;
    }

    public String getLocalita() {
        return localita;
    }

    public int getDurata() {
        return durata;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.id;
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
        final Gita other = (Gita) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Gita con id=" + id + " a " + localita + " di durata " + durata + " giorni";
    }
    
    
}
