package fr.berger.lythium;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RTest {
	
	@Test
	void getLythiumIcon() {
		assertNotNull(R.getLythiumIcon(16, R.ImageType.COLOR));
		assertNotNull(R.getLythiumIcon(32, R.ImageType.COLOR));
		assertNotNull(R.getLythiumIcon(64, R.ImageType.COLOR));
		assertNotNull(R.getLythiumIcon(128, R.ImageType.COLOR));
		assertNotNull(R.getLythiumIcon(256, R.ImageType.COLOR));
		assertNotNull(R.getLythiumIcon(512, R.ImageType.COLOR));
		assertNotNull(R.getLythiumIcon(1024, R.ImageType.COLOR));
		assertNull(R.getLythiumIcon(2048, R.ImageType.COLOR));
		assertNull(R.getLythiumIcon(442, R.ImageType.COLOR));
		assertNull(R.getLythiumIcon(0, R.ImageType.COLOR));
		assertNull(R.getLythiumIcon(-1, R.ImageType.COLOR));
		assertNull(R.getLythiumIcon(6412, R.ImageType.COLOR));
		
		assertNotNull(R.getLythiumIcon(16, R.ImageType.BLACK));
		assertNotNull(R.getLythiumIcon(32, R.ImageType.BLACK));
		assertNull(R.getLythiumIcon(64, R.ImageType.BLACK));
		assertNull(R.getLythiumIcon(0, R.ImageType.BLACK));
		assertNull(R.getLythiumIcon(-1, R.ImageType.BLACK));
		
		assertNotNull(R.getLythiumIcon(16, R.ImageType.WHITE));
		assertNotNull(R.getLythiumIcon(32, R.ImageType.WHITE));
		assertNull(R.getLythiumIcon(64, R.ImageType.WHITE));
		assertNull(R.getLythiumIcon(0, R.ImageType.WHITE));
		assertNull(R.getLythiumIcon(-1, R.ImageType.WHITE));
	}
	
	@Test
	void getRefreshIcon() {
		assertNotNull(R.getRefreshIcon(16));
		assertNotNull(R.getRefreshIcon(32));
		assertNull(R.getRefreshIcon(64));
		assertNull(R.getRefreshIcon(0));
		assertNull(R.getRefreshIcon(-1));
	}
	
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
	
	@Test
	void getPrimaryColor() throws Exception {
		assertEquals(((float)  46)/((float) 255), R.getPrimaryColor().getRed());
		assertEquals(((float) 203)/((float) 255), R.getPrimaryColor().getGreen());
		assertEquals(((float) 163)/((float) 255), R.getPrimaryColor().getBlue());
		assertEquals("#2ecba3ff", R.getPrimaryColor().toString().replaceFirst("0x", "#"));
	}
	
	@Test
	void getPrimaryColorAsHexa() throws Exception {
		assertEquals("#2ecba3ff", R.getPrimaryColorAsHexa());
	}
}