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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/*
Classe controller che gestisce la GUI realizzata con FXMLDocument.fxml
Va associata alla GUI tramite Scene Builder (pannello a sx > Controller)
o direttamente nel codice di FXMLDocument.fxml
Può essere aggiornata automaticamente cliccando col dx su FXMLDocument.fxml
nell'albero del progetto Netbeans/Eclipse e scegliendo Make Controller
*/
public class FXMLDocumentController implements Initializable {
    
    private final DBMSControl dbmsControl = new DBMSControl();
    private Nominativo nominativo;
    protected ObservableList<Nominativo> elenco;
    
    private EditDialogFXMLController dialogController;
    protected Stage dialogStage;
    
    /*
    Tutti gli elementi della GUI FXML e i rispettivi metodi gestori degli eventi
    vanno registrati con @FXML
    */
    
    @FXML 
    protected TableView<Nominativo> tableview;
    @FXML 
    private TableColumn colId;
    @FXML 
    private TableColumn<Nominativo,String> colNome;
    @FXML 
    private TableColumn<Nominativo,String> colCognome;
    @FXML
    private TableColumn<Nominativo,String> colEmail;
    @FXML
    private TableColumn<Nominativo,String> colDataNascita;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCognome;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpkDataNascita;
    @FXML
    private CheckBox chkAttivo;
    @FXML
    private Button btnNuovo;
    @FXML
    private Button btnSalva;
    @FXML
    private Button btnElimina;
    @FXML
    private ComboBox<Categoria> cmbCategoria;

    /*
    Metodo che viene eseguito quando viene inizializzata la GUI
    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        Associamo le colonne della TableView ai campi degli oggetti visualizzati
        */
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colNome.setCellValueFactory(new PropertyValueFactory("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory("cognome"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colDataNascita.setCellValueFactory(new PropertyValueFactory("dataNascita"));
        
        /*
        Inizializziamo l'observableArrayList di oggetti di tipo Nominativo
        con la quale riempiremo la TableView
        */
        elenco = FXCollections.observableArrayList();
        mostraInTabella();
        
        /*
        Riempiamo il menu a discesa che consente di selezionare le categorie
        */
        riempiMenuCategorie();
    }    
    
    public void setDialogController(EditDialogFXMLController dialogController) {
        this.dialogController = dialogController;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    private void mostraInTabella(){
        /*
        Interroga il database, per ogni ResultSet crea un oggetto Nominativo,
        lo inserisce nella observableArrayList e configura con essa la TableView
        */
        dbmsControl.connetti();
        String query = "SELECT * FROM elenco ORDER BY ID_Utente ASC";
        try {
            ResultSet resultSet = dbmsControl.doQuery(query);
            while(resultSet.next()) {
                this.nominativo = new Nominativo(resultSet);
                elenco.add(nominativo);
            }
            
            tableview.setItems(elenco);
            
            /*
            Si mette in ascolto dei possibili eventi sulla TableView
            */
            ascoltaTabella();
        } 
        catch (SQLException ex) {
            System.out.println("Errore nell'esecuzione della query!");
        }
        dbmsControl.disconnetti();
    }
    
    private void ascoltaTabella() {
        riempiCampi(null);
        
        /*
        Listener (ascoltatore) che quando cambia la selezione delle righe della TableView
        richiama il riempimento dei campi sul pannello di sx della GUI
        */
        ChangeListener listener = new ChangeListener<Nominativo>() {
            @Override
            public void changed(ObservableValue<? extends Nominativo> observable, Nominativo vecchio, Nominativo nuovo) {
                riempiCampi(nuovo);
            }
        };
        
        tableview.getSelectionModel().selectedItemProperty().addListener(listener);
        
        /* OPPURE, con una lambda expression:
        tableview.getSelectionModel().selectedItemProperty().addListener(
            (observable, vecchio, nuovo) -> riempiCampi(nuovo)
        );
        */
        
        /*
        Intercetta il doppio click su una riga della tableView
        e richiama il metodo riempiCampi della finestra di dialogo
        */
        tableview.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() > 1) {
                    nominativo = tableview.getSelectionModel().getSelectedItem();
                    dialogController.riempiCampi(nominativo);
                    dialogStage.showAndWait();
                }
            }
        });
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
    
    /*
    Metodo gestore di eventi che azzera i campi nel pannello a sx della GUI e
    deselezione le righe sulla TableView
    */
    @FXML
    protected void nuovoNominativo() {
        riempiCampi(null);
        tableview.getSelectionModel().select(null);
    }
    
    /*
    Metodo gestore di eventi che salva sul database i dati inseriti nei campi
    sul pannello di sx della UI (INSERT o UPDATE) e aggiorna la TableView
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
        
        Alert alert = new Alert(AlertType.WARNING);
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

            int selectedIndex = tableview.getSelectionModel().getSelectedIndex();
            /*
            Controlla se sulla tabella è selezionata una riga (un record)...
            */
            if (selectedIndex >= 0) { // se sì, aggiorna un record esistente
                int id = tableview.getItems().get(selectedIndex).getId();

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
                nominativo = tableview.getSelectionModel().getSelectedItem();
                nominativo.setNome(nome);
                nominativo.setCognome(cognome);
                nominativo.setEmail(email);
                nominativo.setDataNascita(Date.valueOf(dataNascita));
                nominativo.setAttivo(attivo);
                nominativo.setIdCategoria(idCategoria);

                dbmsControl.connetti();
                dbmsControl.doUpdate(query);
                dbmsControl.disconnetti();
            }
            else { // altrimenti inserisce un nuovo record
                query = "INSERT INTO elenco (Nome, Cognome, Email, DataNascita, Attivo, ID_Categoria) "
                        + "VALUES ('" + nome + "', "
                        + "'" + cognome + "', "
                        + "'" + email + "', "
                        + "'" + dataNascitaS + "', "
                        + attivoS + ", " 
                        + idCategoria + ")";

                dbmsControl.connetti();
                dbmsControl.doUpdate(query);

                // recupera il valore della chiave primaria dell'ultimo record inserito
                ResultSet rs = dbmsControl.doQuery("SELECT last_insert_id() AS lid FROM elenco");
                try {
                    if(rs.next()) {
                        int lid = rs.getInt("lid");
                        nominativo = new Nominativo(
                                lid, 
                                nome, 
                                cognome, 
                                Date.valueOf(dataNascita), 
                                email, 
                                attivo,
                                idCategoria
                        );
                        elenco.add(nominativo);
                    }
                } 
                catch (SQLException ex) {
                    System.out.println("Errore nell'esecuzione della query!");
                }
                dbmsControl.disconnetti();
            }
            
            // aggiorna la tabella
            tableview.refresh();
        }
    }
    
    /*
    Metodo gestore di eventi che elimina dal database il record corrispondente
    alla riga selezionata sulla TableView e aggiorna la TableView
    */
    @FXML
    protected void eliminaNominativo() {
        int selectedIndex = tableview.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            int id = tableview.getItems().get(selectedIndex).getId();
            tableview.getItems().remove(selectedIndex);
                        
            dbmsControl.connetti();
            String query = "DELETE FROM elenco WHERE ID_Utente = " + id;
            dbmsControl.doUpdate(query);
            dbmsControl.disconnetti();
        }
    }
    
}
