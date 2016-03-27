package me.murmurchat.client.GUI;

import java.net.URL;
import java.util.ResourceBundle;

import me.murmurchat.client.Contact;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

public class MainWindowController 
{
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML
    private JFXButton addContactButton;
    
    @FXML
    private JFXListView<Contact> contactList;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() 
    {
        addContactButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                System.out.println("Add contact!");
            }
        });
    }
}
