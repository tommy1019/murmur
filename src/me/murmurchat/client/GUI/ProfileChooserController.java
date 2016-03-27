package me.murmurchat.client.GUI;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class ProfileChooserController
{
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML
    private JFXButton browseButton;
    
    @FXML
    private JFXButton doneButton;

    @FXML
    private JFXTextField fileDirectory;
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() 
    {
        browseButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
            	fileDirectory.setStyle("-fx-text-fill: white;");
  
            	FileChooser fileChooser = new FileChooser();
            	File selectedFile = fileChooser.showOpenDialog(null);
            	 
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
                GUI.launchMainWindow();
            }
        });
    }
}