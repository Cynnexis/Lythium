package fr.berger.lythium.ui;

import fr.berger.enhancedlist.Couple;
import fr.berger.lythium.bundles.pythiumbundle.PythiumBundle;
import fr.berger.lythium.bundles.spotifyobjects.Artist;
import fr.berger.lythium.bundles.spotifyobjects.SpotifyBundle;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// TODO: Save the thumbnails in .lythium/cache/

public class Showcase extends Pane {
	
	/* VARIABLES */
	
	// DATA
	
	private @NotNull String musicName;
	private @NotNull String artists;
	private @NotNull Image cover;
	
	// JAVAFX
	
	private ImageView iv_cover;
	private Text tx_musicName;
	private Text tx_artists;
	
	// TODO VBOX + HBOX
	private VBox vb_texts;
	private HBox hb_root;
	
	/* CONSTRUCTORS */
	
	public Showcase(@NotNull String musicName, @NotNull String artists, @NotNull Image cover) {
		initFX();
		update(musicName, artists, cover);
	}
	public Showcase(@NotNull String musicName, @NotNull String artists, @NotNull String coverUrl) {
		initFX();
		update(musicName, artists, coverUrl);
	}
	public Showcase(@NotNull PythiumBundle pythiumBundle, @NotNull Image cover) {
		initFX();
		update(pythiumBundle, cover);
	}
	public Showcase(@NotNull PythiumBundle pythiumBundle, @NotNull String coverUrl) {
		initFX();
		setMusicName(pythiumBundle.getMusic());
		setArtists(pythiumBundle.getArtists());
		setCover(new Image(coverUrl, false));
	}
	public Showcase(@NotNull SpotifyBundle spotifyBundle) {
		initFX();
		update(spotifyBundle);
	}
	public Showcase(@NotNull Couple<PythiumBundle, SpotifyBundle> bundles) {
		this(Objects.requireNonNull(bundles.getY()));
	}
	public Showcase(@NotNull Showcase copy) {
		initFX();
		update(copy);
	}
	public Showcase() {
		initFX();
		initMusicName();
		initArtists();
		initCover();
	}
	
	private void initFX() {
		tx_musicName = new Text();
		tx_artists = new Text();
		iv_cover = new ImageView();
		iv_cover.setFitWidth(150);
		iv_cover.setFitHeight(150);
		
		vb_texts = new VBox(tx_musicName, tx_artists);
		hb_root = new HBox(iv_cover, vb_texts);
		
		this.getChildren().add(hb_root);
	}
	
	/* SHOWCASE METHOD */
	
	public void update(@NotNull String musicName, @NotNull String artists, @NotNull Image cover) {
		setMusicName(musicName);
		setArtists(artists);
		setCover(cover);
	}
	public void update(@NotNull String musicName, @NotNull String artists, @NotNull String coverUrl) {
		setMusicName(musicName);
		setArtists(artists);
		setCover(new Image(coverUrl, false));
	}
	public void update(@NotNull PythiumBundle pythiumBundle, @NotNull Image cover) {
		setMusicName(pythiumBundle.getMusic());
		setArtists(pythiumBundle.getArtists());
		setCover(cover);
	}
	public void update(@NotNull SpotifyBundle spotifyBundle) {
		setMusicName(spotifyBundle.getItem().getName());
		
		String artists = "";
		for (Artist artist : spotifyBundle.getItem().getArtists()) {
			if (!artists.equals(""))
				artists += ", ";
			artists += artist.getName();
		}
		setArtists(artists);
		
		if (spotifyBundle.getItem().getAlbum().getImages() != null && spotifyBundle.getItem().getAlbum().getImages().size() > 0) {
			if (spotifyBundle.getItem().getAlbum().getImages().first() != null && spotifyBundle.getItem().getAlbum().getImages().first().getUrl() != null) {
				String url = spotifyBundle.getItem().getAlbum().getImages().first().getUrl().toString();
				System.out.println("DEBUG> URL: " + url);
				setCover(new Image(url, false));
			}
		}
	}
	public void update(@NotNull Couple<PythiumBundle, SpotifyBundle> bundles) {
		update(Objects.requireNonNull(bundles.getY()));
	}
	public void update(@NotNull Showcase copy) {
		setMusicName(copy.getMusicName());
		setArtists(copy.getArtists());
		setCover(copy.getCover());
	}
	
	/* GETTERS & SETTERS */
	
	@NotNull
	public String getMusicName() {
		return musicName;
	}
	
	public void setMusicName(@NotNull String musicName) {
		this.musicName = musicName;
		
		if (tx_musicName != null)
			tx_musicName.setText(this.musicName);
	}
	
	public void initMusicName() {
		setMusicName("Unknown");
	}
	
	@NotNull
	public String getArtists() {
		return artists;
	}
	
	public void setArtists(@NotNull String artists) {
		this.artists = artists;
		
		if (tx_artists != null)
			tx_artists.setText(this.artists);
	}
	
	public void initArtists() {
		setArtists("Unknown");
	}
	
	@NotNull
	public Image getCover() {
		return cover;
	}
	
	public void setCover(@NotNull Image cover) {
		this.cover = cover;
		
		if (iv_cover != null)
			iv_cover.setImage(this.cover);
	}
	
	public void initCover() {
		setCover(new Image(getClass().getResourceAsStream("/images/lythium256.png")));
	}
}
