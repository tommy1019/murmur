package me.murmurchat.client.GUI;

import me.murmurchat.client.Conversation;
import javafx.scene.control.Tab;

public class ConversationTab extends Tab
{
	public Conversation conversation;
	
	public ConversationTab(Conversation c)
	{
		conversation = c;
	}
}
