package fr.berger.lythium.bundles.spotifyobjects;

import fr.berger.beyondcode.util.EnhancedObservable;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Image extends EnhancedObservable {

	/* VARIABLES */
	
	private int width;
	private int height;
	private URL url;
	
	/* CONSTRUCTOR */
	
	public Image() {}
	public Image(@NotNull Image copy) {
		setWidth(copy.getWidth());
		setHeight(copy.getHeight());
		setUrl(copy.getUrl());
	}
	
	/* GETTERS & SETTERS */
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public void setUrl(URL url) {
		this.url = url;
	}
	
	/* IMAGE METHOD */
	
	public BufferedImage getJavaXImage() throws IOException {
		return ImageIO.read(getUrl());
	}
	
	/* NESTED CLASS */
	
	public class Builder {
		
		private Image instance;
		
		public Builder(@NotNull Image image) {
			this.instance = image;
		}
		public Builder() {
			this(new Image());
		}
		
		public Builder setWidth(int width) {
			instance.setWidth(width);
			return this;
		}
		
		public Builder setHeight(int height) {
			instance.setHeight(height);
			return this;
		}
		
		public Builder setUrl(URL url) {
			instance.setUrl(url);
			return this;
		}
		
		public Image build() {
			return new Image(instance);
		}
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Image)) return false;
		Image image = (Image) o;
		return getWidth() == image.getWidth() &&
				getHeight() == image.getHeight() &&
				Objects.equals(getUrl(), image.getUrl());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getWidth(), getHeight(), getUrl());
	}
	
	@Override
	public String toString() {
		return "Image{" +
				"width=" + width +
				", height=" + height +
				", url=" + url +
				'}';
	}
}
