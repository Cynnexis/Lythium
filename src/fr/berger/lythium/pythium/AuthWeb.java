package fr.berger.lythium.pythium;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Date;

public class AuthWeb {
	
	private Stage stage;
	
	private VBox vb_main;
	
	private WebView wv;
	private WebEngine we;
	
	public AuthWeb() {
		stage = new Stage();
		
		vb_main = new VBox();
		
		wv = new WebView();
		we = wv.getEngine();
		
		vb_main.getChildren().add(wv);
		
		Scene scene = new Scene(vb_main);
		stage.setScene(scene);
	}
	
	public void authenticate(@NotNull String url) {
		we.load(url);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					                  @Override
					                  public void run() {
						                  stage.showAndWait();
					                  }
				                  }
					);
			}
		}).start();
		
		
		while (true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(we.getLocation());
		}
		
		//return null;
	}
}
