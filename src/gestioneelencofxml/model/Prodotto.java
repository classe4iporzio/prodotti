package gestioneelencofxml.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/*
Classe model che rispecchia un record presente sul database
Utilizza proprietà di tipi JavaFX utili per sincronizzare 
ciò che visualizzano gli elementi della GUI con i dati presenti in memoria
*/
public class Prodotto{
    
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty nome = new SimpleStringProperty(); 
    private SimpleIntegerProperty prezzo = new SimpleIntegerProperty(); 
    private SimpleObjectProperty<Date> data = new SimpleObjectProperty(); 
    private SimpleStringProperty descrizione = new SimpleStringProperty(); 
    
    // costruttore vuoto
    public Prodotto() {  }
    
    // costruttore da un insieme di dati
    public Prodotto(
            int id,
            String nome,
            int prezzo,
            Date data,
            String descrizione
    ) {
        this.id.set(id);
        this.nome.set(nome);
        this.prezzo.set(prezzo);
        this.data.set(data);
        this.descrizione.set(descrizione);
    }
    
    // costruttore da un ResultSet estratto dal database
    public Prodotto(ResultSet resultSet) {
        try {
            //if(resultSet.isBeforeFirst()) {
                //resultSet.next();
                
                this.id.set(resultSet.getInt("ID_Utente"));
                this.nome.set(resultSet.getString("NomeProdotto"));
                this.prezzo.set(resultSet.getInt("PrezzoUnitario"));
                this.data.set(resultSet.getDate("DataScadenza"));
                this.descrizione.set(resultSet.getString("Descrizione"));
            //}
        }
        catch(SQLException ex) {
            System.out.println("Errore SQL: " + ex.getMessage());
            System.exit(0);
        }
    }
    
    // metodi get
    public int getId() {
        return this.id.get();
    }
    public String getNome() {
        return this.nome.get();
    }
    public int getPrezzo() {
        return this.prezzo.get();
    }
    public Date getData() {
        return this.data.get();
    }
    public String getDescrizione() {
        return this.descrizione.get();
    }
    
    // metodi set
    public void setId(int id) {
        this.id.set(id);
    }
    public void setNome(String nome) {
        this.nome.set(nome);
    }
    public void setPrezzo(int prezzo) {
        this.prezzo.set(prezzo);
    }
    public void setData(Date data) {
        this.data.set(data);
    }
    public void setDescrizione(String descrizione) {
        this.descrizione.set(descrizione);
    }
    
}
