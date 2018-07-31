package fr.berger.lythium.bundles.spotifyobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class MixIn {

	@JsonIgnore
	abstract String type();
}
