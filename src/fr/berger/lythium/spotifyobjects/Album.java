package fr.berger.lythium.spotifyobjects;

import fr.berger.beyondcode.util.EnhancedObservable;
import fr.berger.enhancedlist.lexicon.Lexicon;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class Album extends EnhancedObservable {
	
	/* VARIABLES */
	
	private String release_date;
	private Lexicon<String> available_markets;
	private URL href;
	private String release_date_precision;
	private HashMap<String, URL> external_urls;
	private String id;
	private Lexicon<Artist> artists;
	private String name;
	private Lexicon<Image> images;
	private URI uri;
	private String album_type;
	private String type;
	
	/* CONSTRUCTOR */
	
	public Album() {}
	public Album(@NotNull Album copy) {
		setRelease_date(copy.getRelease_date());
		setAvailable_markets(copy.getAvailable_markets());
		setHref(copy.getHref());
		setRelease_date_precision(copy.getRelease_date_precision());
		setExternal_urls(copy.getExternal_urls());
		setId(copy.getId());
		setArtists(copy.getArtists());
		setName(copy.getName());
		setImages(getImages());
		setUri(copy.getUri());
		setAlbum_type(copy.getAlbum_type());
		setType(copy.getType());
	}
	
	/* GETTERS & SETTERS */
	
	public String getRelease_date() {
		return release_date;
	}
	
	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}
	
	public Lexicon<String> getAvailable_markets() {
		return available_markets;
	}
	
	public void setAvailable_markets(Lexicon<String> available_markets) {
		this.available_markets = available_markets;
	}
	
	public URL getHref() {
		return href;
	}
	
	public void setHref(URL href) {
		this.href = href;
	}
	
	public String getRelease_date_precision() {
		return release_date_precision;
	}
	
	public void setRelease_date_precision(String release_date_precision) {
		this.release_date_precision = release_date_precision;
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
	
	public Lexicon<Artist> getArtists() {
		return artists;
	}
	
	public void setArtists(Lexicon<Artist> artists) {
		this.artists = artists;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Lexicon<Image> getImages() {
		return images;
	}
	
	public void setImages(Lexicon<Image> images) {
		this.images = images;
	}
	
	public URI getUri() {
		return uri;
	}
	
	public void setUri(URI uri) {
		this.uri = uri;
	}
	
	public String getAlbum_type() {
		return album_type;
	}
	
	public void setAlbum_type(String album_type) {
		this.album_type = album_type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/* NESTED CLASS */
	
	public class Builder {
		
		private Album instance;
		
		public Builder(@NotNull Album album) {
			this.instance = album;
		}
		public Builder() {
			this(new Album());
		}
		
		public void setReleaseDate(String releaseDate) {
			instance.setRelease_date(releaseDate);
		}
		
		public void setAvailableMarkets(Lexicon<String> availableMarkets) {
			instance.setAvailable_markets(availableMarkets);
		}
		
		public void setHref(URL href) {
			instance.setHref(href);
		}
		
		public void setReleaseDatePrecision(String releaseDatePrecision) {
			instance.setRelease_date_precision(releaseDatePrecision);
		}
		
		public void setExternalURLs(HashMap<String, URL> externalURLs) {
			instance.setExternal_urls(externalURLs);
		}
		
		public void setId(String id) {
			instance.setId(id);
		}
		
		public void setArtists(Lexicon<Artist> artists) {
			instance.setArtists(artists);
		}
		
		public void setName(String name) {
			instance.setName(name);
		}
		
		public void setImages(Lexicon<Image> images) {
			instance.setImages(images);
		}
		
		public void setUri(URI uri) {
			instance.setUri(uri);
		}
		
		public void setAlbumType(String albumType) {
			instance.setAlbum_type(albumType);
		}
		
		public void setType(String type) {
			instance.setType(type);
		}
		
		public Album build() {
			return new Album(instance);
		}
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Album)) return false;
		Album album = (Album) o;
		return Objects.equals(getRelease_date(), album.getRelease_date()) &&
				Objects.equals(getAvailable_markets(), album.getAvailable_markets()) &&
				Objects.equals(getHref(), album.getHref()) &&
				Objects.equals(getRelease_date_precision(), album.getRelease_date_precision()) &&
				Objects.equals(getExternal_urls(), album.getExternal_urls()) &&
				Objects.equals(getId(), album.getId()) &&
				Objects.equals(getArtists(), album.getArtists()) &&
				Objects.equals(getName(), album.getName()) &&
				Objects.equals(getImages(), album.getImages()) &&
				Objects.equals(getUri(), album.getUri()) &&
				Objects.equals(getAlbum_type(), album.getAlbum_type()) &&
				Objects.equals(getType(), album.getType());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getRelease_date(), getAvailable_markets(), getHref(), getRelease_date_precision(), getExternal_urls(), getId(), getArtists(), getName(), getImages(), getUri(), getAlbum_type(), getType());
	}
	
	@Override
	public String toString() {
		return "Album{" +
				"release_date=" + release_date +
				", available_markets=" + available_markets +
				", href=" + href +
				", release_date_precision='" + release_date_precision + '\'' +
				", external_urls=" + external_urls +
				", id='" + id + '\'' +
				", artists=" + artists +
				", name='" + name + '\'' +
				", images=" + images +
				", uri=" + uri +
				", album_type='" + album_type + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}
