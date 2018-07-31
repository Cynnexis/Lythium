package fr.berger.lythium.bundles.pythiumbundle;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PythiumBundle {
	
	private String music;
	private String artists;
	private String lyrics;
	
	public PythiumBundle(@NotNull String music, @NotNull String artists, @NotNull String lyrics) {
		setMusic(music);
		setArtists(artists);
		setLyrics(lyrics);
	}
	public PythiumBundle(@NotNull PythiumBundle copy) {
		setMusic(copy.getMusic());
		setArtists(copy.getArtists());
		setLyrics(copy.getLyrics());
	}
	public PythiumBundle() {
		setMusic("");
		setArtists("");
		setLyrics("");
	}
	
	/* GETTERS & SETTERS */
	
	public String getMusic() {
		return music;
	}
	
	public void setMusic(@NotNull String music) {
		this.music = music;
	}
	
	public String getArtists() {
		return artists;
	}
	
	public void setArtists(@NotNull String artists) {
		this.artists = artists;
	}
	
	public String getLyrics() {
		return lyrics;
	}
	
	public void setLyrics(@NotNull String lyrics) {
		this.lyrics = lyrics;
	}
	
	/* NESTED CLASS */
	
	public class Builder {
		
		private PythiumBundle instance;
		
		public Builder(@NotNull PythiumBundle instance) {
			this.instance = instance;
		}
		public Builder() {
			this(new PythiumBundle());
		}
		
		public void setMusic(@NotNull String music) {
			instance.setMusic(music);
		}
		
		public void setArtists(@NotNull String artists) {
			instance.setArtists(artists);
		}
		
		public void setLyrics(@NotNull String lyrics) {
			instance.setLyrics(lyrics);
		}
		
		public PythiumBundle build() {
			return new PythiumBundle(instance);
		}
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PythiumBundle)) return false;
		PythiumBundle that = (PythiumBundle) o;
		return Objects.equals(getMusic(), that.getMusic()) &&
				Objects.equals(getArtists(), that.getArtists()) &&
				Objects.equals(getLyrics(), that.getLyrics());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getMusic(), getArtists(), getLyrics());
	}
	
	@Override
	public String toString() {
		return "PythiumBundle{" +
				"music='" + music + '\'' +
				", artists='" + artists + '\'' +
				", lyrics='" + lyrics + '\'' +
				'}';
	}
}
