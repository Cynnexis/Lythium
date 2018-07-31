package fr.berger.lythium.bundles.spotifyobjects;

import fr.berger.beyondcode.util.EnhancedObservable;
import fr.berger.enhancedlist.lexicon.Lexicon;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class Track extends EnhancedObservable {
	
	/* VARIABLES */
	
	private boolean is_local;
	private Lexicon<String> available_markets;
	private URL preview_url;
	private URL href;
	private boolean explicit;
	private int popularity;
	private HashMap<String, URL> external_urls;
	private String id;
	private Lexicon<Artist> artists;
	private HashMap<String, String> external_ids;
	private long duration_ms;
	private String name;
	private URI uri;
	private int disc_number;
	private Album album;
	private int track_number;
	private String type;
	
	/* CONSTRUCTOR */
	
	public Track() {}
	public Track(@NotNull Track copy) {
		setIs_local(copy.isIs_local());
		setAvailable_markets(copy.getAvailable_markets());
		setPreview_url(copy.getPreview_url());
		setHref(copy.getHref());
		setExplicit(copy.isExplicit());
		setPopularity(copy.getPopularity());
		setExternal_urls(copy.getExternal_urls());
		setId(copy.getId());
		setArtists(copy.getArtists());
		setExternal_ids(copy.getExternal_ids());
		setDuration_ms(copy.getDuration_ms());
		setName(copy.getName());
		setUri(copy.getUri());
		setDisc_number(copy.getDisc_number());
		setAlbum(copy.getAlbum());
		setTrack_number(copy.getTrack_number());
		setType(copy.getType());
	}
	
	/* GETTERS & SETTERS */
	
	public boolean isIs_local() {
		return is_local;
	}
	
	public void setIs_local(boolean is_local) {
		this.is_local = is_local;
	}
	
	public Lexicon<String> getAvailable_markets() {
		return available_markets;
	}
	
	public void setAvailable_markets(Lexicon<String> available_markets) {
		this.available_markets = available_markets;
	}
	
	public URL getPreview_url() {
		return preview_url;
	}
	
	public void setPreview_url(URL preview_url) {
		this.preview_url = preview_url;
	}
	
	public URL getHref() {
		return href;
	}
	
	public void setHref(URL href) {
		this.href = href;
	}
	
	public boolean isExplicit() {
		return explicit;
	}
	
	public void setExplicit(boolean explicit) {
		this.explicit = explicit;
	}
	
	public int getPopularity() {
		return popularity;
	}
	
	public void setPopularity(int popularity) {
		this.popularity = popularity;
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
	
	public HashMap<String, String> getExternal_ids() {
		return external_ids;
	}
	
	public void setExternal_ids(HashMap<String, String> external_ids) {
		this.external_ids = external_ids;
	}
	
	public long getDuration_ms() {
		return duration_ms;
	}
	
	public void setDuration_ms(long duration_ms) {
		this.duration_ms = duration_ms;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public URI getUri() {
		return uri;
	}
	
	public void setUri(URI uri) {
		this.uri = uri;
	}
	
	public int getDisc_number() {
		return disc_number;
	}
	
	public void setDisc_number(int disc_number) {
		this.disc_number = disc_number;
	}
	
	public Album getAlbum() {
		return album;
	}
	
	public void setAlbum(Album album) {
		this.album = album;
	}
	
	public int getTrack_number() {
		return track_number;
	}
	
	public void setTrack_number(int track_number) {
		this.track_number = track_number;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/* NESTED CLASS */
	
	public class Builder {
		
		private Track instance;
		
		public Builder(@NotNull Track track) {
			this.instance = track;
		}
		public Builder() {
			this(new Track());
		}
		
		public void setLocal(boolean local) {
			instance.setIs_local(local);
		}
		
		public void setAvailableMarkets(Lexicon<String> availableMarkets) {
			instance.setAvailable_markets(availableMarkets);
		}
		
		public void setPreviewURL(URL previewURL) {
			instance.setPreview_url(previewURL);
		}
		
		public void setHref(URL href) {
			instance.setHref(href);
		}
		
		public void setExplicit(boolean explicit) {
			instance.setExplicit(explicit);
		}
		
		public void setPopularity(int popularity) {
			instance.setPopularity(popularity);
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
		
		public void setExternalIDs(HashMap<String, String> externalIDs) {
			instance.setExternal_ids(externalIDs);
		}
		
		public void setDurationMs(long durationMs) {
			instance.setDuration_ms(durationMs);
		}
		
		public void setName(String name) {
			instance.setName(name);
		}
		
		public void setUri(URI uri) {
			instance.setUri(uri);
		}
		
		public void setDiscNumber(int discNumber) {
			instance.setDisc_number(discNumber);
		}
		
		public void setAlbum(Album album) {
			instance.setAlbum(album);
		}
		
		public void setTrackNumber(int trackNumber) {
			instance.setTrack_number(trackNumber);
		}
		
		public void setType(String type) {
			instance.setType(type);
		}
		
		public Track build() {
			return new Track(instance);
		}
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Track)) return false;
		Track track = (Track) o;
		return isIs_local() == track.isIs_local() &&
				isExplicit() == track.isExplicit() &&
				getPopularity() == track.getPopularity() &&
				getDuration_ms() == track.getDuration_ms() &&
				getDisc_number() == track.getDisc_number() &&
				getTrack_number() == track.getTrack_number() &&
				Objects.equals(getAvailable_markets(), track.getAvailable_markets()) &&
				Objects.equals(getPreview_url(), track.getPreview_url()) &&
				Objects.equals(getHref(), track.getHref()) &&
				Objects.equals(getExternal_urls(), track.getExternal_urls()) &&
				Objects.equals(getId(), track.getId()) &&
				Objects.equals(getArtists(), track.getArtists()) &&
				Objects.equals(getExternal_ids(), track.getExternal_ids()) &&
				Objects.equals(getName(), track.getName()) &&
				Objects.equals(getUri(), track.getUri()) &&
				Objects.equals(getAlbum(), track.getAlbum()) &&
				Objects.equals(getType(), track.getType());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(isIs_local(), getAvailable_markets(), getPreview_url(), getHref(), isExplicit(), getPopularity(), getExternal_urls(), getId(), getArtists(), getExternal_ids(), getDuration_ms(), getName(), getUri(), getDisc_number(), getAlbum(), getTrack_number(), getType());
	}
	
	@Override
	public String toString() {
		return "Track{" +
				"is_local=" + is_local +
				", available_markets=" + available_markets +
				", preview_url=" + preview_url +
				", href=" + href +
				", explicit=" + explicit +
				", popularity=" + popularity +
				", external_urls=" + external_urls +
				", id='" + id + '\'' +
				", artists=" + artists +
				", external_ids=" + external_ids +
				", duration_ms=" + duration_ms +
				", name='" + name + '\'' +
				", uri=" + uri +
				", disc_number=" + disc_number +
				", album=" + album +
				", track_number=" + track_number +
				", type='" + type + '\'' +
				'}';
	}
}
