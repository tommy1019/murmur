package me.murmurchat.client.GUI;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import me.murmurchat.client.AccountDatabase;
import me.murmurchat.client.Murmur;

public class UserInfoDialog 
{
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField displayNameField;

    @FXML
    private JFXButton doneButton;

    @FXML
    void initialize() 
    {
        doneButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
            	String displayName = displayNameField.getText();
            	
            	if(displayName != null)
            	{
            		Murmur.accountDatabase = new AccountDatabase(displayName);
            		
            		synchronized (Murmur.serverHandler)
					{
            			Murmur.serverHandler.notify();
					}
            	}
            	else
            	{
            		Alert alert = new Alert(AlertType.WARNING);
            		alert.setTitle("Invalid Display Name");
            		alert.setHeaderText(null);
            		alert.setContentText("Please enter a valid display name");

            		alert.showAndWait();
            	}
            }
        });
    }
}