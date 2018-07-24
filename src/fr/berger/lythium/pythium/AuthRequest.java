package fr.berger.lythium.pythium;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class AuthRequest {
	
	private Stage stage;
	
	private VBox vb_main;
	
	private Text tx_description;
	private Hyperlink hl_url;
	private Button bt_copy;
	private Text tx_error;
	private TextField tf_redirect;
	private Button bt_ok;
	
	private URL response = null;
	
	public AuthRequest() {
		stage = new Stage();
		
		vb_main = new VBox();
		
		tx_description = new Text("Please copy-paste the URL below into your browser to login into your Spotify account.\nThen, Spotify will redirect you to another website ; please copy-paste this link below.");
		hl_url = new Hyperlink();
		bt_copy = new Button("Copy URL to Clipboard");
		tx_error = new Text();
		tx_error.setFill(Color.RED);
		
		tf_redirect = new TextField();
		tf_redirect.setPromptText("The redirection url (ex: http://localhost/...)");
		
		bt_ok = new Button("OK");
		
		bt_copy.setOnAction(event -> {
			ClipboardContent content = new ClipboardContent();
			content.putString(hl_url.getText());
			Clipboard.getSystemClipboard().setContent(content);
		});
		hl_url.setWrapText(true);
		
		vb_main.getChildren().addAll(tx_description, hl_url, bt_copy, tx_error, tf_redirect, bt_ok);
		Scene scene = new Scene(vb_main);
		stage.setScene(scene);
	}
	
	@NotNull
	public URL authenticate(@NotNull final URL url) {
		hl_url.setText(url.toString());
		hl_url.setOnAction(event -> {
			try {
				Desktop.getDesktop().browse(url.toURI());
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
				tx_error.setText("Cannot open the URL");
			}
		});
		
		bt_ok.setOnAction(event -> {
			try {
				response = new URL(tf_redirect.getText());
			} catch (MalformedURLException e) {
				e.printStackTrace();
				tx_error.setText("The URL is not valid.");
			}
			
			stage.close();
		});
		
		stage.showAndWait();
		return response;
	}
}
