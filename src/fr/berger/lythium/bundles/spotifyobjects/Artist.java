package fr.berger.lythium.bundles.spotifyobjects;

import fr.berger.beyondcode.util.EnhancedObservable;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class Artist extends EnhancedObservable {
	
	/* VARIABLES */
	
	private URL href;
	private String name;
	private HashMap<String, URL> external_urls;
	private String id;
	private URI uri;
	private String type;
	
	/* CONSTRUCTOR */
	
	public Artist() {}
	public Artist(@NotNull Artist copy) {
		setHref(copy.getHref());
		setName(copy.getName());
		setExternal_urls(copy.getExternal_urls());
		setId(copy.getId());
		setUri(copy.getUri());
		setType(copy.getType());
	}
	
	/* GETTERS & SETTERS */
	
	public URL getHref() {
		return href;
	}
	
	public void setHref(URL href) {
		this.href = href;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public HashMap<String, URL> getExternal_urls() {
		return external_urls;
	}
	
	public void setExternal_urls(HashMap<String, URL> external_urls) {
		this.external_urls = external_urls;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public URI getUri() {
		return uri;
	}
	
	public void setUri(URI uri) {
		this.uri = uri;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/* NESTED CLASS */
	
	public class Builder {
		
		private Artist instance;
		
		public Builder(@NotNull Artist artist) {
			this.instance = artist;
		}
		public Builder() {
			this(new Artist());
		}
		
		public void setHref(URL href) {
			instance.setHref(href);
		}
		
		public void setName(String name) {
			instance.setName(name);
		}
		
		public void setExternalURLs(HashMap<String, URL> externalURLs) {
			instance.setExternal_urls(externalURLs);
		}
		
		public void setId(String id) {
			instance.setId(id);
		}
		
		public void setUri(URI uri) {
			instance.setUri(uri);
		}
		
		public void setType(String type) {
			instance.setType(type);
		}
		
		public Artist build() {
			return new Artist(instance);
		}
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Artist)) return false;
		Artist artist = (Artist) o;
		return Objects.equals(getHref(), artist.getHref()) &&
				Objects.equals(getName(), artist.getName()) &&
				Objects.equals(getExternal_urls(), artist.getExternal_urls()) &&
				Objects.equals(getId(), artist.getId()) &&
				Objects.equals(getUri(), artist.getUri()) &&
				Objects.equals(getType(), artist.getType());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getHref(), getName(), getExternal_urls(), getId(), getUri(), getType());
	}
	
	@Override
	public String toString() {
		return "Artist{" +
				"href=" + href +
				", name='" + name + '\'' +
				", external_urls=" + external_urls +
				", id='" + id + '\'' +
				", uri=" + uri +
				", type='" + type + '\'' +
				'}';
	}
}
