package me.murmurchat.client.GUI;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import me.murmurchat.client.Crypt;
import me.murmurchat.client.Murmur;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProfileChooserController
{
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML
    private JFXButton browseButton;
    
    @FXML
    private JFXButton newProfileButton;
    
    @FXML
    private JFXButton doneButton;

    @FXML
    private JFXTextField fileDirectory;
    
    private File selectedFile;
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() 
    {
    	fileDirectory.setStyle("-fx-text-fill: white;");
    	
        browseButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
            	// Make the user choose a .profile file to use
            	
            	FileChooser fileChooser = new FileChooser();
            	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Profiles (*.profile)", "*.profile");
            	fileChooser.getExtensionFilters().add(extFilter);
            	selectedFile = fileChooser.showOpenDialog(null);
            	 
            	if (selectedFile != null) 
            	{
            		fileDirectory.setText(selectedFile.getPath());
            	}
            }
        });
    	
        doneButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
            	if(selectedFile != null && fileDirectory.getText().endsWith(".profile"))
            	{
            		Murmur.crypt = new Crypt(selectedFile.getPath());
        		
        			Murmur.serverHandler.start();
        			
        			GUI.launchMainWindow();
            	}
            	else
            	{
            		Alert alert = new Alert(AlertType.WARNING);
            		alert.setTitle("Invalid Profile");
            		alert.setHeaderText(null);
            		alert.setContentText("Please select a valid profile");

            		alert.showAndWait();
            	}
            }
        });
    }
}
