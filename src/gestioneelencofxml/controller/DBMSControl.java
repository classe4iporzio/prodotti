package gestioneelencofxml.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
Classe controller utilizzabile per gestire le operazioni con il DBMS
*/
public class DBMSControl {
    
    private Connection connection;
    private Statement statement;
    
    private final String connectionUrl = "jdbc:mysql://localhost:3306/test";
    private final String user = "test";
    private final String password = "test";

    // costruttore vuoto 
    public DBMSControl() {}
    
    public void connetti() {
        System.out.print("Connessione al DBMS in corso... ");
        try {
            connection = DriverManager.getConnection(connectionUrl, user, password);
            statement = connection.createStatement();
            System.out.println("connesso!");
        } 
        catch (SQLException ex) {
            System.out.println("Errore di connessione al DBMS, l'applicazione verrà terminata");
            System.exit(0);
        }
    }

    public void disconnetti() {
        System.out.print("Disconnessione dal DBMS in corso... ");
        try {
            statement.close();
            connection.close();
            System.out.println("disconnesso!");
        } 
        catch (SQLException ex) {
            System.out.println("Errore nella disconnessione dal DBMS!");
        }
    }
    
    /*
    Esegue una interrogazione al database, ovvero una SELECT
    */
    public ResultSet doQuery(String query) {
        System.out.println("Eseguo: " + query);
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
            return resultSet;
        } 
        catch (SQLException ex) {
            System.out.println("Errore nell'esecuzione della query!");
        }
        return resultSet;
    }
    
    /*
    Esegue un aggiornamento del database, ovvero un INSERT, un DELETE o un UPDATE
    */
    public void doUpdate(String query) {
        System.out.println("Eseguo: " + query);
        try {
            statement.executeUpdate(query);
        } 
        catch (SQLException ex) {
            System.out.println("Errore nell'esecuzione della query!");
        }
    }
    
    /*
    Restituisce il numero di record presente in un ResultSet
    */
    public int getNumeroRighe(ResultSet resultSet) {
        int size;
        try {
            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();
        }
        catch(Exception ex) {
            return 0;
        }
        return size;
    }

    
}
