/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package projetstadev1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
 *
 * @author Ayoub Zerdoum
 */
public class ProjetStadeV1 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        try{
            //Parent root = FXMLLoader.load(getClass().getResource("/Vues/StadeBuilder.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource("/Vues/EventManager.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource("/Vues/EventClient.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource("/Vues/StadeViewer.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/Vues/LoginPage.fxml"));
    
        
            Scene scene = new Scene(root);

            primaryStage.setTitle("Hello World!");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex){
            Logger.getLogger(ProjetStadeV1.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
