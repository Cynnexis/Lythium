package fr.berger.lythium.spotifyobjects;

import fr.berger.beyondcode.util.EnhancedObservable;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class Playlist extends EnhancedObservable {
	
	/* VARIABLES */
	
	private URL href;
	private URI uri;
	private HashMap<String, URL> external_urls;
	
	/* CONSTRUCTOR */
	
	public Playlist() {}
	public Playlist(@NotNull Playlist copy) {
		setHref(copy.getHref());
		setUri(copy.getUri());
		setExternal_urls(copy.getExternal_urls());
	}
	
	/* GETTERS & SETTERS */
	
	public URL getHref() {
		return href;
	}
	
	public void setHref(URL href) {
		this.href = href;
	}
	
	public URI getUri() {
		return uri;
	}
	
	public void setUri(URI uri) {
		this.uri = uri;
	}
	
	public HashMap<String, URL> getExternal_urls() {
		return external_urls;
	}
	
	public void setExternal_urls(HashMap<String, URL> external_urls) {
		this.external_urls = external_urls;
	}
	
	/* NESTED CLASS */
	
	public class Builder {
		
		private Playlist instance;
		
		public Builder(@NotNull Playlist playlist) {
			this.instance = playlist;
		}
		public Builder() {
			this(new Playlist());
		}
		
		public void setHref(URL href) {
			instance.setHref(href);
		}
		
		public void setUri(URI uri) {
			instance.setUri(uri);
		}
		
		public void setExternalURLs(HashMap<String, URL> externalURLs) {
			instance.setExternal_urls(externalURLs);
		}
		
		public Playlist build() {
			return new Playlist(instance);
		}
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Playlist)) return false;
		Playlist playlist = (Playlist) o;
		return Objects.equals(getHref(), playlist.getHref()) &&
				Objects.equals(getUri(), playlist.getUri()) &&
				Objects.equals(getExternal_urls(), playlist.getExternal_urls());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getHref(), getUri(), getExternal_urls());
	}
	
	@Override
	public String toString() {
		return "Playlist{" +
				"href=" + href +
				", uri=" + uri +
				", external_urls=" + external_urls +
				'}';
	}
}
