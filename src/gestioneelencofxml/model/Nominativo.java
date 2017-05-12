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
public class Nominativo {
    
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty nome = new SimpleStringProperty(); 
    private SimpleStringProperty cognome = new SimpleStringProperty(); 
    private SimpleObjectProperty<Date> dataNascita = new SimpleObjectProperty(); 
    private SimpleStringProperty email = new SimpleStringProperty(); 
    private SimpleBooleanProperty attivo = new SimpleBooleanProperty();
    private SimpleIntegerProperty idCategoria = new SimpleIntegerProperty();
    
    // costruttore vuoto
    public Nominativo() {  }
    
    // costruttore da un insieme di dati
    public Nominativo(
            int id,
            String nome,
            String cognome,
            Date dataNascita,
            String email,
            boolean attivo,
            int idCategoria
    ) {
        this.id.set(id);
        this.nome.set(nome);
        this.cognome.set(cognome);
        this.dataNascita.set(dataNascita);
        this.email.set(email);
        this.attivo.set(attivo);
        this.idCategoria.set(idCategoria);
    }
    
    // costruttore da un ResultSet estratto dal database
    public Nominativo(ResultSet resultSet) {
        try {
            //if(resultSet.isBeforeFirst()) {
                //resultSet.next();
                
                this.id.set(resultSet.getInt("ID_Utente"));
                this.nome.set(resultSet.getString("Nome"));
                this.cognome.set(resultSet.getString("Cognome"));
                this.dataNascita.set(resultSet.getDate("DataNascita"));
                this.email.set(resultSet.getString("Email"));
                this.attivo.set(resultSet.getBoolean("Attivo"));
                this.idCategoria.set(resultSet.getInt("ID_Categoria"));
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
    public String getCognome() {
        return this.cognome.get();
    }
    public Date getDataNascita() {
        return this.dataNascita.get();
    }
    public String getEmail() {
        return this.email.get();
    }
    public boolean isAttivo() {
        return this.attivo.get();
    }
    public int getIdCategoria() {
        return this.idCategoria.get();
    }
    
    // metodi set
    public void setId(int id) {
        this.id.set(id);
    }
    public void setNome(String nome) {
        this.nome.set(nome);
    }
    public void setCognome(String cognome) {
        this.cognome.set(cognome);
    }
    public void setDataNascita(Date dataNascita) {
        this.dataNascita.set(dataNascita);
    }
    public void setEmail(String email) {
        this.email.set(email);
    }
    public void setAttivo(boolean attivo) {
        this.attivo.set(attivo);
    }
    public void setIdCategoria(int id) {
        this.idCategoria.set(id);
    }
    
}
