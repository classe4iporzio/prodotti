package gestioneelencofxml.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/*
Classe model che rispecchia un record presente sul database
Utilizza proprietà di tipi JavaFX utili per sincronizzare 
ciò che visualizzano gli elementi della GUI con i dati presenti in memoria
*/
public class Categoria {
    
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty nome = new SimpleStringProperty(); 
    
    // costruttore vuoto
    public Categoria() {  }
    
    // costruttore da un insieme di dati
    public Categoria(
            int id,
            String nome
    ) {
        this.id.set(id);
        this.nome.set(nome);
    }
    
    // costruttore da un ResultSet estratto dal database
    public Categoria(ResultSet resultSet) {
        try {
            //if(resultSet.isBeforeFirst()) {
                //resultSet.next();
                
                this.id.set(resultSet.getInt("ID_Categoria"));
                this.nome.set(resultSet.getString("NomeCategoria"));
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
    
    // metodi set
    public void setId(int id) {
        this.id.set(id);
    }
    public void setNome(String nome) {
        this.nome.set(nome);
    }
    
    // metodo toString
    @Override
    public String toString() {
        return this.nome.get();
    }
}
