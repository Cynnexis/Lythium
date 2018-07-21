package fr.berger.lythium;

import org.junit.jupiter.api.Test;

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
}