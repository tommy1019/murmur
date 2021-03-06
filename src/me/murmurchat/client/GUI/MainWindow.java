package me.murmurchat.client.GUI;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import me.murmurchat.client.Contact;
import me.murmurchat.client.Conversation;
import me.murmurchat.client.Message;
import me.murmurchat.client.Murmur;

public class MainWindow
{
	public MainWindow()
	{
		Murmur.mainWindowController = this;
	}

	@FXML
	private JFXButton addContactButton;

	@FXML
	private JFXButton sendButton;
	
	@FXML
	private JFXButton getKeyButton;

	@FXML
	private JFXListView<Contact> contactList;

	@FXML
	private TextArea messageLog;

	@FXML
	private JFXTextArea messageInput;
	
	@FXML
	private JFXTextField currentProfile;

	@FXML
	private JFXTabPane tabPane;
	
	@FXML
	private Pane bluePane;
	
	public static Conversation currentConversation;
	
	//public static ArrayList<ConversationTab> openedTabs = new ArrayList<ConversationTab>();
	
	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	void initialize()
	{	
		populateContactList();
		
		currentProfile.setText("Logged In As " + Murmur.accountDatabase.displayName);
		
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
		
		getKeyButton.setOnAction(new EventHandler<ActionEvent>()
		{
			// Copies the user's public key to the clipboard
			@Override
			public void handle(ActionEvent event)
			{
		        final Clipboard clipboard = Clipboard.getSystemClipboard();
		        final ClipboardContent content = new ClipboardContent();
		        content.putString(Murmur.profile.getPublicKey());
		        clipboard.setContent(content);
		        
        		Alert alert = new Alert(AlertType.WARNING);
        		alert.setTitle("Key Copied");
        		alert.setHeaderText(null);
        		alert.setContentText("Your public key has been copied to the clipboard");

        		alert.showAndWait();
			}
		});
		
		contactList.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent me) 
			{
				boolean tabAlreadyOpen = false;
				
				// If there is already a tab for this contact, switch to that tab
				for(Tab t: tabPane.getTabs())
				{
					ConversationTab tab = (ConversationTab)t;
					
					// Look for a tab with the same name as the contact
					if(tab.conversation.contacts.get(0) == contactList.getSelectionModel().getSelectedItem())
					{
						tabPane.getSelectionModel().select(tab);
						tabAlreadyOpen = true;
						break;
					}
				}
				
				// Otherwise create a new conversation with the contact
				if(!tabAlreadyOpen)
				{
					Conversation conversation = new Conversation();
					conversation.generateNewConversation();
					conversation.contacts.add(contactList.getSelectionModel().getSelectedItem());

					createTab(conversation);
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

	public void updateMessageLog()
	{
		clearMessageLog();
	}
	
	public void clearMessageLog()
	{
		messageLog.setText("");
	}
	
	public void receiveMessage(String message)
	{		
		// Make sure there is a new line between every new entry
		String newLine = System.getProperty("line.separator");

		messageLog.appendText(newLine);
		messageLog.appendText(message);
	}
	
	public void sendMessage()
	{
		// Get the text from the input box, add it to the message log, and clear
		// the input box
		Message message = new Message(messageInput.getText());
		clearMessageLog();
		
		Murmur.serverHandler.sendMessage(currentConversation, message);

		// Make sure there is a new line between every new entry
		String newLine = System.getProperty("line.separator");

		messageLog.appendText(newLine);
		messageLog.appendText(message.getText());
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

		// Not really sure what this does, but I'm scared to delete it
		contactList.getStyleClass().add("mylistview");
	}
	
	public void createTab(Conversation c)
	{
		ConversationTab tab = new ConversationTab(c);
		
		tab.setText(tab.conversation.contacts.get(0).getDisplayName());
		
		tabPane.getTabs().add(tab);
		
		bluePane.setOpacity(0);
		
		switchTab(tab);
	}
	
	public void switchTab(ConversationTab t)
	{
		tabPane.getSelectionModel().select(t);
		currentConversation = t.conversation;
	}
	
	public void closeTab(ConversationTab t)
	{
		tabPane.getTabs().remove(t);
	}
}
