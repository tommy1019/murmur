package me.murmurchat.client.GUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import me.murmurchat.client.Murmur;
import me.murmurchat.client.Profile;

public class ProfileChooser
{
	public static final String PREFERENCES_PROFILE_PATH = "PROFILE_PATH";
	
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
    	fileDirectory.setText(Murmur.preferences.get(PREFERENCES_PROFILE_PATH, "File Path"));
    	
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
    	
        newProfileButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
            	// Make the user choose a .profile file to use
            	
            	FileChooser fileChooser = new FileChooser();
            	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Profiles (*.profile)", "*.profile");
            	fileChooser.getExtensionFilters().add(extFilter);
            	selectedFile = fileChooser.showSaveDialog(null);
            	 
            	if (selectedFile != null) 
            	{
            		fileDirectory.setText(selectedFile.getPath());
            		
            		try
            		{
            			Profile.createNewProfile(selectedFile.getPath());
            		}
            		catch(Exception e)
            		{
            			System.out.println("Exception while creating new profile!");
            			e.printStackTrace();
            		}
            	}
            }
        });
        
        doneButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
            	// If the file is valid, use it to load the profile and start the server handler
            	// Then launch the program
            	if(selectedFile != null && selectedFile.exists() && fileDirectory.getText().endsWith(".profile"))
            	{	
            		try 
            		{
						Murmur.profile = new Profile(selectedFile.getPath());
						Murmur.preferences.put(PREFERENCES_PROFILE_PATH, selectedFile.getPath());
					} 
            		catch (IOException e) 
            		{
            			System.out.println("Error while loading profile!");
						e.printStackTrace();
					}
        		 		
        			Murmur.serverHandler.start();
            	}
            	// If the file is invalid, pop a dialog and return to the profile chooser
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
