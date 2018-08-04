package fr.berger.lythium.pythium;

import fr.berger.enhancedlist.Couple;
import fr.berger.lythium.bundles.pythiumbundle.PythiumBundle;
import fr.berger.lythium.bundles.spotifyobjects.SpotifyBundle;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PythiumTest {
	
	@Test
	void call() {
		//Scanner sc = new Scanner(System.in);
		
		System.out.println("PythiumTest.call> Testing Pythium.call()...");
		System.out.println("PythiumTest.call> Deleting previous credentials...");
		Pythium.resetCredentials();
		
		/*boolean loop = true;
		while (loop) {
			System.out.println("PythiumTest.call> User, please play a music on your Spotify account. [(d)one|(E)xit]");
			String input = sc.next();
			
			if (input.toLowerCase().equals("e")) {
				System.out.println("Exiting...");
				return;
			}
			
			loop = !input.toLowerCase().equals("d");
			
			if (loop)
				System.out.println("PythiumTest.call> Invalid answer. Please enter 'd' if you played the music or 'e' if you want to exit.");
		}*/
		ImageIcon icon;
		try {
			icon = new ImageIcon(ImageIO.read(PythiumTest.class.getResourceAsStream("/images/lythium32.png")));
		} catch (IOException e) {
			icon = new ImageIcon();
		}
		int answer = JOptionPane.showConfirmDialog(
				null,
				"User, please play a music on your Spotify account. Press \"OK\" when it is done, or \"Cancel\" to exit the test.",
				"PythiumTest.call",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE,
				icon
		);
		
		if (answer == JOptionPane.CANCEL_OPTION)
			return;
		
		Couple<PythiumBundle, SpotifyBundle> result = Pythium.call();
		assertNotNull(result);
		assertNotNull(result.getX());
		assertNotNull(result.getX().getMusic());
		assertNotNull(result.getX().getArtists());
		assertNotNull(result.getX().getLyrics());
		System.out.println("PythiumTest.call> You are listening to \"" + result.getX().getMusic() + "\" by " + result.getX().getArtists());
		System.out.println("PythiumTest.call> Lyrics: \"" + result.getX().getLyrics().replaceAll("\n", "\\n") + "\"");
		assertNotNull(result.getY());
	}
}