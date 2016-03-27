package me.murmurchat.client.GUI;

import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
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
	private JFXButton sendButton;

	@FXML
	private JFXListView<Contact> contactList;

	@FXML
	private TextArea messageLog;

	@FXML
	private JFXTextArea messageInput;

	@FXML
	private JFXTabPane tabPane;

	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	void initialize()
	{
		populateContactList();

		for (int i = 0; i < 3; i++)
		{
			Tab tab = new Tab();
			tab.setText("Tab " + i);
			tab.setContent(new Label("Content"));
			tab.setStyle("-fx-background-color:  rgb(3,169,244)");
			tabPane.getTabs().add(tab);
		}

		
		
		addContactButton.setOnAction(new EventHandler<ActionEvent>()
		{
			// Adds a new contact to the account database
			@Override
			public void handle(ActionEvent event)
			{
				// Ask the user for a public key
				TextInputDialog dialog1 = new TextInputDialog();
				dialog1.setTitle("Add Contact");
				dialog1.setHeaderText(null);
				dialog1.setContentText("Please enter a public key: ");
				
				TextInputDialog dialog2 = new TextInputDialog();
				dialog2.setTitle("Add Contact");
				dialog2.setHeaderText(null);
				dialog2.setContentText("Please enter a display name: ");

				// Get the user's input
				Optional<String> result1 = dialog1.showAndWait();
				Optional<String> result2 = dialog2.showAndWait();
				if (result1.isPresent() && result2.isPresent())
				{
					String publicKey = result1.get();
					BigInteger key = new BigInteger(publicKey, 36);
					
					Murmur.accountDatabase.addContact(key.toByteArray(), result2.get());
					populateContactList();
				}
			}
		});

		messageInput.setOnKeyReleased(new EventHandler<KeyEvent>()
		{
			// If the enter key is pressed, send the message in the input box
			final KeyCombination combo = new KeyCodeCombination(KeyCode.ENTER);

			public void handle(KeyEvent ke)
			{
				if (combo.match(ke))
				{
					sendMessage();
				}
			}
		});

		sendButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				sendMessage();
			}
		});
	}

	public void sendMessage()
	{
		// Get the text from the input box, add it to the message log, and clear
		// the input box
		String message = messageInput.getText();
		messageInput.setText("");

		// Make sure there is a new line between every new entry
		String newLine = System.getProperty("line.separator");

		messageLog.appendText(newLine);
		messageLog.appendText(message);
	}

	public void populateContactList()
	{
		Murmur.mainWindowController = this;
		// Remove all contacts from the list before repopulating
		contactList.getItems().clear();

		// Fill the contact list with the contacts from accountDatabase
		ArrayList<Contact> contacts = Murmur.accountDatabase.getContacts();

		for (int i = 0; i < contacts.size(); i++)
		{
			contactList.getItems().add(contacts.get(i));
		}

		// Not really sure what this does
		contactList.getStyleClass().add("mylistview");
	}
}
