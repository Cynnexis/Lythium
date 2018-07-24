package fr.berger.lythium;

import fr.berger.lythium.pythium.Pythium;
import fr.berger.lythium.spotifyobjects.SpotifyBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Lythium extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		SpotifyBundle bundle = Pythium.call("cybervalentin");
		
		System.out.println("You're listening to " + bundle.getItem().getName() + " by " + bundle.getItem().getArtists().toString());
		
		Platform.exit();
	}
}
