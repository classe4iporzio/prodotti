package gestioneelencofxml.view;

import gestioneelencofxml.controller.FXMLDocumentController;
import gestioneelencofxml.controller.EditDialogFXMLController;
import gestioneelencofxml.controller.RootLayoutController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
Classe principale dell'applicazione
*/
public class GestioneElencoFXML extends Application {
    
    /*
    Metodo eseguito all'avvio dell'applicazione che carica la GUI,
    imposta la scena (scene) e la mette sul palcoscenico (stage)
    */
    @Override
    public void start(Stage stage) throws Exception {
        /* Creo un nuovo oggetto di tipo FXMLLoader */
        FXMLLoader loader;
                
        /*
        Carico l'FXML dell'interfaccia principale (di tipo Anchor Pane)
        e ottengo il suo controller assegnandolo ad una variabile anchorController
        */
        loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        AnchorPane anchor = (AnchorPane)loader.load();
        FXMLDocumentController anchorController = loader.getController();
        
        /*
        Carico l'FXML dell'interfaccia dei menu (di tipo Border Pane)
        e ottengo il suo controller assegnandolo ad una variabile borderController
        */
        loader = new FXMLLoader(getClass().getResource("RootLayout.fxml"));
        BorderPane border = (BorderPane)loader.load();
        RootLayoutController borderController = loader.getController();
        
        /*
        Carico l'FXML della finestra di dialogo (di tipo Dialog Pane)
        e ottengo il suo controller assegnandolo ad una variabile edController
        */
        loader = new FXMLLoader(getClass().getResource("EditDialogFXML.fxml"));
        DialogPane editDialog = (DialogPane)loader.load();
        EditDialogFXMLController edController = loader.getController();
        
        /*
        assegno anchorController come valore della proprietà controller 
        sia di borderController che di edController
        in modo che i controller dei menu e della finestra di dialogo 
        possano richiamare i metodi del controller dell'interfaccia principale
        */
        borderController.setParentController(anchorController);
        edController.setParentController(anchorController);
        
        /*
        allo stesso modo, assegno edController come valore della proprietà dialogController 
        di anchorController, in modo che anchorController 
        possa richiamare i metodi della finestra di dialogo
        */
        anchorController.setDialogController(edController);
        
        /*
        Crea una nuova scena basata sul Border Pane e vi pone al centro l'Anchor Pane
        */
        Scene scene = new Scene(border);
        border.setCenter(anchor);
        
        /*
        Imposta il titolo del palcoscenico e mostra la scena al suo interno
        */
        stage.setTitle("Gestione Elenco");
        stage.setScene(scene);
        stage.show();
        
        /*
        Predispone il palcoscenico per la finestra di dialogo
        */
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Modifica nominativo");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(stage);
        Scene dialogScene = new Scene(editDialog);
        dialogStage.setScene(dialogScene);
        
        /*
        Passa ad anchorController il dialogStage in modo che 
        all'occorrenza possa aprire la finestra di dialogo
        */
        anchorController.setDialogStage(dialogStage);
    }

    /*
    Metodo main dell'applicazione
    */
    public static void main(String[] args) {
        launch(args);
    }
    
}
