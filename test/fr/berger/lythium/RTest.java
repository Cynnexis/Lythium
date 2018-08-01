package fr.berger.lythium;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RTest {
	
	@Test
	void getSpotifyClientId() {
		String content = R.getSpotifyClientId();
		System.out.println("RTest.getSpotifyClientId> " + content);
		assertNotEquals("", content);
		assertFalse(content.endsWith("\n"));
	}
	
	@Test
	void getSpotifyClientSecret() {
		String content = R.getSpotifyClientSecret();
		System.out.println("RTest.getSpotifyClientSecret> " + content);
		assertNotEquals("", content);
		assertFalse(content.endsWith("\n"));
	}
	
	@Test
	void getApplicationFolderPath() throws Exception {
		String applicationFolderPath = R.getApplicationFolderPath();
		System.out.println("RTest.getApplicationFolderPath> Application folder path = " + applicationFolderPath);
		assertNotNull(applicationFolderPath);
	}
	
	@Test
	void getApplicationFolder() throws Exception {
		File f = R.getApplicationFolder();
		System.out.println("RTest.getApplicationFolder> Application folder path = " + f.getAbsolutePath());
		assertNotNull(f);
		assertTrue(f.exists());
	}
	
	@Test
	void getApplicationPreferencesPath() throws Exception {
		String path = R.getApplicationPreferencesPath();
		System.out.println("RTest.getCachePath> Application preferences path = " + path);
		assertNotNull(path);
		assertTrue(path.startsWith(R.getApplicationFolderPath()));
	}
	
	@Test
	void getApplicationPreferencesFile() throws Exception {
		File f = R.getApplicationPreferencesFile();
		System.out.println("RTest.getApplicationPreferencesFile> Application preferences path = " + f.getAbsolutePath());
		assertNotNull(f);
	}
	
	@Test
	void getMusicPreferencesPath() throws Exception {
		String path = R.getApplicationPreferencesPath();
		System.out.println("RTest.getMusicPreferencesPath> Music preferences path = " + path);
		assertNotNull(path);
		assertTrue(path.startsWith(R.getApplicationFolderPath()));
	}
	
	@Test
	void getMusicPreferencesFile() throws Exception {
		File f = R.getApplicationPreferencesFile();
		System.out.println("RTest.getMusicPreferencesFile> Music preferences path = " + f.getAbsolutePath());
		assertNotNull(f);
	}
	
	@Test
	void getCachePath() throws IOException {
		String cachePath = R.getCachePath();
		System.out.println("RTest.getCachePath> Application cache folder path = " + cachePath);
		assertNotNull(cachePath);
		assertTrue(cachePath.startsWith(R.getApplicationFolderPath()));
	}
	
	@Test
	void getCacheFolder() throws Exception {
		File f = R.getCacheFolder();
		System.out.println("RTest.getCacheFolder> Application cache folder path = " + f.getAbsolutePath());
		assertNotNull(f);
		assertTrue(f.exists());
	}
}