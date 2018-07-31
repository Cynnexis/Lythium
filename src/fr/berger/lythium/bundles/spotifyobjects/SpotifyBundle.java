package fr.berger.lythium.bundles.spotifyobjects;

import fr.berger.beyondcode.util.EnhancedObservable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SpotifyBundle extends EnhancedObservable {
	
	/* VARIABLES */
	
	private long progress_ms;
	private long timestamp;
	private Playlist context;
	private boolean is_playing;
	private Track item;
	
	/* CONSTRUCTOR */
	
	public SpotifyBundle() {}
	public SpotifyBundle(@NotNull SpotifyBundle copy) {
		setProgress_ms(copy.getProgress_ms());
		setTimestamp(copy.getTimestamp());
		setContext(copy.getContext());
		setIs_playing(copy.isIs_playing());
		setItem(copy.getItem());
	}
	
	/* GETTERS & SETTERS */
	
	public long getProgress_ms() {
		return progress_ms;
	}
	
	public void setProgress_ms(long progress_ms) {
		this.progress_ms = progress_ms;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public Playlist getContext() {
		return context;
	}
	
	public void setContext(Playlist context) {
		this.context = context;
	}
	
	public boolean isIs_playing() {
		return is_playing;
	}
	
	public void setIs_playing(boolean is_playing) {
		this.is_playing = is_playing;
	}
	
	public Track getItem() {
		return item;
	}
	
	public void setItem(Track item) {
		this.item = item;
	}
	
	/* NESTED CLASS */
	
	public class Builder {
		
		private SpotifyBundle instance;
		
		public Builder(@NotNull SpotifyBundle spotifyBundle) {
			this.instance = spotifyBundle;
		}
		public Builder() {
			this(new SpotifyBundle());
		}
		
		public void setProgessMs(long progessMs) {
			instance.setProgress_ms(progessMs);
		}
		
		public void setTimestamp(long timestamp) {
			instance.setTimestamp(timestamp);
		}
		
		public void setContext(Playlist context) {
			instance.setContext(context);
		}
		
		public void setPlaying(boolean playing) {
			instance.setIs_playing(playing);
		}
		
		public void setItem(Track item) {
			instance.setItem(item);
		}
		
		public SpotifyBundle build() {
			return new SpotifyBundle(instance);
		}
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SpotifyBundle)) return false;
		SpotifyBundle that = (SpotifyBundle) o;
		return getProgress_ms() == that.getProgress_ms() &&
				getTimestamp() == that.getTimestamp() &&
				isIs_playing() == that.isIs_playing() &&
				Objects.equals(getContext(), that.getContext()) &&
				Objects.equals(getItem(), that.getItem());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getProgress_ms(), getTimestamp(), getContext(), isIs_playing(), getItem());
	}
	
	@Override
	public String toString() {
		return "SpotifyBundle{" +
				"progress_ms=" + progress_ms +
				", timestamp=" + timestamp +
				", context=" + context +
				", is_playing=" + is_playing +
				", item=" + item +
				'}';
	}
}
