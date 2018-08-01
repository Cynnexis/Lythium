package fr.berger.lythium;

import org.jetbrains.annotations.NotNull;

import java.awt.image.ImagingOpException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class R {
	
	@NotNull
	public static String getApplicationFolderPath() throws IOException {
		String appdataFolder = "";
		String applicationFolderPath = "";
		
		String os = System.getProperty("os.name").toLowerCase();
		
		if (os.contains("win")) {
			appdataFolder = System.getProperty("AppData");
			
			if (appdataFolder == null || Objects.equals(appdataFolder, "")) {
				appdataFolder = System.getProperty("%appdata%");
				
				if (appdataFolder == null || Objects.equals(appdataFolder, "")) {
					String base = System.getProperty("user.home");
					
					if (appdataFolder == null)
						base = "C:\\";
					if (!base.endsWith("\\"))
						base += "\\";
					
					appdataFolder = base + "AppData\\Roaming";
					
					if (!new File(appdataFolder).exists()) {
						appdataFolder = base + "AppData";
						
						if (!new File(appdataFolder).exists()) {
							appdataFolder = base;
							
							if (!new File(appdataFolder).exists())
								throw new IOException("Cannot find the AppData directory.");
						}
					}
				}
			}
			
			if (!appdataFolder.endsWith("\\"))
				appdataFolder += "\\";
			
			applicationFolderPath = appdataFolder + "Lythium";
		}
		else {
			appdataFolder = System.getProperty("user.home");
			
			if (appdataFolder == null || Objects.equals(appdataFolder, ""))
				throw new IOException("Cannot find HOME directory. Have you launched this application with the right permissions?");
			
			if (!appdataFolder.endsWith("/"))
				appdataFolder += "/";
			
			applicationFolderPath = appdataFolder + ".lythium";
		}
		
		// Check if it exists
		mkdir(applicationFolderPath);
		
		return applicationFolderPath;
	}
	
	@NotNull
	public static File getApplicationFolder() throws IOException {
		return new File(getApplicationFolderPath());
	}
	
	@NotNull
	public static String getApplicationPreferencesPath() throws IOException {
		return Paths.get(getApplicationFolderPath(), "app-preferences.xml").toAbsolutePath().toString();
	}
	
	@NotNull
	public static File getApplicationPreferencesFile() throws IOException {
		return new File(getApplicationPreferencesPath());
	}
	
	@NotNull
	public static String getMusicPreferencesPath() throws IOException {
		return Paths.get(getApplicationFolderPath(), "music-preferences.xml").toAbsolutePath().toString();
	}
	
	@NotNull
	public static File getMusicPreferencesFile() throws IOException {
		return new File(getMusicPreferencesPath());
	}
	
	@NotNull
	public static String getCachePath() throws IOException {
		File appFolder = getApplicationFolder();
		
		if (appFolder.exists()) {
			String cachePath = Paths.get(appFolder.getAbsolutePath(), "cache").toAbsolutePath().toString();
			
			// Check if it exists
			mkdir(cachePath);
			
			return cachePath;
		}
		else
			throw new IOException("Something went wrong while trying to get the cache folder of the application in \"" + appFolder.getAbsolutePath() + "\"");
	}
	
	@NotNull
	public static File getCacheFolder() throws IOException {
		return new File(getCachePath());
	}
	
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
	
	
	private static void mkdir(@NotNull File f) throws IOException {
		if (!f.exists()) {
			if (!f.mkdir())
				throw new IOException("Cannot create the application data directory \"" + f.getAbsolutePath() + "\"");
		}
	}
	private static void mkdir(@NotNull String path) throws IOException {
		mkdir(new File(path));
	}
}
