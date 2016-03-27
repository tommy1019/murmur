package me.murmurchat.client.GUI;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUI extends Application 
{
	static Stage primaryStage;
	
    @Override
    public void start(Stage stage) 
    {
    	primaryStage = stage;
    	
    	launchLoginDialog();
    }
    
    public static void launchLoginDialog()
    {
        try 
        {
            StackPane page = (StackPane) FXMLLoader.load(GUI.class.getResource("ProfileChooser.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Choose Login Profile");
            primaryStage.show();
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void launchMainWindow()
    {
        try 
        {
            StackPane page = (StackPane) FXMLLoader.load(GUI.class.getResource("MainWindow.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setTitle("Murmur Client");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(200);
            primaryStage.show();
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}