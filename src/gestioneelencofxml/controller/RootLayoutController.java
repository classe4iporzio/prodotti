/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestioneelencofxml.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class RootLayoutController implements Initializable {

    @FXML
    private MenuItem menuNuovo;
    @FXML
    private MenuItem menuSalva;
    @FXML
    private MenuItem menuElimina;
    @FXML
    private MenuItem menuEsci;
    
    private FXMLDocumentController controller;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setParentController(FXMLDocumentController controller) {
        this.controller = controller;
    }

    @FXML
    private void nuovoNominativo(ActionEvent event) {
        controller.nuovoNominativo();
    }

    @FXML
    private void salvaNominativo(ActionEvent event) {
        controller.salvaNominativo();
    }

    @FXML
    private void eliminaNominativo(ActionEvent event) {
        controller.eliminaNominativo();
    }

    @FXML
    private void esci(ActionEvent event) {
        System.exit(1);
    }
    
}
