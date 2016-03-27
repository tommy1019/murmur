package me.murmurchat.client.GUI;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MurmurGUI extends Application 
{
    @Override
    public void start(Stage primaryStage) 
    {
        try 
        {
            StackPane page = (StackPane) FXMLLoader.load(MurmurGUI.class.getResource("MurmurGUI.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Murmur Client");
            primaryStage.show();
            
            launchLoginDialog();
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(MurmurGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void launchLoginDialog()
    {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Choose User Profile");
    	alert.setHeaderText("Choose a user profile");
    	//alert.setContentText("Choose your option.");

    	ButtonType buttonTypeOne = new ButtonType("Use last");
    	ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

    	alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == buttonTypeOne)
    	{
    	    // User chose to use the last profile
    		
    	} 
    	else 
    	{
    	    // User chose to cancel and exit the program
    		Platform.exit();
    	}
    }
}