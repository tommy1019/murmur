package me.murmurchat.client.GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import me.murmurchat.client.Contact;
import me.murmurchat.client.Murmur;

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
    
    @FXML
    private TextArea messageLog;
    
    @FXML
    private JFXTextArea messageInput;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() 
    {    
    	populateContactList();

        addContactButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
            	// Ask the user for a public key
            	TextInputDialog dialog = new TextInputDialog();
            	dialog.setTitle("Add Contact");
            	dialog.setHeaderText(null);
            	dialog.setContentText("Please enter a public key: ");

            	// Get the user's input
            	Optional<String> result = dialog.showAndWait();
            	if (result.isPresent())
            	{
            	    String publicKey = result.get();
            	    byte[] bytes = publicKey.getBytes();
            	    Murmur.accountDatabase.addContact(bytes);
            	    populateContactList();
            	}
            }
        });
    }
    
    public void populateContactList()
    {
    	// Remove all contacts from the list before repopulating
    	contactList.getItems().clear();
    	
    	// Fills the contact list with the contacts from accountDatabase
    	ArrayList<Contact> contacts = Murmur.accountDatabase.getContacts();
    	
    	for(int i = 0 ; i < contacts.size() ; i++)
    	{
    			contactList.getItems().add(contacts.get(i));
    	}
    	contactList.getStyleClass().add("mylistview");
    }
}
