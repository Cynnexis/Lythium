package fr.berger.lythium.pythium;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.lexicon.Lexicon;
import fr.berger.lythium.bundles.pythiumbundle.PythiumBundle;
import fr.berger.lythium.bundles.spotifyobjects.Artist;
import fr.berger.lythium.bundles.spotifyobjects.MixIn;
import fr.berger.lythium.bundles.spotifyobjects.SpotifyBundle;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class Pythium {
	
	private static String SPOTIFY_BUNDLE_FILENAME = "spotify-bundle.json";
	private static String PYTHIUM_BUNDLE_FILENAME = "lyrics.json";
	private static String SPOTIFY_CREDENTIALS_FILENAME = ".cache-rauth-spotify.json";
	private static String GENIUS_CREDENTIALS_FILENAME = ".cache-rauth-genius.json";
	
	
	// The function Pythium.call() can only be executed as one and only one instance at a time maximum.
	private static boolean FUNCTION_CALL_EXECUTING = false;
	
	
	@Nullable
	public static Couple<PythiumBundle, SpotifyBundle> call() {
		if (!FUNCTION_CALL_EXECUTING) {
			FUNCTION_CALL_EXECUTING = true;
			File f_spotifyBundle = new File(SPOTIFY_BUNDLE_FILENAME);
			File f_lyrics = new File(PYTHIUM_BUNDLE_FILENAME);
			
			String content = "";
			try {
				Process process = new ProcessBuilder("python3", "pythium-core.py", "-r", "../../../res/misc/",
						"-s", "../../../spotify-bundle.json", "-o", "../../../lyrics.json", "-i")
						.inheritIO()
						.directory(new File("Pythium/src/pythium"))
						.start();
				process.waitFor();
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				StringBuilder b_content = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null && !Objects.equals(line, ""))
					b_content.append(line);
				content = b_content.toString();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				FUNCTION_CALL_EXECUTING = false;
				return null;
			}
			
			if (content.startsWith("ERROR") || content.startsWith("Traceback")) {
				FUNCTION_CALL_EXECUTING = false;
				return null;
			}
			
			// Get the JSON files
			
			PythiumBundle pythiumBundle = null;
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				pythiumBundle = objectMapper.readValue(f_lyrics, PythiumBundle.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			SpotifyBundle spotifyBundle = null;
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.addMixIn(Artist.class, MixIn.class);
				spotifyBundle = objectMapper.readValue(f_spotifyBundle, SpotifyBundle.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			FUNCTION_CALL_EXECUTING = false;
			return new Couple<>(pythiumBundle, spotifyBundle);
		}
		else
			return null;
	}
	
	public static boolean resetCredentials() {
		ArrayList<Boolean> results = new ArrayList<>();
		
		File f_spotify = new File(SPOTIFY_CREDENTIALS_FILENAME);
		File f_genius = new File(GENIUS_CREDENTIALS_FILENAME);
		
		if (f_spotify.exists())
			results.add(f_spotify.delete());
		
		if (f_genius.exists())
			results.add(f_genius.delete());
		
		return !results.contains(false);
	}
}
