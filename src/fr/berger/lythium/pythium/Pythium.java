package fr.berger.lythium.pythium;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.berger.jexec.JExec;
import fr.berger.lythium.spotifyobjects.Artist;
import fr.berger.lythium.spotifyobjects.MixIn;
import fr.berger.lythium.spotifyobjects.SpotifyBundle;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Pythium {
	
	public static SpotifyBundle call(@NotNull String username) {
		final String command = "python ./Pythium/src/pythium/pythium.py " + username + "cybervalentin -i res/misc/spotify-client-id.txt -s res/misc/spotify-client-secret.txt -r res/misc/spotify-redirect-uri.txt";
		File f_json = new File("current_user_playing_track.tmp.json");
		
		Thread th_cmd = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String output = JExec.cmd(command, true);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		th_cmd.start();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Check if the final answer is here (the token has not expired, and authentication is not required)
		if (!f_json.exists()) {
			// Read the file 'auth_url.txt' written by the python script:
			String auth_url = null;
			File f_auth_url = new File("auth_url.txt");
			
			System.out.println("Waiting for auth_url");
			while (auth_url == null) {
				if (f_json.exists())
					break;
				
				if (f_auth_url.exists()) {
					try {
						auth_url = new String(Files.readAllBytes(f_auth_url.toPath()), StandardCharsets.UTF_8);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							Files.deleteIfExists(f_auth_url.toPath());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			if (!f_json.exists()) {
				System.out.println(auth_url);
				
				System.out.println("Waiting for response");
				URL response;
				AuthRequest a = new AuthRequest();
				try {
					response = a.authenticate(new URL(auth_url));
				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
				System.out.println(response.toString());
				
				System.out.println("Writing response...");
				// Write the answer in the file 'auth_response.txt' for the python script:
				File f_auth_response = new File("auth_response.txt");
				try {
					Files.write(f_auth_response.toPath(), response.toString().getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// Wait for the final answer
				while (!f_json.exists()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException ignored) {
					}
				}
			}
		}
		
		// Get the JSON file
		SpotifyBundle result = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.addMixIn(Artist.class, MixIn.class);
			result = objectMapper.readValue(f_json, SpotifyBundle.class);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		return result;
	}
}
