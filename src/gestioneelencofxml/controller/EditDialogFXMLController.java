/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestioneelencofxml.controller;

import gestioneelencofxml.model.Categoria;
import gestioneelencofxml.model.Nominativo;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class EditDialogFXMLController implements Initializable {
    
    private FXMLDocumentController controller;
    
    private final DBMSControl dbmsControl = new DBMSControl();
    private Nominativo nominativo;
    
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCognome;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpkDataNascita;
    @FXML
    private ComboBox<Categoria> cmbCategoria;
    @FXML
    private CheckBox chkAttivo;
    @FXML
    private Button btnSalva;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        riempiMenuCategorie();
    }    
    
    public void setParentController(FXMLDocumentController controller) {
        this.controller = controller;
    }

    /*
    Metodo gestore di eventi che salva sul database i dati inseriti nel form
    */
    @FXML
    protected void salvaNominativo() {
        String query;

        String nome = txtNome.getText().trim();
        String cognome = txtCognome.getText().trim();
        String email = txtEmail.getText().trim();
        LocalDate dataNascita = dpkDataNascita.getValue();
        boolean attivo = chkAttivo.isSelected();
        int idCategoria = (cmbCategoria.getSelectionModel().isEmpty()) 
                ? 0 
                : cmbCategoria.getSelectionModel().getSelectedItem().getId();
        
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attenzione!");
        alert.setHeaderText("Dati incompleti");
        
        if(nome.length() == 0) {
            alert.setContentText("Nome non indicato!");
            alert.showAndWait();            
        }
        else if(cognome.length() == 0) {
            alert.setContentText("Cognome non indicato!");
            alert.showAndWait();            
        }
        else if(email.length() == 0) {
            alert.setContentText("Indirizzo email non indicato!");
            alert.showAndWait();            
        }
        else if(dataNascita == null) {
            alert.setContentText("Data di nascita non indicata!");
            alert.showAndWait();            
        }
        else if(idCategoria == 0) {
            alert.setContentText("Categoria non indicata!");
            alert.showAndWait();            
        }
        else { // se tutti i controlli sono stati superati...
            /*
            Formatta come stringa la LocalDate letta dal DatePicker
            */
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dataNascitaS = dataNascita.format(formatter);
            /*
            Formatta come stringa il booleano ottenuto dalla CheckBox
            */
            String attivoS = attivo ? "true" : "false";

            int selectedIndex = controller.tableview.getSelectionModel().getSelectedIndex();
            /*
            Controlla se sulla tabella è selezionata una riga (un record)...
            */
            if (selectedIndex >= 0) { // se sì, aggiorna un record esistente
                int id = controller.tableview.getItems().get(selectedIndex).getId();

                query = "UPDATE elenco SET "
                            + "Nome = '" + nome + "', "
                            + "Cognome = '" + cognome + "', "
                            + "Email = '" + email + "', "
                            + "DataNascita = '" + dataNascitaS + "', "
                            + "Attivo = " + attivoS + ", "
                            + "ID_Categoria = " + idCategoria + " "
                            + "WHERE ID_Utente = " + id;

                /*
                aggiorna anche il nominativo selezionato nella tableview
                */
                nominativo = controller.tableview.getSelectionModel().getSelectedItem();
                nominativo.setNome(nome);
                nominativo.setCognome(cognome);
                nominativo.setEmail(email);
                nominativo.setDataNascita(Date.valueOf(dataNascita));
                nominativo.setAttivo(attivo);
                nominativo.setIdCategoria(idCategoria);

                dbmsControl.connetti();
                dbmsControl.doUpdate(query);
                dbmsControl.disconnetti();
                
                // aggiorna i valori nel form della finestra principale
                controller.riempiCampi(nominativo);
                //chiude la finestra di dialogo
                controller.dialogStage.close();
                // aggiorna la tabella della finestra principale
                controller.tableview.refresh();
            }
        }
    }
    
    /*
    Interroga il database, e per ogni ResultSet (record) aggiunge una voce alla ComboBox
    */
    private void riempiMenuCategorie() {
        dbmsControl.connetti();
        String query = "SELECT * FROM categorie ORDER BY ID_Categoria ASC";
        try {
            ResultSet resultSet = dbmsControl.doQuery(query);
            while(resultSet.next()) {
                /*
                Per ciascun record crea un oggetto Categoria e lo aggiunge agli Items della ComboBox
                */
                Categoria categoria = new Categoria(resultSet);
                cmbCategoria.getItems().add(categoria);
            }
            cmbCategoria.getSelectionModel().select(0);
        } 
        catch (SQLException ex) {
            System.out.println("Errore nell'esecuzione della query!");
        }
        dbmsControl.disconnetti();        
    }

    /*
    Riempie i campi sul pannello a sx della GUI dopo aver formattato correttamente
    la data di nascita per poterla inserire nel DatePicker
    */
    protected void riempiCampi(Nominativo nominativo) {
        if(nominativo != null) {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dataNascita = LocalDate.parse(df.format(nominativo.getDataNascita()), formatter);
            
            txtNome.setText(nominativo.getNome());
            txtCognome.setText(nominativo.getCognome());
            txtEmail.setText(nominativo.getEmail());
            dpkDataNascita.setValue(dataNascita);
            chkAttivo.setSelected(nominativo.isAttivo());
            
            /*
            Cerca tra tutti gli items della ComboBox quello che ha id
            uguale all'idCategoria del record selezionato nella tableview
            */
            Categoria categoria = null;
            for(int i = 0; i < cmbCategoria.getItems().size(); i++) {
                categoria = cmbCategoria.getItems().get(i);
                if(categoria.getId() == nominativo.getIdCategoria()) {
                    break;
                }
            }
            cmbCategoria.setValue(categoria);
        }
        else {
            txtNome.setText("");
            txtCognome.setText("");
            txtEmail.setText("");
            dpkDataNascita.setValue(null);
            chkAttivo.setSelected(false);
            cmbCategoria.setValue(null);
        }
    }
}
