package fr.berger.lythium;

import org.jetbrains.annotations.NotNull;

import java.awt.image.ImagingOpException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class R {
	
	@NotNull
	public static String getSpotifyClientId() {
		return readResourceTextFile("/misc/spotify-client-id.txt").replaceAll("\n", "");
	}
	
	@NotNull
	public static String getSpotifyClientSecret() {
		return readResourceTextFile("/misc/spotify-client-secret.txt").replaceAll("\n", "");
	}
	
	@NotNull
	private static String readResourceTextFile(@NotNull String path) {
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			isr = new InputStreamReader(R.class.getResourceAsStream(path));
			br = new BufferedReader(isr);
			
			StringBuilder content = new StringBuilder();
			String line;
			
			while ((line = br.readLine()) != null) {
				content.append(line)
						.append('\n');
			}
			
			return content.toString();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		throw new RuntimeException("Cannot read the resources.");
	}
}
